package com.tokopedia.topchat.chatroom.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.ReplyChatViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.domain.usecase.ReplyChatUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.TopchatUploadImageUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class UploadImageChatService: JobIntentService() {

    @Inject
    lateinit var uploadImageUseCase: TopchatUploadImageUseCase
    @Inject
    lateinit var replyChatUseCase: ReplyChatUseCase
    private var image: ImageUploadViewModel? = null
    private var messageId = ""

    private var notificationManager: UploadImageNotificationManager? = null

    companion object {
        private const val JOB_ID_UPLOAD_IMAGE = 813
        const val IMAGE = "image"
        const val ERROR_MESSAGE = "errorMessage"
        const val MESSAGE_ID = "messageId"
        const val BROADCAST_UPLOAD_IMAGE = "BROADCAST_UPLOAD_IMAGE"
        var dummyMap = hashMapOf<String, ArrayList<Visitable<*>>>()

        fun enqueueWork(context: Context, image: ImageUploadViewModel, messageId: String) {
            val intent = Intent(context, UploadImageChatService::class.java)
            intent.putExtra(IMAGE, image)
            intent.putExtra(MESSAGE_ID, messageId)
            enqueueWork(context, UploadImageChatService::class.java, JOB_ID_UPLOAD_IMAGE, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        DaggerChatComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .chatRoomContextModule(ChatRoomContextModule(this))
                .build()
                .inject(this)
    }

    private fun initNotificationManager() {
        if (notificationManager == null) {
            notificationManager = object: UploadImageNotificationManager(this@UploadImageChatService) {
                override fun getSuccessIntent(): PendingIntent {
                    val intent = Intent()
                    return PendingIntent.getActivity(this@UploadImageChatService, 0, intent, 0)
                }

                override fun getFailedIntent(errorMessage: String): PendingIntent {
                    val intent = Intent()
                    return PendingIntent.getActivity(this@UploadImageChatService, 0, intent, 0)
                }
            }
        }
    }

    override fun onHandleWork(intent: Intent) {
        initNotificationManager()
        messageId = intent.getStringExtra(MESSAGE_ID,)?: ""
        image = intent.getSerializableExtra(IMAGE) as ImageUploadViewModel
        image?.let {
            uploadImageUseCase.upload(it, ::onSuccessUploadImage, ::onErrorUploadImage)
        }
        notificationManager?.onStartUpload()
    }

    private fun onSuccessUploadImage(uploadId: String, image: ImageUploadViewModel) {
        sendImageByApi(uploadId, image)
    }

    private fun sendImageByApi(uploadId: String, image: ImageUploadViewModel) {
        val requestParams = ReplyChatUseCase.generateParamAttachImage(messageId, uploadId)
        sendByApi(requestParams, image)
    }

    private fun sendByApi(requestParams: RequestParams, dummyMessage: Visitable<*>) {
        replyChatUseCase.execute(requestParams, object : Subscriber<ReplyChatViewModel>() {
            override fun onNext(response: ReplyChatViewModel) {
                if (response.isSuccessReplyChat) {
                    notificationManager?.onSuccessUpload()
                }
                removeDummyOnList(dummyMessage)
                image?.let {
                    sendSuccessBroadcast(it)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                val errorMessage = ErrorHandler.getErrorMessage(this@UploadImageChatService, e)
                notificationManager?.onFailedUpload(errorMessage)
                removeDummyOnList(dummyMessage)
                image?.let {
                    onErrorUploadImage(e, it)
                }
            }
        })
    }

    private fun sendSuccessBroadcast(image: ImageUploadViewModel) {
        val result = Intent(BROADCAST_UPLOAD_IMAGE)

        val bundle = Bundle()
        bundle.putSerializable(IMAGE, image)
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_DONE)

        result.putExtras(bundle)
        LocalBroadcastManager.getInstance(this).sendBroadcast(result)
    }

    private fun onErrorUploadImage(throwable: Throwable, image: ImageUploadViewModel) {
        val result = Intent(BROADCAST_UPLOAD_IMAGE)

        val bundle = Bundle()
        bundle.putString(ERROR_MESSAGE, ErrorHandler.getErrorMessage(this, throwable))
        bundle.putSerializable(IMAGE, image)
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_ERROR)

        result.putExtras(bundle)
        LocalBroadcastManager.getInstance(this).sendBroadcast(result)
    }

    @Synchronized
    private fun removeDummyOnList(dummy: Visitable<*>) {
        var tmpDummy: Visitable<*>? = null
        dummyMap[messageId]?.let {
            for(i in 0 until it.size) {
                val temp = (it[i] as SendableViewModel)
                if (temp.startTime == (dummy as SendableViewModel).startTime
                        && temp.messageId == (dummy as SendableViewModel).messageId) {
                    tmpDummy = it[i]
                    break
                }
            }
            tmpDummy?.let {tmp ->
                it.remove(tmp)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        replyChatUseCase.unsubscribe()
    }
}