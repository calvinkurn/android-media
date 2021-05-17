package com.tokopedia.review.feature.createreputation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.review.common.data.*
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.*
import com.tokopedia.review.feature.createreputation.model.BaseImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.DefaultImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ProductRevGetForm
import com.tokopedia.review.feature.createreputation.presentation.mapper.CreateReviewImageMapper
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewProgressBarState
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

class CreateReviewViewModel @Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatchers,
                                                private val getProductReputationForm: GetProductReputationForm,
                                                private val getProductIncentiveOvo: GetProductIncentiveOvo,
                                                private val getReviewDetailUseCase: ProductrevGetReviewDetailUseCase,
                                                private val submitReviewUseCase: ProductrevSubmitReviewUseCase,
                                                private val uploaderUseCase: UploaderUseCase,
                                                private val editReviewUseCase: ProductrevEditReviewUseCase,
                                                private val userSessionInterface: UserSessionInterface,
                                                private val getReviewTemplatesUseCase: GetReviewTemplatesUseCase
) : BaseViewModel(coroutineDispatcherProvider.io) {

    companion object {
        const val CREATE_REVIEW_SOURCE_ID = "bjFkPX"
        const val LOCAL_IMAGE_SOURCE = "storage"
    }

    private var imageData: MutableList<BaseImageReviewUiModel> = mutableListOf()
    private var originalImages: MutableList<String> = mutableListOf()

    private var reputationDataForm = MutableLiveData<Result<ProductRevGetForm>>()
    val getReputationDataForm: LiveData<Result<ProductRevGetForm>>
        get() = reputationDataForm

    private var _incentiveOvo = MutableLiveData<Result<ProductRevIncentiveOvoDomain>?>()
    val incentiveOvo: LiveData<Result<ProductRevIncentiveOvoDomain>?> = _incentiveOvo

    private val _reviewDetails = MutableLiveData<ReviewViewState<ProductrevGetReviewDetail>>()
    val reviewDetails: LiveData<ReviewViewState<ProductrevGetReviewDetail>>
        get() = _reviewDetails

    private val _submitReviewResult = MutableLiveData<ReviewViewState<String>>()
    val submitReviewResult: LiveData<ReviewViewState<String>>
        get() = _submitReviewResult

    private val _editReviewResult = MutableLiveData<ReviewViewState<Boolean>>()
    val editReviewResult: LiveData<ReviewViewState<Boolean>>
        get() = _editReviewResult

    private val _reviewTemplates = MutableLiveData<Result<List<String>>>()
    val reviewTemplates: LiveData<Result<List<String>>>
        get() = _reviewTemplates

    private val _submitButtonState = MutableLiveData<Boolean>(false)
    val submitButtonState: LiveData<Boolean>
        get() = _submitButtonState

    private val _progressBarState = MutableLiveData<CreateReviewProgressBarState>(CreateReviewProgressBarState())
    val progressBarState: LiveData<CreateReviewProgressBarState>
        get() =  _progressBarState

    fun submitReview(reputationId: Long, productId: Long, shopId: Long, reputationScore: Int, rating: Int,
                     reviewText: String, isAnonymous: Boolean, utmSource: String) {
        _submitReviewResult.postValue(LoadingView())
        if (imageData.isEmpty()) {
            sendReviewWithoutImage(reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous, utmSource)
        } else {
            sendReviewWithImage(reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous, getSelectedImagesUrl(), utmSource)
        }
    }

    fun submitReview(rating: Int, reviewText: String, isAnonymous: Boolean, utmSource: String) {
        (reputationDataForm.value as? CoroutineSuccess)?.data?.productrevGetForm?.let {
            _submitReviewResult.postValue(LoadingView())
            if (imageData.isEmpty()) {
                sendReviewWithoutImage(reputationId = it.reputationID, productId = it.productData.productID, shopId = it.shopData.shopID, rating = rating, reviewText = reviewText, isAnonymous = isAnonymous, utmSource = utmSource)
            } else {
                sendReviewWithImage(reputationId = it.reputationID, productId = it.productData.productID, shopId = it.shopData.shopID, rating = rating, reviewText = reviewText, isAnonymous = isAnonymous, listOfImages = getSelectedImagesUrl(), utmSource = utmSource)
            }
        }
    }

    fun editReview(feedbackId: Long, reputationId: Long, productId: Long, shopId: Long, reputationScore: Int, rating: Int,
                   reviewText: String, isAnonymous: Boolean) {
        _submitReviewResult.postValue(LoadingView())
        if (imageData.isEmpty()) {
            editReviewWithoutImage(feedbackId, reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous)
        } else {
            editReviewWithImage(feedbackId, reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous, getSelectedImagesUrl())
        }
    }

    fun getReviewDetails(feedbackId: Long) {
        _reviewDetails.value = LoadingView()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                getReviewDetailUseCase.setRequestParams(feedbackId)
                getReviewDetailUseCase.executeOnBackground()
            }
            originalImages = response.productrevGetReviewDetail.review.attachments.map { it.fullSize }.toMutableList()
            _reviewDetails.postValue(Success(response.productrevGetReviewDetail))
        }) {
            _reviewDetails.postValue(Fail(it))
        }
    }

    fun getAfterEditImageList(imagePickerResult: MutableList<String>, imagesFedIntoPicker: MutableList<String>): MutableList<BaseImageReviewUiModel> {
        // Remove old image
        originalImages = imagesFedIntoPicker.filter { !it.contains(LOCAL_IMAGE_SOURCE) }.toMutableList()
        val imagesToDisplay = originalImages.toMutableList()
        imagePickerResult.forEachIndexed { index, s ->
            if(s.contains(LOCAL_IMAGE_SOURCE) && index > originalImages.lastIndex) {
                imagesToDisplay.add(s)
            }
        }

        when (imagesToDisplay.size) {
            5 -> {
                imageData = (imagesToDisplay.map {
                    ImageReviewUiModel(it)
                }).toMutableList()
            }
            else -> {
                imageData.addAll(imagesToDisplay.map {
                    ImageReviewUiModel(it)
                })
                imageData.add(DefaultImageReviewUiModel())
            }
        }
        return imageData
    }

    fun getImageList(selectedImage: List<ProductrevReviewAttachment>): MutableList<BaseImageReviewUiModel> {
        when (selectedImage.size) {
            5 -> {
                imageData = (selectedImage.map {
                    ImageReviewUiModel(it.thumbnail, it.fullSize)
                }).toMutableList()
            }
            else -> {
                imageData.addAll(selectedImage.map {
                    ImageReviewUiModel(it.thumbnail, it.fullSize)
                })
                imageData.add(DefaultImageReviewUiModel())
            }
        }
        return imageData
    }

    fun removeImage(image: BaseImageReviewUiModel, isEditMode: Boolean = false): MutableList<BaseImageReviewUiModel> {
        imageData.remove(image)
        if (isEditMode) {
            originalImages = CreateReviewImageMapper.removeImageFromList(image, originalImages)
        }
        imageData = CreateReviewImageMapper.addDefaultModelIfLessThanFive(imageData)
        return imageData
    }

    fun isImageNotEmpty(): Boolean {
        return imageData.isNotEmpty()
    }

    fun clearImageData() {
        imageData.clear()
    }

    fun getSelectedImagesUrl(): ArrayList<String> {
        val result = arrayListOf<String>()
        imageData.forEach {
            val imageUrl = CreateReviewImageMapper.getImageUrl(it)
            if (imageUrl.isNotEmpty()) {
                result.add(imageUrl)
            }
        }
        return result
    }

    fun getProductReputation(productId: Long, reputationId: Long) {
        launchCatchError(block = {
            val data = withContext(coroutineDispatcherProvider.io) {
                getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(reputationId, productId))
            }
            reputationDataForm.postValue(CoroutineSuccess(data))
        }) {
            reputationDataForm.postValue(CoroutineFail(it))
        }
    }

    fun getProductIncentiveOvo(productId: Long = 0, reputationId: Long = 0) {
        launchCatchError(block = {
            val data = withContext(coroutineDispatcherProvider.io) {
                getProductIncentiveOvo.getIncentiveOvo(productId, reputationId)
            }
            if (data == null) {
                _incentiveOvo.postValue(null)
            } else {
                _incentiveOvo.postValue(CoroutineSuccess(data))
            }

        }) {
            _incentiveOvo.postValue(CoroutineFail(it))
        }
    }

    fun getReviewTemplates(productId: Long) {
        launchCatchError(block = {
            val data = withContext(coroutineDispatcherProvider.io) {
                getReviewTemplatesUseCase.setParams(productId.toString(), 1)
                getReviewTemplatesUseCase.executeOnBackground()
            }
            _reviewTemplates.postValue(CoroutineSuccess(data.productrevGetPersonalizedReviewTemplate.templates))
        }) {
            _reviewTemplates.postValue(CoroutineFail(it))
        }
    }

    fun updateButtonState(isEnabled: Boolean) {
        _submitButtonState.value = isEnabled
    }

    fun updateProgressBarFromRating(isGoodRating: Boolean) {
        _progressBarState.value = _progressBarState.value?.copy(isGoodRating = isGoodRating)
    }

    fun updateProgressBarFromPhotos() {
        _progressBarState.value = _progressBarState.value?.copy(isPhotosFilled = isImageNotEmpty())
    }

    fun updateProgressBarFromTextArea(isNotEmpty: Boolean) {
        _progressBarState.value = _progressBarState.value?.copy(isTextAreaFilled = isNotEmpty)
    }

    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun isUserEligible(): Boolean {
        return (incentiveOvo.value as? com.tokopedia.usecase.coroutines.Success)?.data?.productrevIncentiveOvo != null
    }

    private fun sendReviewWithoutImage(reputationId: Long, productId: Long, shopId: Long, reputationScore: Int = 0, rating: Int,
                                       reviewText: String, isAnonymous: Boolean, utmSource: String) {
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                submitReviewUseCase.setParams(reputationId = reputationId, productId =
                productId, shopId = shopId, reputationScore = reputationScore, rating = rating, reviewText = reviewText, isAnonymous = isAnonymous, utmSource = utmSource)
                submitReviewUseCase.executeOnBackground()
            }
            if (response.productrevSuccessIndicator != null) {
                if (response.productrevSuccessIndicator.success) {
                    _submitReviewResult.postValue(Success(response.productrevSuccessIndicator.feedbackID))
                } else {
                    _submitReviewResult.postValue(Fail(Throwable()))
                }
            }
        }) {
            _submitReviewResult.postValue(Fail(it))
        }
    }

    private fun sendReviewWithImage(reputationId: Long, productId: Long, shopId: Long, reputationScore: Int = 0, rating: Int,
                                    reviewText: String, isAnonymous: Boolean, listOfImages: List<String>, utmSource: String) {
        val uploadIdList: ArrayList<String> = ArrayList()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                repeat(listOfImages.size) {
                    val imageId = uploadImageAndGetId(listOfImages[it])
                    if (imageId.isEmpty()) {
                        _submitReviewResult.postValue(Fail(Throwable()))
                        this@launchCatchError.cancel()
                    }
                    uploadIdList.add(imageId)
                }
                submitReviewUseCase.setParams(reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous, uploadIdList, utmSource)
                submitReviewUseCase.executeOnBackground()
            }
            if (response.productrevSuccessIndicator != null) {
                if (response.productrevSuccessIndicator.success) {
                    _submitReviewResult.postValue(Success(response.productrevSuccessIndicator.feedbackID))
                } else {
                    _submitReviewResult.postValue(Fail(Throwable()))
                }
            }
        }) {
            _submitReviewResult.postValue(Fail(it))
        }
    }

    private fun editReviewWithoutImage(feedbackId: Long, reputationId: Long, productId: Long, shopId: Long, reputationScore: Int, rating: Int,
                                       reviewText: String, isAnonymous: Boolean) {
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                editReviewUseCase.setParams(feedbackId, reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous)
                editReviewUseCase.executeOnBackground()
            }
            if (response.productrevSuccessIndicator != null) {
                if (response.productrevSuccessIndicator.success) {
                    _editReviewResult.postValue(Success(response.productrevSuccessIndicator.success))
                } else {
                    _editReviewResult.postValue(Fail(Throwable()))
                }
            }
        }) {
            _editReviewResult.postValue(Fail(it))
        }
    }

    private fun editReviewWithImage(feedbackId: Long, reputationId: Long, productId: Long, shopId: Long, reputationScore: Int, rating: Int,
                                    reviewText: String, isAnonymous: Boolean, listOfImages: List<String>) {
        val uploadIdList: ArrayList<String> = ArrayList()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                repeat(listOfImages.size) {
                    if (!originalImages.contains(listOfImages[it])) {
                        val imageId = uploadImageAndGetId(listOfImages[it])
                        if (imageId.isEmpty()) {
                            _submitReviewResult.postValue(Fail(Throwable()))
                            this@launchCatchError.cancel()
                        }
                        uploadIdList.add(imageId)
                    }
                }
                editReviewUseCase.setParams(feedbackId, reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous, originalImages, uploadIdList)
                editReviewUseCase.executeOnBackground()
            }
            if (response.productrevSuccessIndicator != null) {
                if (response.productrevSuccessIndicator.success) {
                    _editReviewResult.postValue(Success(response.productrevSuccessIndicator.success))
                } else {
                    _editReviewResult.postValue(Fail(Throwable()))
                }
            }
        }) {
            _editReviewResult.postValue(Fail(it))
        }
    }

    private suspend fun uploadImageAndGetId(imagePath: String): String {
        val filePath = File(imagePath)
        val params = uploaderUseCase.createParams(
                sourceId = CREATE_REVIEW_SOURCE_ID,
                filePath = filePath
        )

        return when (val result = uploaderUseCase(params)) {
            is UploadResult.Success -> {
                result.uploadId
            }
            is UploadResult.Error -> {
                ""
            }
        }
    }
}