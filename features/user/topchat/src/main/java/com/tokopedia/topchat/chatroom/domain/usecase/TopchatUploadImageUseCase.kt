package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TopchatUploadImageUseCase @Inject constructor(
        private var uploadImageUseCase: UploaderUseCase,
        private var chatImageServerUseCase: ChatImageServerUseCase,
        private var dispatchers: TopchatCoroutineContextProvider
) : CoroutineScope {

    var isUploading: Boolean = false
        private set

    private var onSuccess: ((String, ImageUploadViewModel) -> Unit)? = null
    private var onError: ((Throwable, ImageUploadViewModel) -> Unit)? = null

    override val coroutineContext: CoroutineContext get() = dispatchers.Main + SupervisorJob()

    fun upload(
            image: ImageUploadViewModel,
            onSuccess: (String, ImageUploadViewModel) -> Unit,
            onError: (Throwable, ImageUploadViewModel) -> Unit
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
            onSuccess: (String, ImageUploadViewModel) -> Unit,
            onError: (Throwable, ImageUploadViewModel) -> Unit
    ) {
        this.onSuccess = onSuccess
        this.onError = onError
    }

    private fun uploadImageWithSourceId(sourceId: String, image: ImageUploadViewModel) {
        launch(dispatchers.IO) {
            val requestParams = uploadImageUseCase.createParams(sourceId, File(image.imageUrl))
            val result = uploadImageUseCase(requestParams)
            setUploading(false)
            withContext(dispatchers.Main) {
                when (result) {
                    is UploadResult.Success -> onSuccess?.invoke(result.uploadId, image)
                    is UploadResult.Error -> onErrorUploadImage(result, image)
                }
            }
        }
    }

    private fun onErrorUploadImage(result: UploadResult.Error, image: ImageUploadViewModel) {
        val error = Throwable("Something went wrong")
        onError?.invoke(error, image)
    }

    private fun setUploading(isUploading: Boolean) {
        this.isUploading = isUploading
    }
}