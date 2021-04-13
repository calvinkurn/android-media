package com.tokopedia.topchat.chatroom.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topchat.chatroom.data.ImageUploadServiceModel
import com.tokopedia.topchat.chatroom.data.UploadImageDummy
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.domain.usecase.ReplyChatGQLUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.TopchatUploadImageUseCase
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.common.mapper.ImageUploadMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class UploadImageChatService: JobIntentService(), CoroutineScope {

    @Inject
    lateinit var uploadImageUseCase: TopchatUploadImageUseCase
    @Inject
    lateinit var replyChatGQLUseCase: ReplyChatGQLUseCase
    @Inject
    lateinit var dispatcher: CoroutineDispatchers

    private var image: ImageUploadViewModel? = null
    private var messageId = ""
    private var notificationManager: UploadImageNotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        inject()
    }

    open fun inject() {
        DaggerChatComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .chatRoomContextModule(ChatRoomContextModule(this))
                .build()
                .inject(this)
    }

    private fun initNotificationManager() {
        if (notificationManager == null) {
            notificationManager = object: UploadImageNotificationManager(this@UploadImageChatService) {
                override fun getFailedIntent(errorMessage: String): PendingIntent {
                    val intent = createLocalChatRoomIntent()
                    return PendingIntent.getActivity(this@UploadImageChatService, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }
            }
        }
    }

    private fun createLocalChatRoomIntent(): Intent {
        val intent = Intent(this, TopChatRoomActivity::class.java)
        intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        return intent
    }

    override fun onHandleWork(intent: Intent) {
        initNotificationManager()
        setupData(intent)
        uploadImage()
    }

    private fun setupData(intent: Intent) {
        messageId = intent.getStringExtra(MESSAGE_ID,)?: ""
        val imageUploadService = intent.getParcelableExtra<ImageUploadServiceModel>(IMAGE)
        imageUploadService?.let {
            image =  ImageUploadMapper.mapToImageUploadViewModel(it)
        }
    }

    private fun uploadImage() {
        image?.let {
            uploadImageUseCase.upload(it, ::onSuccessUploadImage, ::onErrorUploadImage)
        }
    }

    private fun onSuccessUploadImage(uploadId: String, dummyMessage: ImageUploadViewModel) {
        sendImageByGQL(messageId, "Uploaded Image", uploadId, dummyMessage)
    }

    private fun sendImageByGQL(msgId: String, msg: String, filePath: String, dummyMessage: ImageUploadViewModel) {
        launchCatchError(block = {
            withContext(dispatcher.io) {
                val result = replyChatGQLUseCase.replyMessage(msgId, msg, filePath, dummyMessage.source)
                if(result.data.attachment.attributes.isNotEmpty()) {
                    removeDummyOnList(dummyMessage)
                    sendSuccessBroadcast()
                }
            }
        },
        onError = {
            onErrorUploadImage(it, dummyMessage)
        })
    }

    private fun sendSuccessBroadcast() {
        val result = Intent(BROADCAST_UPLOAD_IMAGE)
        result.putExtras(generateBundleSuccess())
        LocalBroadcastManager.getInstance(this).sendBroadcast(result)
    }

    private fun generateBundleSuccess(): Bundle {
        val bundle = Bundle()
        bundle.putString(MESSAGE_ID, messageId)
        image?.let {
            bundle.putParcelable(IMAGE, ImageUploadMapper.mapToImageUploadServer(it))
        }
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_DONE)
        return bundle
    }

    private fun onErrorUploadImage(throwable: Throwable, image: ImageUploadViewModel) {
        val position = findDummy(image)?: -1
        flagDummyInPosition(position)

        val result = Intent(BROADCAST_UPLOAD_IMAGE)
        result.putExtras(generateBundleError(position, throwable))
        LocalBroadcastManager.getInstance(this).sendBroadcast(result)

        firebaseLogError(throwable)

        val errorMessage = ErrorHandler.getErrorMessage(this@UploadImageChatService, throwable)
        notificationManager?.onFailedUpload(errorMessage)
    }

    private fun flagDummyInPosition(position: Int) {
        if(position > -1) {
            dummyMap[position].isFail = true
        }
    }

    private fun generateBundleError(position: Int, throwable: Throwable): Bundle {
        val bundle = Bundle()
        bundle.putString(MESSAGE_ID, messageId)
        bundle.putString(ERROR_MESSAGE, ErrorHandler.getErrorMessage(this, throwable))
        bundle.putInt(RETRY_POSITION, position)
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_ERROR)
        return bundle
    }

    override fun onDestroy() {
        super.onDestroy()
        replyChatGQLUseCase.cancel()
    }

    private fun firebaseLogError(throwable: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = dispatcher.io + SupervisorJob()

    companion object {
        const val JOB_ID_UPLOAD_IMAGE = 813
        const val IMAGE = "image"
        const val RETRY_POSITION = "retryPosition"
        const val ERROR_MESSAGE = "errorMessage"
        const val MESSAGE_ID = "messageId"
        const val BROADCAST_UPLOAD_IMAGE = "BROADCAST_UPLOAD_IMAGE"
        var dummyMap = arrayListOf<UploadImageDummy>()

        fun enqueueWork(context: Context, image: ImageUploadServiceModel, messageId: String) {
            val intent = Intent(context, UploadImageChatService::class.java)
            intent.putExtra(IMAGE, image)
            intent.putExtra(MESSAGE_ID, messageId)
            enqueueWork(context, UploadImageChatService::class.java, JOB_ID_UPLOAD_IMAGE, intent)
        }

        @Synchronized
        fun removeDummyOnList(dummy: Visitable<*>) {
            val tmpDummy: Int? = findDummy(dummy)
            tmpDummy?.let {tmp ->
                dummyMap.removeAt(tmp)
            }
        }

        private fun findDummy(dummy: Visitable<*>): Int? {
            for(i in 0 until dummyMap.size) {
                val temp = (dummyMap[i].visitable as SendableViewModel)
                if (temp.startTime == (dummy as SendableViewModel).startTime
                        && temp.messageId == (dummy as SendableViewModel).messageId) {
                    return i
                }
            }
            return null
        }
    }
}