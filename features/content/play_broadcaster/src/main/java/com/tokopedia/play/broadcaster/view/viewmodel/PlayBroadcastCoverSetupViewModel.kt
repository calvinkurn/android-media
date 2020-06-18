package com.tokopedia.play.broadcaster.view.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.model.PlayCoverUploadEntity
import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetOriginalProductImageUseCase
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.cover.ImageTransformer
import com.tokopedia.play.broadcaster.util.cover.PlayCoverImageUtil
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

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

    val observableSelectedCover: LiveData<PlayCoverUiModel>
        get() = setupDataStore.getObservableSelectedCover()

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

    fun getOriginalImageUrl(context: Context, productId: Long, resizedImageUrl: String) {
        launchCatchError(dispatcher.io, block = {
            val originalImageUrlList = getOriginalProductImageUseCase.apply {
                params = GetOriginalProductImageUseCase.createParams(productId)
            }.executeOnBackground()
            val resizedUrlLastSegments = resizedImageUrl.split("/")
                    .let { it[it.lastIndex] }
            val originalImageUrl = originalImageUrlList.first {
                it.contains(resizedUrlLastSegments)
            }
            getBitmapFromUrl(context, originalImageUrl)
        }) {
            it.printStackTrace()
        }
    }

    fun setCover(imagePath: String, coverTitle: String) {
//        launchCatchError(dispatcher.io, block = {
//            val params = hashMapOf<String, RequestBody>()
//            params[PARAM_WEB_SERVICE] = RequestBody.create(MediaType.parse(TEXT_PLAIN), DEFAULT_WEB_SERVICE)
//            params[PARAM_RESOLUTION] = RequestBody.create(MediaType.parse(TEXT_PLAIN), RESOLUTION_700)
//            params[PARAM_ID] = RequestBody.create(MediaType.parse(TEXT_PLAIN),
//                    "${userSession.userId}${UUID.randomUUID()}${System.currentTimeMillis()}")
//
//            val dataUploadedImage = uploadImageUseCase
//                    .createObservable(uploadImageUseCase.createRequestParam(
//                            imagePath,
//                            DEFAULT_UPLOAD_PATH,
//                            DEFAULT_UPLOAD_TYPE,
//                            params))
//                    .toBlocking()
//                    .first()
//                    .dataResultImageUpload
//
//            var url = dataUploadedImage.data.picSrc
//            if (url.contains(DEFAULT_RESOLUTION)) {
//                url = url.replaceFirst(DEFAULT_RESOLUTION, RESOLUTION_700)
//            }
//            setupDataStore.setCover()
//        }) {
//            it.printStackTrace()
//        }
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

    private fun getBitmapFromUrl(context: Context, imageUrl: String) {
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        mutableOriginalImageUri.postValue(
                                coverImageUtil.getImagePathFromBitmap(resource)
                        )
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
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