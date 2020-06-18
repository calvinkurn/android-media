package com.tokopedia.play.broadcaster.view.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.model.PlayCoverUploadEntity
import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetOriginalProductImageUseCase
import com.tokopedia.play.broadcaster.ui.model.CoverSourceEnum
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.cover.ImageTransformer
import com.tokopedia.play.broadcaster.util.cover.PlayCoverImageUtil
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * @author by furqan on 09/06/2020
 */
class PlayBroadcastCoverSetupViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val setupDataStore: PlayBroadcastSetupDataStore,
        private val uploadImageUseCase: UploadImageUseCase<PlayCoverUploadEntity>,
        private val getOriginalProductImageUseCase: GetOriginalProductImageUseCase,
        private val userSession: UserSessionInterface,
        private val coverImageUtil: PlayCoverImageUtil,
        private val coverImageTransformer: ImageTransformer
) : BaseViewModel(dispatcher.main) {

    val coverUri: Uri?
        get() {
            return when(val cropState = observableCropState.value) {
                is CoverSetupState.Cropped -> cropState.coverImage
                is CoverSetupState.Cropping -> cropState.coverImage
                else -> null
            }
        }

    val observableCropState: LiveData<CoverSetupState>
        get() = _observableCropState
    private val _observableCropState = MutableLiveData<CoverSetupState>().apply {
        val selectedCover = setupDataStore.getSelectedCover()
        value =
                if (selectedCover == null) CoverSetupState.Blank
                else CoverSetupState.Cropped(selectedCover.coverImage)
    }

    val observableSelectedProducts: LiveData<List<ProductContentUiModel>>
        get() = setupDataStore.getObservableSelectedProducts()

    private val mutableOriginalImageUri = MutableLiveData<Uri>()
    val originalImageUri: LiveData<Uri>
        get() = mutableOriginalImageUri
    private val mutableImageEcsLink = MutableLiveData<String>()
    val imageEcsLink: LiveData<String>
        get() = mutableImageEcsLink

    val maxTitleChars: Int
        get() = MAX_CHARS

    fun isValidCoverTitle(coverTitle: String): Boolean {
        return coverTitle.isNotEmpty() && coverTitle.length <= MAX_CHARS
    }

    suspend fun getOriginalImageUrl(productId: Long, resizedImageUrl: String): String? = suspendCancellableCoroutine {
        launchCatchError(dispatcher.io, block = {
            val originalImageUrlList = getOriginalProductImageUseCase.apply {
                params = GetOriginalProductImageUseCase.createParams(productId)
            }.executeOnBackground()
            yield()
            val resizedUrlLastSegments = resizedImageUrl.split("/")
                    .let { it[it.lastIndex] }
            val originalImageUrl = originalImageUrlList.first {
                it.contains(resizedUrlLastSegments)
            }
            it.resume(originalImageUrl)
        }) { err ->
            err.printStackTrace()
            it.resumeWithException(err)
        }
    }

    fun setCover(imagePath: String, coverTitle: String) {
        launchCatchError(block = {
            val url = withContext(dispatcher.io) {
                val params = hashMapOf<String, RequestBody>()
                params[PARAM_WEB_SERVICE] = RequestBody.create(MediaType.parse(TEXT_PLAIN), DEFAULT_WEB_SERVICE)
                params[PARAM_RESOLUTION] = RequestBody.create(MediaType.parse(TEXT_PLAIN), RESOLUTION_700)
                params[PARAM_ID] = RequestBody.create(MediaType.parse(TEXT_PLAIN),
                        "${userSession.userId}${UUID.randomUUID()}${System.currentTimeMillis()}")

                val dataUploadedImage = uploadImageUseCase
                        .createObservable(uploadImageUseCase.createRequestParam(
                                imagePath,
                                DEFAULT_UPLOAD_PATH,
                                DEFAULT_UPLOAD_TYPE,
                                params))
                        .toBlocking()
                        .first()
                        .dataResultImageUpload

                dataUploadedImage.data.picSrc.let {
                    if (it.contains(DEFAULT_RESOLUTION)) it.replaceFirst(DEFAULT_RESOLUTION, RESOLUTION_700)
                    else it
                }
            }

            setupDataStore.setCover(
                    PlayCoverUiModel(Uri.parse(url), coverTitle)
            )
        }) {
            it.printStackTrace()
        }
    }

    fun setCroppingCover(coverUri: Uri, source: CoverSourceEnum) {
        _observableCropState.value = CoverSetupState.Cropping(coverUri, source)
    }

    fun setCroppingCover(bitmap: Bitmap, source: CoverSourceEnum) {
        setCroppingCover(
                getImagePathFromBitmap(bitmap),
                source
        )
    }

    fun setCroppedCover(coverUri: Uri) {
        val imageValidated = validateImageMinSize(coverUri)
        _observableCropState.value = CoverSetupState.Cropped(imageValidated)
    }

    /**
     * Make sure new image won't be smaller than required minimum size
     */
    private fun validateImageMinSize(imageUri: Uri): Uri {
        return coverImageTransformer.transformImageFromUri(imageUri)
    }

    private fun getImagePathFromBitmap(bitmap: Bitmap): Uri {
        return coverImageUtil.getImagePathFromBitmap(bitmap)
    }

    companion object {
        private const val PARAM_ID = "id"
        private const val PARAM_WEB_SERVICE = "web_service"
        private const val PARAM_RESOLUTION = "param_resolution"
        private const val DEFAULT_RESOLUTION = "100-square"
        private const val DEFAULT_WEB_SERVICE = "1"
        private const val DEFAULT_UPLOAD_PATH = "/upload/attachment"
        private const val DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg"
        private const val TEXT_PLAIN = "text/plain"
        private const val RESOLUTION_700 = "700"

        private const val MAX_CHARS = 38
    }

}