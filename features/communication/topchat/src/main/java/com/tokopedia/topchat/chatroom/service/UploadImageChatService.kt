package com.tokopedia.topchat.chatroom.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.service.JobIntentServiceX
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
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

open class UploadImageChatService : JobIntentServiceX(), CoroutineScope {

    @Inject
    lateinit var uploadImageUseCase: TopchatUploadImageUseCase

    @Inject
    lateinit var replyChatGQLUseCase: ReplyChatGQLUseCase

    @Inject
    lateinit var dispatcher: CoroutineDispatchers

    private var image: ImageUploadUiModel? = null
    private var messageId = ""
    private var isSecure = false
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
            notificationManager = object : UploadImageNotificationManager(this@UploadImageChatService) {
                override fun getFailedIntent(errorMessage: String): PendingIntent {
                    val intent = createLocalChatRoomIntent()
                    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        PendingIntent.getActivity(this@UploadImageChatService, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
                    } else {
                        PendingIntent.getActivity(this@UploadImageChatService, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    }
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
        messageId = intent.getStringExtra(MESSAGE_ID) ?: ""
        isSecure = intent.getBooleanExtra(IS_SECURE, false)
        val imageUploadService = ImageUploadServiceModel(
            messageId = messageId,
            fromUid = intent.getStringExtra(FROM_UI_ID) ?: "",
            from = intent.getStringExtra(FROM) ?: "",
            fromRole = intent.getStringExtra(FROM_ROLE) ?: "",
            attachmentId = intent.getStringExtra(ATTACHMENT_ID) ?: "",
            attachmentType = intent.getStringExtra(ATTACHMENT_TYPE) ?: "",
            replyTime = intent.getStringExtra(REPLY_TIME) ?: "",
            startTime = intent.getStringExtra(START_TIME) ?: "",
            message = intent.getStringExtra(MESSAGE) ?: "",
            source = intent.getStringExtra(SOURCE) ?: "",
            imageUrl = intent.getStringExtra(IMAGE_URL) ?: "",
            imageUrlThumbnail = intent.getStringExtra(IMAGE_URL_THUMBNAIL) ?: "",
            parentReply = intent.getStringExtra(PARENT_REPLY) ?: "",
            localId = intent.getStringExtra(LOCAL_ID) ?: "",
            isRead = intent.getBooleanExtra(IS_READ, false),
            isDummy = intent.getBooleanExtra(IS_DUMMY, false),
            isSender = intent.getBooleanExtra(IS_SENDER, false),
            isRetry = intent.getBooleanExtra(IS_RETRY, false)
        )
        imageUploadService?.let {
            image = ImageUploadMapper.mapToImageUploadUiModel(it)
        }
    }

    private fun uploadImage() {
        image?.let {
            uploadImageUseCase.upload(it, ::onSuccessUploadImage, ::onErrorUploadImage, isSecure)
        }
    }

    private fun onSuccessUploadImage(
        filePath: String,
        dummyMessage: ImageUploadUiModel,
        isSecure: Boolean
    ) {
        sendImageByGQL(messageId, "Uploaded Image", filePath, dummyMessage, isSecure)
    }

    private fun sendImageByGQL(
        msgId: String,
        msg: String,
        filePath: String,
        dummyMessage: ImageUploadUiModel,
        isSecure: Boolean
    ) {
        launchCatchError(
            block = {
                withContext(dispatcher.io) {
                    val param = ReplyChatGQLUseCase.Param(
                        msgId = msgId,
                        msg = msg,
                        filePath = filePath,
                        source = dummyMessage.source,
                        parentReply = dummyMessage.parentReply
                    )
                    param.isSecure = isSecure
                    val response = replyChatGQLUseCase(param)
                    if (response.data.attachment.attributes.isNotEmpty()) {
                        removeDummyOnList(dummyMessage)
                        sendSuccessBroadcast()
                    }
                }
            },
            onError = {
                onErrorUploadImage(it, dummyMessage)
            }
        )
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

    private fun onErrorUploadImage(throwable: Throwable, image: ImageUploadUiModel) {
        val position = findDummy(image) ?: -1
        flagDummyInPosition(position)

        val result = Intent(BROADCAST_UPLOAD_IMAGE)
        result.putExtras(generateBundleError(position, throwable))
        LocalBroadcastManager.getInstance(this).sendBroadcast(result)

        ErrorHandler.getErrorMessage(
            this, throwable,
            ErrorHandler.Builder().apply {
                className = UploadImageChatService::class.java.name
            }.build()
        )

        val errorMessage = uploaderReadableError(throwable)
        notificationManager?.onFailedUpload(errorMessage)
    }

    private fun flagDummyInPosition(position: Int) {
        if (position > -1) {
            dummyMap[position].isFail = true
        }
    }

    private fun generateBundleError(position: Int, throwable: Throwable): Bundle {
        val bundle = Bundle()
        bundle.putString(MESSAGE_ID, messageId)
        bundle.putString(ERROR_MESSAGE, uploaderReadableError(throwable))
        bundle.putInt(RETRY_POSITION, position)
        bundle.putInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_ERROR)
        return bundle
    }

    override fun onDestroy() {
        super.onDestroy()
        replyChatGQLUseCase.cancel()
    }

    private fun uploaderReadableError(throwable: Throwable): String {
        // produced by uploader
        val throwableMessage = throwable.message ?: ""

        // produced by ErrorHandler following with `Kode Error:` at the end of message
        val errorMessage = ErrorHandler.getErrorMessage(this, throwable)

        // check if uploader error message contains the error-code or not
        val hasErrorCode = throwableMessage
            .indexOfFirst {
                it.toString().matches("[(<]".toRegex())
            }.takeIf {
                it > 0
            } ?: 0

        return if (!hasErrorCode.isZero()) {
            throwableMessage
        } else {
            errorMessage
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
        const val FROM_UI_ID = "fromUid"
        const val FROM = "from"
        const val FROM_ROLE = "fromRole"
        const val ATTACHMENT_ID = "attachmentId"
        const val ATTACHMENT_TYPE = "attachmentType"
        const val REPLY_TIME = "replyTime"
        const val START_TIME = "startTime"
        const val IS_READ = "isRead"
        const val IS_DUMMY = "isDummy"
        const val IS_SENDER = "isSender"
        const val MESSAGE = "message"
        const val SOURCE = "source"
        const val IMAGE_URL = "imageUrl"
        const val IMAGE_URL_THUMBNAIL = "imageUrlThumbnail"
        const val IS_RETRY = "isRetry"
        const val PARENT_REPLY = "parentReply"
        const val LOCAL_ID = "localId"
        const val BROADCAST_UPLOAD_IMAGE = "BROADCAST_UPLOAD_IMAGE"
        const val IS_SECURE = "isSecure"
        var dummyMap = arrayListOf<UploadImageDummy>()

        fun enqueueWork(
            context: Context,
            image: ImageUploadServiceModel,
            messageId: String,
            isSecure: Boolean
        ) {
            val intent = Intent(context, UploadImageChatService::class.java)
            intent.putExtra(MESSAGE_ID, messageId)
            intent.putExtra(MESSAGE, image.message)
            intent.putExtra(FROM_UI_ID, image.fromUid)
            intent.putExtra(FROM, image.from)
            intent.putExtra(FROM_ROLE, image.fromRole)
            intent.putExtra(ATTACHMENT_ID, image.attachmentId)
            intent.putExtra(ATTACHMENT_TYPE, image.attachmentType)
            intent.putExtra(REPLY_TIME, image.replyTime)
            intent.putExtra(START_TIME, image.startTime)
            intent.putExtra(PARENT_REPLY, image.parentReply)
            intent.putExtra(LOCAL_ID, image.localId)
            intent.putExtra(SOURCE, image.source)
            intent.putExtra(IMAGE_URL, image.imageUrl)
            intent.putExtra(IMAGE_URL_THUMBNAIL, image.imageUrlThumbnail)
            intent.putExtra(IS_SENDER, image.isSender)
            intent.putExtra(IS_RETRY, image.isRetry)
            intent.putExtra(IS_READ, image.isRead)
            intent.putExtra(IS_DUMMY, image.isDummy)
            intent.putExtra(IS_SECURE, isSecure)
            enqueueWork(context, UploadImageChatService::class.java, JOB_ID_UPLOAD_IMAGE, intent)
        }

        @Synchronized
        fun removeDummyOnList(dummy: Visitable<*>) {
            val tmpDummy: Int? = findDummy(dummy)
            tmpDummy?.let { tmp ->
                dummyMap.removeAt(tmp)
            }
        }

        fun findDummy(dummy: Visitable<*>): Int? {
            for (i in 0 until dummyMap.size) {
                val temp = (dummyMap[i].visitable as SendableUiModel)
                if (temp.startTime == (dummy as SendableUiModel).startTime &&
                    temp.messageId == (dummy as SendableUiModel).messageId
                ) {
                    return i
                }
            }
            return null
        }
    }
}
