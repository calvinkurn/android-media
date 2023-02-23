package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class TopchatUploadImageUseCase @Inject constructor(
    private var uploadImageUseCase: UploaderUseCase,
    private var chatImageServerUseCase: ChatImageServerUseCase,
    private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    var isUploading: Boolean = false
        private set

    private var onSuccess: ((String, ImageUploadUiModel, Boolean) -> Unit)? = null
    private var onError: ((Throwable, ImageUploadUiModel) -> Unit)? = null

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    open fun upload(
        image: ImageUploadUiModel,
        onSuccess: (String, ImageUploadUiModel, Boolean) -> Unit,
        onError: (Throwable, ImageUploadUiModel) -> Unit,
        isSecure: Boolean
    ) {
        setUploading(true)
        assignCallback(onSuccess, onError)
        chatImageServerUseCase.getSourceId(
            { sourceId, sourceIdSecure ->
                val id = if (isSecure) {
                    sourceIdSecure
                } else {
                    sourceId
                }
                uploadImageWithSourceId(id, image, isSecure)
            },
            { throwable ->
                this.onError?.invoke(throwable, image)
                setUploading(false)
            }
        )
    }

    private fun assignCallback(
        onSuccess: (String, ImageUploadUiModel, Boolean) -> Unit,
        onError: (Throwable, ImageUploadUiModel) -> Unit
    ) {
        this.onSuccess = onSuccess
        this.onError = onError
    }

    private fun uploadImageWithSourceId(
        sourceId: String,
        image: ImageUploadUiModel,
        isSecure: Boolean
    ) {
        launch(dispatchers.io) {
            val requestParams = uploadImageUseCase.createParams(
                sourceId = sourceId,
                filePath = File(image.imageUrl),
                isSecure = isSecure,
                withTranscode = false,
                extraBody = mapOf("msg_id" to image.messageId)
            )
            val result = uploadImageUseCase(requestParams)
            setUploading(false)
            withContext(dispatchers.main) {
                when (result) {
                    is UploadResult.Success -> {
                        val filePath = if (isSecure) {
                            result.fileUrl
                        } else {
                            result.uploadId
                        }
                        onSuccess?.invoke(filePath, image, isSecure)
                    }
                    is UploadResult.Error -> onErrorUploadImage(result, image)
                }
            }
        }
    }

    private fun onErrorUploadImage(result: UploadResult.Error, image: ImageUploadUiModel) {
        val error = MessageErrorException(result.message)
        onError?.invoke(error, image)
    }

    private fun setUploading(isUploading: Boolean) {
        this.isUploading = isUploading
    }
}
