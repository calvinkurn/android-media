package com.tokopedia.play.broadcaster.view.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.model.PlayCoverUploadEntity
import com.tokopedia.play.broadcaster.domain.usecase.GetOriginalProductImageUseCase
import com.tokopedia.play.broadcaster.ui.model.CarouselCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.cover.ImageTransformer
import com.tokopedia.play.broadcaster.util.cover.PlayCoverImageUtil
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.delay
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
class PlayCoverSetupViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val setupDataStore: PlayBroadcastSetupDataStore,
        private val uploadImageUseCase: UploadImageUseCase<PlayCoverUploadEntity>,
        private val getOriginalProductImageUseCase: GetOriginalProductImageUseCase,
        private val userSession: UserSessionInterface,
        private val coverImageUtil: PlayCoverImageUtil,
        private val coverImageTransformer: ImageTransformer
) : BaseViewModel(dispatcher.main) {

    val cropState: CoverSetupState
        get() {
            return observableCropState.value ?: CoverSetupState.Blank
        }

    val coverUri: Uri?
        get() {
            return when(val cropState = observableCropState.value) {
                is CoverSetupState.Cropped -> cropState.coverImage
                is CoverSetupState.Cropping -> if (cropState is CoverSetupState.Cropping.Image) cropState.coverImage else null
                else -> null
            }
        }

    private val source: CoverSource
        get() {
            return when (val currentCropState = observableCropState.value) {
                is CoverSetupState.Cropped -> currentCropState.coverSource
                is CoverSetupState.Cropping -> currentCropState.coverSource
                else -> CoverSource.None
            }
        }

    val observableCropState: LiveData<CoverSetupState> = Transformations.map(setupDataStore.getObservableSelectedCover()) {
        it.croppedCover
    }

    val observableCoverTitle: LiveData<String>
        get() = Transformations.map(setupDataStore.getObservableSelectedCover()) {
            it.title
        }

    val observableSelectedProducts: LiveData<List<CarouselCoverUiModel.Product>> = Transformations.map(setupDataStore.getObservableSelectedProducts()) { dataList ->
        dataList.map { CarouselCoverUiModel.Product(ProductContentUiModel.createFromData(it)) }
    }

    val observableUploadCoverEvent: LiveData<NetworkResult<Event<Unit>>>
        get() = _observableUploadCoverEvent
    private val _observableUploadCoverEvent = MutableLiveData<NetworkResult<Event<Unit>>>()

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

    fun uploadCover(channelId: String, coverTitle: String) {
        _observableUploadCoverEvent.value = NetworkResult.Loading
        launchCatchError(block = {
            val currentCropState = cropState
            val path = if (currentCropState is CoverSetupState.Cropped) currentCropState.coverImage.path else throw IllegalStateException("Cover is not cropped yet")
            val url = uploadCoverToCloud(path)

            setupDataStore.updateCoverTitle(coverTitle)

//            val result = setupDataStore.uploadSelectedCover(channelId)
//            if (result is NetworkResult.Success) _observableUploadCoverEvent.value = NetworkResult.Success(Event(Unit))
//            else if (result is NetworkResult.Fail) throw result.error

            //TODO("Remove mock behavior")
            delay(3000)
            _observableUploadCoverEvent.value = NetworkResult.Success(Event(Unit))

        }) {
            it.printStackTrace()
            _observableUploadCoverEvent.value = NetworkResult.Fail(it)
        }
    }

    fun setCroppingCoverByUri(coverUri: Uri, source: CoverSource) {
        setupDataStore.updateCoverState(
                CoverSetupState.Cropping.Image(coverUri, source)
        )
    }

    fun setCroppingCoverByBitmap(bitmap: Bitmap, source: CoverSource) {
        setCroppingCoverByUri(
                getImagePathFromBitmap(bitmap),
                source
        )
    }

    fun setCroppingProductCover(productId: Long, imageUrl: String) {
        setupDataStore.updateCoverState(
                CoverSetupState.Cropping.Product(productId, imageUrl)
        )
    }

    fun setCroppedCover(coverUri: Uri) {
        val imageValidated = validateImageMinSize(coverUri)
        setupDataStore.updateCoverState(
                CoverSetupState.Cropped(imageValidated, source)
        )
    }

    fun removeCover() {
        setupDataStore.updateCoverState(
                CoverSetupState.Blank
        )
    }

    fun saveCover(coverTitle: String) {
        setupDataStore.updateCoverTitle(coverTitle)
    }

    private suspend fun uploadCoverToCloud(imagePath: String): String = withContext(dispatcher.io) {
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