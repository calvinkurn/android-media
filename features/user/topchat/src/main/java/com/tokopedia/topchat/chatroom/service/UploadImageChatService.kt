package com.tokopedia.topchat.chatroom.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.ResultReceiver
import androidx.core.app.JobIntentService
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.DaggerChatComponent
import com.tokopedia.topchat.chatroom.domain.usecase.TopchatUploadImageUseCase
import javax.inject.Inject

class UploadImageChatService: JobIntentService() {

    @Inject
    lateinit var uploadImageUseCase: TopchatUploadImageUseCase

    private var resultReceiver: ResultReceiver? = null
    private var image: ImageUploadViewModel? = null

    companion object {
        private const val JOB_ID_UPLOAD_IMAGE = 813
        private const val RECEIVER = "receiver"
        const val UPLOAD_ID = "uploadId"
        const val IS_SUCCESS = "isSuccess"
        const val IMAGE = "image"
        const val ERROR_MESSAGE = "errorMessage"
        const val SHOW_RESULT = 913

        var tempImageUploadViewModel: ImageUploadViewModel? = null

        fun enqueueWork(context: Context, resultReceiver: UploadImageServiceReceiver, image: ImageUploadViewModel) {
            val intent = Intent(context, UploadImageChatService::class.java)
            intent.putExtra(IMAGE, image)
            intent.putExtra(RECEIVER, resultReceiver)
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

    override fun onHandleWork(intent: Intent) {
        resultReceiver = intent.getParcelableExtra(RECEIVER)
        image = intent.getSerializableExtra(IMAGE) as ImageUploadViewModel
        image?.let {
            tempImageUploadViewModel = image
            uploadImageUseCase.upload(it, ::onSuccessUploadImage, ::onErrorUploadImage)
        }
    }

    private fun onSuccessUploadImage(uploadId: String, image: ImageUploadViewModel) {
        val bundle = Bundle()
        bundle.putBoolean(IS_SUCCESS, true)
        bundle.putString(UPLOAD_ID, uploadId)
        bundle.putSerializable(IMAGE, image)
        tempImageUploadViewModel = null
        resultReceiver?.send(SHOW_RESULT, bundle)
    }

    private fun onErrorUploadImage(throwable: Throwable, image: ImageUploadViewModel) {
        val bundle = Bundle()
        bundle.putBoolean(IS_SUCCESS, false)
        bundle.putString(ERROR_MESSAGE, ErrorHandler.getErrorMessage(this, throwable))
        bundle.putSerializable(IMAGE, image)
        tempImageUploadViewModel = null
        resultReceiver?.send(SHOW_RESULT, bundle)
    }
}