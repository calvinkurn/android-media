package com.tokopedia.review.feature.createreputation.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.review.common.data.*
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.common.util.CoroutineDispatcherProvider
import com.tokopedia.review.feature.createreputation.model.*
import com.tokopedia.review.feature.createreputation.usecase.GetProductIncentiveOvo
import com.tokopedia.review.feature.createreputation.usecase.GetProductReputationForm
import com.tokopedia.review.feature.createreputation.usecase.ProductrevSubmitReviewUseCase
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.collections.ArrayList
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

class CreateReviewViewModel @Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
                                                private val getProductReputationForm: GetProductReputationForm,
                                                private val getProductIncentiveOvo: GetProductIncentiveOvo,
                                                private val getReviewDetailUseCase: ProductrevGetReviewDetailUseCase,
                                                private val submitReviewUseCase: ProductrevSubmitReviewUseCase,
                                                private val uploaderUseCase: UploaderUseCase
) : BaseViewModel(coroutineDispatcherProvider.io()) {

    companion object {
        private const val CREATE_REVIEW_SOURCE_ID = "bjFkPX"
    }

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private var imageData: MutableList<BaseImageReviewViewModel> = mutableListOf()

    private var reputationDataForm = MutableLiveData<Result<ProductRevGetForm>>()
    val getReputationDataForm = reputationDataForm

    private var _incentiveOvo = MutableLiveData<Result<ProductRevIncentiveOvoDomain>>()
    val incentiveOvo: LiveData<Result<ProductRevIncentiveOvoDomain>> = _incentiveOvo

    private val _reviewDetails = MutableLiveData<ReviewViewState<ProductrevGetReviewDetail>>()
    val reviewDetails: LiveData<ReviewViewState<ProductrevGetReviewDetail>>
        get() = _reviewDetails

    private val _submitReviewResult = MutableLiveData<Boolean>()
    val submitReviewResult: LiveData<Boolean>
        get() = _submitReviewResult

    fun submitReview(reputationId: Int, productId: Int, shopId: Int, reputationScore: Int, rating: Int,
                     reviewText: String, isAnonymous: Boolean) {
        if (imageData.isEmpty()) {
            sendReviewWithoutImage(reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous)
        } else {
            sendReviewWithImage(reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous, getSelectedImagesUrl())
        }
    }

    fun getReviewDetails(feedbackId: Int) {
        _reviewDetails.value = LoadingView()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io()) {
                getReviewDetailUseCase.setRequestParams(feedbackId)
                getReviewDetailUseCase.executeOnBackground()
            }
            _reviewDetails.postValue(Success(response.productrevGetReviewDetail))
        }) {
            _reviewDetails.postValue(Fail(it))
        }
    }

    fun getImageList(selectedImage: ArrayList<String>): MutableList<BaseImageReviewViewModel> {
        when (selectedImage.size) {
            5 -> {
                imageData = (selectedImage.map {
                    ImageReviewViewModel(it)
                }).toMutableList()
            }
            else -> {
                imageData.addAll(selectedImage.map {
                    ImageReviewViewModel(it)
                })
                imageData.add(DefaultImageReviewModel())
            }
        }
        return imageData
    }

    fun removeImage(image: BaseImageReviewViewModel) {
        imageData.remove(image)
    }

    fun clearImageData() {
        imageData.clear()
    }

    fun getSelectedImagesUrl(): ArrayList<String> {
        val result = arrayListOf<String>()
        imageData.forEach {
            val imageUrl = (it as? ImageReviewViewModel)?.imageUrl
            if(imageUrl?.isNotEmpty() == true) {
                result.add(imageUrl)
            }
        }
        return result
    }

    fun getProductReputation(productId: Int, reputationId: Int) {
        launchCatchError(block = {
            val data = withContext(coroutineDispatcherProvider.io()) {
                getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(reputationId, productId))
            }
            reputationDataForm.postValue(CoroutineSuccess(data))
        }) {
            reputationDataForm.postValue(CoroutineFail(it))
        }
    }

    fun getProductIncentiveOvo() {
        launchCatchError(block = {
            val data = withContext(coroutineDispatcherProvider.io()) {
                getProductIncentiveOvo.getIncentiveOvo()
            }
            _incentiveOvo.postValue(CoroutineSuccess(data))
        }) {
            _incentiveOvo.postValue(CoroutineFail(it))
        }
    }

    private fun sendReviewWithoutImage(reputationId: Int, productId: Int, shopId: Int, reputationScore: Int, rating: Int,
                                       reviewText: String, isAnonymous: Boolean) {
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io()) {
                submitReviewUseCase.setParams(reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous)
                submitReviewUseCase.executeOnBackground()
            }
            _submitReviewResult.postValue(response.productrevSubmitReview?.success ?: false)
        }) {
            _submitReviewResult.postValue(false)
        }
    }

    private fun sendReviewWithImage(reputationId: Int, productId: Int, shopId: Int, reputationScore: Int, rating: Int,
                                    reviewText: String, isAnonymous: Boolean, listOfImages: List<String>) {
        val uploadIdList: ArrayList<String> = ArrayList()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io()) {
                repeat(listOfImages.size) {
                    val imageId = uploadImageAndGetId(listOfImages[it])
                    if(imageId.isEmpty()) {
                        _submitReviewResult.postValue(false)
                        this@launchCatchError.cancel()
                    }
                    uploadIdList.add(imageId)
                }
                submitReviewUseCase.setParams(reputationId, productId, shopId, reputationScore, rating, reviewText, isAnonymous, uploadIdList)
                submitReviewUseCase.executeOnBackground()
            }
            _submitReviewResult.postValue(response.productrevSubmitReview?.success ?: false)
        }) {
            _submitReviewResult.postValue(false)
        }
    }

    private suspend fun uploadImageAndGetId(imagePath: String): String {
        val filePath = File(imagePath)
        val params = uploaderUseCase.createParams(
                sourceId = CREATE_REVIEW_SOURCE_ID,
                filePath = filePath
        )

        // check picture availability
        if (!filePath.exists()) {
            return ""
        }

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