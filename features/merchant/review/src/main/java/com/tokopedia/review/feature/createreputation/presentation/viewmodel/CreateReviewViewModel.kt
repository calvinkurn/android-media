package com.tokopedia.review.feature.createreputation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.ProductrevGetReviewDetail
import com.tokopedia.review.common.data.ProductrevReviewAttachment
import com.tokopedia.review.common.data.ReviewViewState
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.domain.usecase.GetBadRatingCategoryUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.GetProductReputationForm
import com.tokopedia.review.feature.createreputation.domain.usecase.GetReviewTemplatesUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevEditReviewUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevGetPostSubmitBottomSheetUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevSubmitReviewUseCase
import com.tokopedia.review.feature.createreputation.model.BadRatingCategory
import com.tokopedia.review.feature.createreputation.model.BaseImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.DefaultImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ProductRevGetForm
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet
import com.tokopedia.review.feature.createreputation.presentation.mapper.CreateReviewImageMapper
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewProgressBarState
import com.tokopedia.review.feature.createreputation.presentation.uimodel.PostSubmitUiState
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.usecase.GetProductIncentiveOvo
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

class CreateReviewViewModel @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatchers,
    private val getProductReputationForm: GetProductReputationForm,
    private val getProductIncentiveOvo: GetProductIncentiveOvo,
    private val getReviewDetailUseCase: ProductrevGetReviewDetailUseCase,
    private val submitReviewUseCase: ProductrevSubmitReviewUseCase,
    private val uploaderUseCase: UploaderUseCase,
    private val editReviewUseCase: ProductrevEditReviewUseCase,
    private val userSessionInterface: UserSessionInterface,
    private val getReviewTemplatesUseCase: GetReviewTemplatesUseCase,
    private val getBadRatingCategoryUseCase: GetBadRatingCategoryUseCase,
    private val getPostSubmitBottomSheetUseCase: ProductrevGetPostSubmitBottomSheetUseCase,
) : BaseViewModel(coroutineDispatcherProvider.io) {

    companion object {
        const val CREATE_REVIEW_SOURCE_ID = "bjFkPX"
        const val LOCAL_IMAGE_SOURCE = "storage"
        const val MAX_IMAGE_COUNT = 5
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

    private val _submitButtonState = MutableLiveData(false)
    val submitButtonState: LiveData<Boolean>
        get() = _submitButtonState

    private val _progressBarState =
        MutableLiveData(CreateReviewProgressBarState())
    val progressBarState: LiveData<CreateReviewProgressBarState>
        get() = _progressBarState

    private var _badRatingCategories = MutableLiveData<Result<List<BadRatingCategory>>>()
    val badRatingCategories: LiveData<Result<List<BadRatingCategory>>>
        get() = _badRatingCategories

    private var _postSubmitUiState = MutableLiveData<PostSubmitUiState>()
    val postSubmitUiState: LiveData<PostSubmitUiState>
        get() = _postSubmitUiState

    private val selectedBadRatingCategories = mutableSetOf<String>()

    private fun mergeImagePickerResultWithOriginalImages(
        imagePickerResult: MutableList<String>,
        imagesFedIntoPicker: MutableList<String>
    ): List<String> {
        return imagePickerResult.mapIndexed { index, result ->
            if (result.endsWith(ReviewConstants.TEMP_IMAGE_EXTENSION)) {
                imagesFedIntoPicker[index]
            } else {
                result
            }
        }
    }

    fun submitReview(
        rating: Int,
        reviewText: String,
        reputationScore: Int,
        isAnonymous: Boolean,
        utmSource: String
    ) {
        (reputationDataForm.value as? CoroutineSuccess)?.data?.productrevGetForm?.let {
            _submitReviewResult.postValue(LoadingView())
            if (imageData.isEmpty()) {
                sendReviewWithoutImage(
                    it.reputationIDStr,
                    it.productData.productIDStr,
                    it.shopData.shopIDStr,
                    reputationScore,
                    rating,
                    reviewText,
                    isAnonymous,
                    utmSource
                )
            } else {
                sendReviewWithImage(
                    it.reputationIDStr,
                    it.productData.productIDStr,
                    it.shopData.shopIDStr,
                    reputationScore,
                    rating,
                    reviewText,
                    isAnonymous,
                    getSelectedImagesUrl(),
                    utmSource
                )
            }
        }
    }

    fun editReview(
        feedbackId: String,
        reputationId: String,
        productId: String,
        shopId: String,
        reputationScore: Int,
        rating: Int,
        reviewText: String,
        isAnonymous: Boolean
    ) {
        _submitReviewResult.postValue(LoadingView())
        if (imageData.isEmpty()) {
            editReviewWithoutImage(
                feedbackId,
                reputationId,
                productId,
                shopId,
                reputationScore,
                rating,
                reviewText,
                isAnonymous
            )
        } else {
            editReviewWithImage(
                feedbackId,
                reputationId,
                productId,
                shopId,
                reputationScore,
                rating,
                reviewText,
                isAnonymous,
                getSelectedImagesUrl()
            )
        }
    }

    fun getReviewDetails(feedbackId: String) {
        _reviewDetails.value = LoadingView()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                getReviewDetailUseCase.setRequestParams(feedbackId)
                getReviewDetailUseCase.executeOnBackground()
            }
            originalImages =
                response.productrevGetReviewDetail.review.attachments.map { it.fullSize }
                    .toMutableList()
            _reviewDetails.postValue(Success(response.productrevGetReviewDetail))
        }) {
            _reviewDetails.postValue(Fail(it))
        }
    }

    fun getAfterEditImageList(
        imagePickerResult: MutableList<String>,
        imagesFedIntoPicker: MutableList<String>
    ): MutableList<BaseImageReviewUiModel> {
        // Remove old image
        originalImages = imagesFedIntoPicker.filter { !it.contains(LOCAL_IMAGE_SOURCE) }.toMutableList()
        val mergedImagePaths = mergeImagePickerResultWithOriginalImages(imagePickerResult, imagesFedIntoPicker)
        when (mergedImagePaths.size) {
            MAX_IMAGE_COUNT -> {
                imageData = (mergedImagePaths.map {
                    ImageReviewUiModel(it)
                }).toMutableList()
            }
            else -> {
                imageData.addAll(mergedImagePaths.map {
                    ImageReviewUiModel(it)
                })
                imageData.add(DefaultImageReviewUiModel())
            }
        }
        return imageData
    }

    fun getImageList(selectedImage: List<ProductrevReviewAttachment>): MutableList<BaseImageReviewUiModel> {
        when (selectedImage.size) {
            MAX_IMAGE_COUNT -> {
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

    fun removeImage(
        image: BaseImageReviewUiModel,
        isEditMode: Boolean = false
    ): MutableList<BaseImageReviewUiModel> {
        imageData.remove(image)
        if (isEditMode) {
            originalImages = CreateReviewImageMapper.removeImageFromList(image, originalImages)
        }
        imageData = CreateReviewImageMapper.addDefaultModelIfLessThanFive(imageData)
        return imageData
    }

    fun isImageNotEmpty(): Boolean {
        return imageData.filterIsInstance<ImageReviewUiModel>().isNotEmpty()
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

    fun getProductReputation(productId: String, reputationId: String) {
        launchCatchError(block = {
            val data = withContext(coroutineDispatcherProvider.io) {
                getProductReputationForm.getReputationForm(
                    GetProductReputationForm.createRequestParam(
                        reputationId,
                        productId
                    )
                )
            }
            reputationDataForm.postValue(CoroutineSuccess(data))
        }) {
            reputationDataForm.postValue(CoroutineFail(it))
        }
    }

    fun getProductIncentiveOvo(productId: String = "", reputationId: String = "") {
        launchCatchError(block = {
            val data = withContext(coroutineDispatcherProvider.io) {
                getProductIncentiveOvo.getIncentiveOvo(productId, reputationId)
            }
            if (data?.productrevIncentiveOvo == null) {
                _incentiveOvo.postValue(null)
            } else {
                _incentiveOvo.postValue(CoroutineSuccess(data))
            }

        }) {
            _incentiveOvo.postValue(CoroutineFail(it))
        }
    }

    fun getReviewTemplates(productId: String) {
        launchCatchError(block = {
            val data = withContext(coroutineDispatcherProvider.io) {
                getReviewTemplatesUseCase.setParams(productId, 1)
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

    fun getUserId(): String {
        return userSessionInterface.userId
    }

    fun hasIncentive(): Boolean {
        return incentiveOvo.getSuccessData()?.let {
            it.productrevIncentiveOvo?.amount.isMoreThanZero()
        }.orFalse()
    }

    fun hasOngoingChallenge(): Boolean {
        return incentiveOvo.getSuccessData()?.let {
            it.productrevIncentiveOvo?.amount.isZero()
        }.orFalse()
    }

    fun isTemplateAvailable(): Boolean {
        return reviewTemplates.value.takeIfInstanceOf<CoroutineSuccess<List<String>>>()?.let {
            it.data.isNotEmpty()
        }.orFalse()
    }

    fun getImageCount(): Int {
        return imageData.count { it is ImageReviewUiModel }
    }

    fun getBadRatingCategories() {
        launchCatchError(block = {
            val data = withContext(coroutineDispatcherProvider.io) {
                getBadRatingCategoryUseCase.executeOnBackground()
            }
            _badRatingCategories.postValue(CoroutineSuccess(data.productrevGetBadRatingCategory.list))
        }) {
            _badRatingCategories.postValue(CoroutineFail(it))
        }
    }

    fun getPostSubmitBottomSheetData(reviewText: String, feedbackId: String) {
        launchCatchError(block = {
            val hasPendingIncentive = hasPendingIncentive()
            val data = withContext(coroutineDispatcherProvider.io) {
                getPostSubmitBottomSheetUseCase.setParams(
                    feedbackId = feedbackId,
                    reviewText = reviewText,
                    imagesTotal = getImageCount(),
                    isInboxEmpty = hasPendingIncentive.not(),
                    incentiveAmount = getIncentiveAmount()
                )
                getPostSubmitBottomSheetUseCase.executeOnBackground().productrevGetPostSubmitBottomSheetResponse
            }
            if (data == null) {
                _postSubmitUiState.postValue(PostSubmitUiState.ShowThankYouToaster(null))
            } else {
                if (data.isShowBottomSheet()) {
                    _postSubmitUiState.postValue(PostSubmitUiState.ShowThankYouBottomSheet(
                        data = data,
                        hasPendingIncentive = hasPendingIncentive
                    ))
                } else {
                    _postSubmitUiState.postValue(PostSubmitUiState.ShowThankYouToaster(data))
                }
            }
        }) {
            _postSubmitUiState.postValue(PostSubmitUiState.ShowThankYouToaster(null))
        }
    }

    fun addBadRatingCategory(badRatingCategoryId: String) {
        selectedBadRatingCategories.add(badRatingCategoryId)
        updateProgressBarFromBadRatingCategory()
    }

    fun removeBadRatingCategory(badRatingCategoryId: String) {
        selectedBadRatingCategories.remove(badRatingCategoryId)
        updateProgressBarFromBadRatingCategory()
    }

    fun isBadRatingReasonSelected(isTextAreaNotEmpty: Boolean): Boolean {
        return if (isOtherCategorySelected()) {
            isTextAreaNotEmpty
        } else {
            selectedBadRatingCategories.isNotEmpty()
        }
    }

    fun isOtherCategoryOnly(): Boolean {
        return selectedBadRatingCategories.size == 1 && selectedBadRatingCategories.first() == CreateReviewBottomSheet.BAD_RATING_OTHER_ID
    }

    private fun isOtherCategorySelected(): Boolean {
        return selectedBadRatingCategories.contains(CreateReviewBottomSheet.BAD_RATING_OTHER_ID)
    }

    private fun updateProgressBarFromBadRatingCategory() {
        _progressBarState.value = _progressBarState.value?.copy(isBadRatingReasonSelected = selectedBadRatingCategories.isNotEmpty())
    }

    private fun sendReviewWithoutImage(
        reputationId: String, productId: String, shopId: String, reputationScore: Int, rating: Int,
        reviewText: String, isAnonymous: Boolean, utmSource: String
    ) {
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                submitReviewUseCase.setParams(
                    reputationId = reputationId,
                    productId = productId,
                    shopId = shopId,
                    reputationScore = reputationScore,
                    rating = rating,
                    reviewText = reviewText,
                    isAnonymous = isAnonymous,
                    utmSource = utmSource,
                    badRatingCategoryIds = selectedBadRatingCategories.toList()
                )
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

    private fun sendReviewWithImage(
        reputationId: String, productId: String, shopId: String, reputationScore: Int, rating: Int,
        reviewText: String, isAnonymous: Boolean, listOfImages: List<String>, utmSource: String
    ) {
        val uploadIdList: ArrayList<String> = ArrayList()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                repeat(listOfImages.size) {
                    when (val uploadImageResult = uploadImage(listOfImages[it])) {
                        is UploadResult.Success -> {
                            uploadIdList.add(uploadImageResult.uploadId)
                        }
                        is UploadResult.Error -> {
                            _submitReviewResult.postValue(
                                Fail(
                                    MessageErrorException(
                                        uploadImageResult.message
                                    )
                                )
                            )
                            this@launchCatchError.cancel()
                        }
                    }
                }
                submitReviewUseCase.setParams(
                    reputationId,
                    productId,
                    shopId,
                    reputationScore,
                    rating,
                    reviewText,
                    isAnonymous,
                    uploadIdList,
                    utmSource,
                    selectedBadRatingCategories.toList()
                )
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

    private fun editReviewWithoutImage(
        feedbackId: String,
        reputationId: String,
        productId: String,
        shopId: String,
        reputationScore: Int,
        rating: Int,
        reviewText: String,
        isAnonymous: Boolean
    ) {
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                editReviewUseCase.setParams(
                    feedbackId,
                    reputationId,
                    productId,
                    shopId,
                    reputationScore,
                    rating,
                    reviewText,
                    isAnonymous
                )
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

    private fun editReviewWithImage(
        feedbackId: String,
        reputationId: String,
        productId: String,
        shopId: String,
        reputationScore: Int,
        rating: Int,
        reviewText: String,
        isAnonymous: Boolean,
        listOfImages: List<String>
    ) {
        val uploadIdList: ArrayList<String> = ArrayList()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                repeat(listOfImages.size) {
                    if (!originalImages.contains(listOfImages[it])) {
                        when (val uploadImageResult = uploadImage(listOfImages[it])) {
                            is UploadResult.Success -> {
                                uploadIdList.add(uploadImageResult.uploadId)
                            }
                            is UploadResult.Error -> {
                                _editReviewResult.postValue(
                                    Fail(
                                        MessageErrorException(
                                            uploadImageResult.message
                                        )
                                    )
                                )
                                this@launchCatchError.cancel()
                            }
                        }
                    }
                }
                editReviewUseCase.setParams(
                    feedbackId,
                    reputationId,
                    productId,
                    shopId,
                    reputationScore,
                    rating,
                    reviewText,
                    isAnonymous,
                    originalImages,
                    uploadIdList
                )
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

    private suspend fun uploadImage(imagePath: String): UploadResult {
        val filePath = File(imagePath)
        val params = uploaderUseCase.createParams(
            sourceId = CREATE_REVIEW_SOURCE_ID,
            filePath = filePath
        )
        return uploaderUseCase(params)
    }

    private fun getIncentiveAmount(): Int {
        return incentiveOvo.getSuccessData()?.productrevIncentiveOvo?.amount.orZero()
    }

    private suspend fun hasPendingIncentive(): Boolean {
        return try {
            withContext(coroutineDispatcherProvider.io) {
                getProductIncentiveOvo.getIncentiveOvo(
                    productId = "",
                    reputationId = ""
                )?.productrevIncentiveOvo != null
            }
        } catch (_: Exception) {
            false
        }
    }

    private inline fun <reified T: Any> Any?.takeIfInstanceOf(): T? {
        return if (this is T) this else null
    }

    private inline fun <reified T: Any> LiveData<Result<T>?>.getSuccessData(): T? {
        return value.takeIfInstanceOf<CoroutineSuccess<T>>()?.data
    }
}