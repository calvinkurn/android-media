package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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

    private var onSuccess: ((String, ImageUploadUiModel) -> Unit)? = null
    private var onError: ((Throwable, ImageUploadUiModel) -> Unit)? = null

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    open fun upload(
        image: ImageUploadUiModel,
        onSuccess: (String, ImageUploadUiModel) -> Unit,
        onError: (Throwable, ImageUploadUiModel) -> Unit
    ) {
        setUploading(true)
        assignCallback(onSuccess, onError)
        chatImageServerUseCase.getSourceId(
                { sourceId -> uploadImageWithSourceId(sourceId, image) },
                { throwable ->
                    this.onError?.invoke(throwable, image)
                    setUploading(false)
                }
        )
    }

    private fun assignCallback(
        onSuccess: (String, ImageUploadUiModel) -> Unit,
        onError: (Throwable, ImageUploadUiModel) -> Unit
    ) {
        this.onSuccess = onSuccess
        this.onError = onError
    }

    private fun uploadImageWithSourceId(sourceId: String, image: ImageUploadUiModel) {
        launch(dispatchers.io) {
            val requestParams = uploadImageUseCase.createParams(sourceId, File(image.imageUrl))
            val result = uploadImageUseCase(requestParams)
            setUploading(false)
            withContext(dispatchers.main) {
                when (result) {
                    is UploadResult.Success -> onSuccess?.invoke(result.uploadId, image)
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