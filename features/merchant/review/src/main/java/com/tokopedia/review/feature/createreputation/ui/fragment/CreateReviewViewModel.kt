package com.tokopedia.review.feature.createreputation.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.data.*
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.common.util.CoroutineDispatcherProvider
import com.tokopedia.review.feature.createreputation.model.*
import com.tokopedia.review.feature.createreputation.usecase.GetProductIncentiveOvo
import com.tokopedia.review.feature.createreputation.usecase.GetProductReputationForm
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSuccess

class CreateReviewViewModel @Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
                                                private val getProductReputationForm: GetProductReputationForm,
                                                private val getProductIncentiveOvo: GetProductIncentiveOvo,
                                                private val getReviewDetailUseCase: ProductrevGetReviewDetailUseCase
) : BaseViewModel(coroutineDispatcherProvider.io()) {

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

    fun submitReview(reviewId: String, reputationId: String, productId: String, shopId: String, reviewDesc: String,
                     ratingCount: Float, listOfImages: List<String>, isAnonymous: Boolean, utmSource: String) {

        if (listOfImages.isEmpty()) {
            sendReviewWithoutImage(reviewId, reputationId, productId, shopId, reviewDesc, ratingCount, isAnonymous, utmSource)
        } else {
            sendReviewWithImage(reviewId, reputationId, productId, shopId, reviewDesc, ratingCount, isAnonymous, listOfImages, utmSource)
        }
    }

    fun getReviewDetails(feedbackId: Int) {
        _reviewDetails.value = LoadingView()
        launchCatchError(block = {
            val response = withContext(Dispatchers.IO) {
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
                    ImageReviewViewModel(it, shouldDisplayOverlay = true)
                }).toMutableList()
            }
            else -> {
                imageData.addAll(selectedImage.map {
                    ImageReviewViewModel(it, shouldDisplayOverlay = false)
                })
                imageData.add(DefaultImageReviewModel())
            }
        }

        return imageData
    }

    fun initImageData(): MutableList<BaseImageReviewViewModel> {
        imageData.clear()
        return imageData
    }

    fun getProductReputation(productId: Int, reptutationId: Int) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                getProductReputationForm.getReputationForm(GetProductReputationForm.createRequestParam(reptutationId, productId))
            }
            reputationDataForm.postValue(CoroutineSuccess(data))
        }) {
            reputationDataForm.postValue(CoroutineFail(it))
        }
    }

    fun getProductIncentiveOvo() {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) { getProductIncentiveOvo.getIncentiveOvo() }
            _incentiveOvo.postValue(CoroutineSuccess(data))
        }) {
            _incentiveOvo.postValue(CoroutineFail(it))
        }
    }

    private fun sendReviewWithoutImage(reviewId: String, reputationId: String, productId: String, shopId: String,
                                       reviewDesc: String, ratingCount: Float, isAnonymous: Boolean, utmSource: String) {
    }

    private fun sendReviewWithImage(reviewId: String, reputationId: String, productId: String, shopId: String,
                                    reviewDesc: String, ratingCount: Float, isAnonymous: Boolean, listOfImages: List<String>, utmSource: String) {
    }
}