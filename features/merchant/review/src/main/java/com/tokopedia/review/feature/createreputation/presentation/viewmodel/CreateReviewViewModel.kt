package com.tokopedia.review.feature.createreputation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.review.common.data.*
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.review.feature.createreputation.model.*
import com.tokopedia.review.feature.createreputation.domain.usecase.GetProductIncentiveOvo
import com.tokopedia.review.feature.createreputation.domain.usecase.GetProductReputationForm
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevEditReviewUseCase
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevSubmitReviewUseCase
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
                                                private val userSessionInterface: UserSessionInterface
) : BaseViewModel(coroutineDispatcherProvider.io) {

    companion object {
        const val CREATE_REVIEW_SOURCE_ID = "bjFkPX"
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

    fun submitReview(reputationId: Int, productId: Int, shopId: Int, reputationScore: Int, rating: Int,
                     reviewText: String, isAnonymous: Boolean, utmSource: String) {
        _submitReviewResult.postValue(LoadingView())
        if (imageData.isEmpty()) {
            sendReviewWithoutImage(reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous, utmSource)
        } else {
            sendReviewWithImage(reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous, getSelectedImagesUrl(), utmSource)
        }
    }

    fun editReview(feedbackId: Int, reputationId: Int, productId: Int, shopId: Int, reputationScore: Int, rating: Int,
                   reviewText: String, isAnonymous: Boolean) {
        _submitReviewResult.postValue(LoadingView())
        if (imageData.isEmpty()) {
            editReviewWithoutImage(feedbackId, reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous)
        } else {
            editReviewWithImage(feedbackId, reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous, getSelectedImagesUrl())
        }
    }

    fun getReviewDetails(feedbackId: Int) {
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

    fun getAfterEditImageList(imagePickerResult: ArrayList<String>, originalImageUrl: ArrayList<String>, edited: ArrayList<Boolean>): MutableList<BaseImageReviewUiModel> {
        val pictureList = originalImages.filter {
            originalImageUrl.contains(it)
        }.filterIndexed { index, _ -> !edited[index] }

        val imageUrlOrPathList = imagePickerResult.mapIndexed { index, urlOrPath ->
            if (edited[index]) urlOrPath else pictureList.find { it == originalImageUrl[index] }
                    ?: urlOrPath
        }.toMutableList()

        originalImages = originalImageUrl

        when (imagePickerResult.size) {
            5 -> {
                imageData = (imageUrlOrPathList.map {
                    ImageReviewUiModel(it)
                }).toMutableList()
            }
            else -> {
                imageData.addAll(imageUrlOrPathList.map {
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
            val imageToRemove = image as? ImageReviewUiModel
            imageToRemove?.let {
                if (originalImages.contains(it.fullImageUrl)) {
                    originalImages.remove(it.fullImageUrl)
                }
            }
        }
        if (imageData.size < 5 && !imageData.contains(DefaultImageReviewUiModel())) {
            imageData.add(DefaultImageReviewUiModel())
        }
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
            val imageUrl = if ((it as? ImageReviewUiModel)?.fullImageUrl?.isNotBlank() == true) {
                (it as? ImageReviewUiModel)?.fullImageUrl
            } else {
                (it as? ImageReviewUiModel)?.imageUrl
            }
            if (imageUrl?.isNotEmpty() == true) {
                result.add(imageUrl)
            }
        }
        return result
    }

    fun getProductReputation(productId: Int, reputationId: Int) {
        launchCatchError(block = {
            val data = withContext(coroutineDispatcherProvider.io) {
                getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(reputationId, productId))
            }
            reputationDataForm.postValue(CoroutineSuccess(data))
        }) {
            reputationDataForm.postValue(CoroutineFail(it))
        }
    }

    fun getProductIncentiveOvo(productId: Int = 0, reputationId: Int = 0) {
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

    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun isUserEligible(): Boolean {
        return (incentiveOvo.value as? com.tokopedia.usecase.coroutines.Success)?.data?.productrevIncentiveOvo != null
    }

    private fun sendReviewWithoutImage(reputationId: Int, productId: Int, shopId: Int, reputationScore: Int, rating: Int,
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

    private fun sendReviewWithImage(reputationId: Int, productId: Int, shopId: Int, reputationScore: Int, rating: Int,
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

    private fun editReviewWithoutImage(feedbackId: Int, reputationId: Int, productId: Int, shopId: Int, reputationScore: Int, rating: Int,
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

    private fun editReviewWithImage(feedbackId: Int, reputationId: Int, productId: Int, shopId: Int, reputationScore: Int, rating: Int,
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