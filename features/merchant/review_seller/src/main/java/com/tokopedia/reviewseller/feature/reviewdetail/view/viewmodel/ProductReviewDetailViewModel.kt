package com.tokopedia.reviewseller.feature.reviewdetail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant
import com.tokopedia.reviewseller.feature.reviewdetail.domain.GetProductFeedbackDetailListUseCase
import com.tokopedia.reviewseller.feature.reviewdetail.domain.GetProductReviewDetailOverallUseCase
import com.tokopedia.reviewseller.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.OverallRatingDetailUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.ProductFeedbackDetailUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductReviewDetailViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val userSession: UserSessionInterface,
        private val getProductReviewDetailOverallUseCase: GetProductReviewDetailOverallUseCase,
        private val getProductFeedbackDetailListUseCase: GetProductFeedbackDetailListUseCase
) : BaseViewModel(dispatcherProvider.main()) {
    
    private val _reviewDetailOverall = MutableLiveData<Result<OverallRatingDetailUiModel>>()
    val reviewDetailOverallRating: LiveData<Result<OverallRatingDetailUiModel>>
        get() = _reviewDetailOverall

    private val _productFeedbackDetail =
            MutableLiveData<Result<Pair<Boolean, ProductFeedbackDetailUiModel>>>()
    val productFeedbackDetail:
            LiveData<Result<Pair<Boolean, ProductFeedbackDetailUiModel>>>
        get() = _productFeedbackDetail

    fun getProductRatingDetail(productID: Int, sortBy: String, filterBy: String) {
        launchCatchError(block = {

            val reviewDetailOverallResponse = asyncCatchError(
                    dispatcherProvider.io(),
                    block = {
                        getReviewDetailOverall(productID, filterBy)
                    },
                    onError = {
                        _reviewDetailOverall.postValue(Fail(it))
                        null
                    }
            )

            val feedbackDetailResponse = asyncCatchError(
                    dispatcherProvider.io(),
                    block = {
                        getProductFeedbackDetailList(productID, sortBy, filterBy)
                    },
                    onError = {
                        _productFeedbackDetail.postValue(Fail(it))
                        null
                    }
            )

            reviewDetailOverallResponse.await()?.let {
                _reviewDetailOverall.postValue(Success(it))
                feedbackDetailResponse.await()?.also { feedbackResponse ->
                    feedbackResponse.second.topicList.countFeedback = feedbackResponse.second.productFeedbackDetailList.size
                    _productFeedbackDetail.postValue(Success(feedbackResponse))
                }
            }
        }) {
        }
    }

    fun getFeedbackDetailListNext(productID: Int, sortBy: String, filterBy: String, page: Int) {
        launchCatchError(block = {
            val feedbackDetailList = withContext(dispatcherProvider.io()) {
                getProductFeedbackDetailList(productID, sortBy, filterBy, page)
            }
            _productFeedbackDetail.postValue(Success(feedbackDetailList))
        }, onError = {
            _productFeedbackDetail.postValue(Fail(it))
        })
    }

    private suspend fun getProductFeedbackDetailList(productID: Int, sortBy: String, filterBy: String, page: Int = 1):
            Pair<Boolean, ProductFeedbackDetailUiModel> {
        getProductFeedbackDetailListUseCase.params = GetProductFeedbackDetailListUseCase.createParams(
                productID,
                sortBy,
                filterBy,
                ReviewSellerConstant.DEFAULT_PER_PAGE,
                page
        )

        val productFeedbackDetailResponse = getProductFeedbackDetailListUseCase.executeOnBackground()
        return Pair(
                productFeedbackDetailResponse.hasNext,
                SellerReviewProductDetailMapper.mapToProductFeedbackDetailUiModel(productFeedbackDetailResponse, userSession))
    }

    private suspend fun getReviewDetailOverall(productID: Int, filterBy: String): OverallRatingDetailUiModel {
        getProductReviewDetailOverallUseCase.params = GetProductReviewDetailOverallUseCase.createParams(productID, filterBy)
        return SellerReviewProductDetailMapper.mapToRatingDetailOverallUiModel(getProductReviewDetailOverallUseCase.executeOnBackground())
    }

}