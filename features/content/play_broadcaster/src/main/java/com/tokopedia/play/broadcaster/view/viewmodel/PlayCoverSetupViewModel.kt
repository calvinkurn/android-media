package com.tokopedia.play.broadcaster.view.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetOriginalProductImageUseCase
import com.tokopedia.play.broadcaster.domain.usecase.UploadImageToRemoteV2UseCase
import com.tokopedia.play.broadcaster.error.CoverChangeForbiddenException
import com.tokopedia.play.broadcaster.ui.model.CarouselCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.util.cover.ImageTransformer
import com.tokopedia.play.broadcaster.util.cover.PlayCoverImageUtil
import com.tokopedia.play.broadcaster.view.state.*
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.result.map
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import javax.inject.Inject

/**
 * @author by furqan on 09/06/2020
 */
class PlayCoverSetupViewModel @Inject constructor(
        private val hydraConfigStore: HydraConfigStore,
        private val dispatcher: CoroutineDispatchers,
        private val setupDataStore: PlayBroadcastSetupDataStore,
        private val uploadImageUseCase: UploadImageToRemoteV2UseCase,
        private val getOriginalProductImageUseCase: GetOriginalProductImageUseCase,
        private val coverImageUtil: PlayCoverImageUtil,
        private val coverImageTransformer: ImageTransformer
) : BaseViewModel(dispatcher.main) {

    private val channelId: String
        get() = hydraConfigStore.getChannelId()

    val cropState: CoverSetupState
        get() {
            return observableCropState.value ?: CoverSetupState.Blank
        }

    val coverUri: Uri?
        get() {
            return when(val cropState = observableCropState.value) {
                is CoverSetupState.Cropped.Draft -> cropState.coverImage
                is CoverSetupState.Cropped.Uploaded -> cropState.localImage
                is CoverSetupState.Cropping -> if (cropState is CoverSetupState.Cropping.Image) cropState.coverImage else null
                else -> null
            }
        }

    val source: CoverSource
        get() {
            return when (val currentCropState = observableCropState.value) {
                is CoverSetupState.Cropped -> currentCropState.coverSource
                is CoverSetupState.Cropping -> currentCropState.coverSource
                else -> CoverSource.None
            }
        }

    val observableCropState: LiveData<CoverSetupState> = Transformations.map(setupDataStore.getObservableSelectedCover()) {
        println("Latest Crop State: $it")
        it.croppedCover
    }

    val observableSelectedProducts: LiveData<List<CarouselCoverUiModel.Product>> = setupDataStore.getObservableSelectedProducts()
            .map { dataList -> dataList.map { CarouselCoverUiModel.Product(ProductContentUiModel.createFromData(it)) } }
            .asLiveData(viewModelScope.coroutineContext + dispatcher.computation)

    val observableUploadCoverEvent: LiveData<NetworkResult<Event<Unit>>>
        get() = _observableUploadCoverEvent
    private val _observableUploadCoverEvent = MutableLiveData<NetworkResult<Event<Unit>>>()

    val maxTitleChars: Int
        get() = hydraConfigStore.getMaxTitleChars()

    fun isValidCoverTitle(coverTitle: String): Boolean {
        return coverTitle.isNotBlank() && coverTitle.length <= maxTitleChars
    }

    suspend fun getOriginalImageUrl(productId: Long, resizedImageUrl: String): String? = withContext(dispatcher.io) {
        val originalImageUrlList = getOriginalProductImageUseCase.apply {
            params = GetOriginalProductImageUseCase.createParams(productId)
        }.executeOnBackground()
        yield()

        val resizedUrlLastSegments = resizedImageUrl.split("/")
                .let { it[it.lastIndex] }

        return@withContext originalImageUrlList.first {
            it.contains(resizedUrlLastSegments)
        }
    }

    fun uploadCover() {
        _observableUploadCoverEvent.value = NetworkResult.Loading
        launchCatchError(block = {
            uploadImageAndUpdateCoverState()

            val result = setupDataStore.uploadSelectedCover(channelId)
            if (result is NetworkResult.Success) _observableUploadCoverEvent.value = result.map { Event(Unit) }
            else if (result is NetworkResult.Fail) throw result.error
        }) {
            it.printStackTrace()
            _observableUploadCoverEvent.value = NetworkResult.Fail(it)
        }
    }

    fun coverChangeState(): CoverChangeState {
        return if (observableUploadCoverEvent.value !is NetworkResult.Loading) Changeable
        else NotChangeable(CoverChangeForbiddenException("Cover is being uploaded to server. Please wait..."))
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

    fun setDraftCroppedCover(coverUri: Uri) {
        setupDataStore.updateCoverState(
                CoverSetupState.Cropped.Draft(coverUri, source)
        )
    }

    fun removeCover() {
        setupDataStore.updateCoverState(
                CoverSetupState.Blank
        )
    }

    private suspend fun uploadImageAndUpdateCoverState() {
        val currentCropState = cropState
        if (currentCropState is CoverSetupState.Cropped.Draft) {
            val validatedImageUri = validateImageMinSize(currentCropState.coverImage)
            val validatedImagePath = validatedImageUri.path
            if (validatedImagePath != null) {
                /**
                 * Set validated image cover as draft
                 */
                setupDataStore.setFullCover(
                        PlayCoverUiModel(
                                croppedCover = CoverSetupState.Cropped.Draft(validatedImageUri, source),
                                state = SetupDataState.Draft
                        )
                )
                /**
                 * Upload Cover Image to remote store
                 */
                val uploadedImageUri = Uri.parse(uploadImageToRemoteStore(validatedImagePath))
                setupDataStore.setFullCover(
                        PlayCoverUiModel(
                                croppedCover = CoverSetupState.Cropped.Uploaded(validatedImageUri, uploadedImageUri, source),
                                state = SetupDataState.Draft
                        )
                )
            } else throw IllegalStateException("Error in validating image")
        } else if (currentCropState !is CoverSetupState.Cropped.Uploaded) {
            throw IllegalStateException("Cover is not cropped yet")
        }
    }

    private suspend fun uploadImageToRemoteStore(imagePath: String): String = withContext(dispatcher.io) {
        uploadImageUseCase.setParams(File(imagePath))

        return@withContext when (val uploadedImage = uploadImageUseCase.executeOnBackground()) {
            is UploadResult.Success -> uploadedImage.uploadId
            is UploadResult.Error -> error(uploadedImage.message)
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
}