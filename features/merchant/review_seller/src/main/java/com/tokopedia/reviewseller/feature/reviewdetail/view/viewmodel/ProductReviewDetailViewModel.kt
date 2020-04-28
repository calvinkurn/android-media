package com.tokopedia.reviewseller.feature.reviewdetail.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant
import com.tokopedia.reviewseller.common.util.ReviewSellerUtil
import com.tokopedia.reviewseller.common.util.getKeyByValue
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.reviewseller.feature.reviewdetail.domain.GetProductFeedbackDetailListUseCase
import com.tokopedia.reviewseller.feature.reviewdetail.domain.GetProductReviewDetailOverallUseCase
import com.tokopedia.reviewseller.feature.reviewdetail.domain.GetProductReviewInitialUseCase
import com.tokopedia.reviewseller.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.OverallRatingDetailUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.ProductFeedbackDetailUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.ProductFeedbackErrorUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductReviewDetailViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val userSession: UserSessionInterface,
        private val getProductReviewInitialUseCase: GetProductReviewInitialUseCase,
        private val getProductReviewDetailOverallUseCase: GetProductReviewDetailOverallUseCase,
        private val getProductFeedbackDetailListUseCase: GetProductFeedbackDetailListUseCase
) : BaseViewModel(dispatcherProvider.main()) {

    var positionFilterPeriod = 1
    var filterPeriod: String = ""
    var filterAllText: String = ""
    private var chipsFilterText = "30 Hari Terakhir"
    private var productId = 0

    fun setChipFilterText(text:String) {
        _chipFilterText.value = text
    }
    private val _chipFilterText = MutableLiveData<String>()

    private val _ratingFilterData = MediatorLiveData<Result<Pair<List<BaseSellerReviewDetail>,String>>>()
    val ratingFilterData: LiveData<Result<Pair<List<BaseSellerReviewDetail>,String>>>
        get() = _ratingFilterData

    private val _reviewDetailOverall = MutableLiveData<Result<OverallRatingDetailUiModel>>()
    val reviewDetailOverallRating: LiveData<Result<OverallRatingDetailUiModel>>
        get() = _reviewDetailOverall

    private val _productFeedbackDetail = MutableLiveData<Result<Pair<Boolean, ProductFeedbackDetailUiModel>>>()
    val productFeedbackDetail: LiveData<Result<Pair<Boolean, ProductFeedbackDetailUiModel>>>
        get() = _productFeedbackDetail

    init {
        _ratingFilterData.addSource(_chipFilterText){
            chipsFilterText = it
            filterPeriod = ReviewSellerConstant.mapFilterReviewDetail().getKeyByValue(it)
            filterAllText = ReviewSellerUtil.setFilterJoinValueFormat(filterPeriod)

            getProductRatingDetail(productId,"")
        }
    }

    fun getProductRatingDetail(productID: Int, sortBy: String) {
        launchCatchError(block = {
            productId = productID
            getProductReviewInitialUseCase.requestParams = GetProductReviewInitialUseCase.createParams(productID, filterAllText, sortBy, 1)
            val data = getProductReviewInitialUseCase.executeOnBackground()
            val productName = data.productReviewDetailOverallResponse?.productGetReviewAggregateByProduct?.productName ?: ""
            val uiData: MutableList<BaseSellerReviewDetail> = mutableListOf()

            uiData.add(SellerReviewProductDetailMapper.mapToRatingDetailOverallUiModel(data.productReviewDetailOverallResponse?.productGetReviewAggregateByProduct ?: ProductReviewDetailOverallResponse.ProductGetReviewAggregateByProduct(), chipsFilterText))
            uiData.add(SellerReviewProductDetailMapper.mapToRatingBarUiModel(data.productFeedBackResponse?.productrevFeedbackDataPerProduct ?: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct()))
            uiData.add(SellerReviewProductDetailMapper.mapToTopicUiModel(data.productFeedBackResponse?.productrevFeedbackDataPerProduct ?: ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct()))

            if (data.productFeedBackResponse == null) {
                _ratingFilterData.postValue(Fail(Throwable()))
            } else {
                if(data.productFeedBackResponse?.productrevFeedbackDataPerProduct?.list?.isEmpty() == true || data.productFeedBackResponse?.productrevFeedbackDataPerProduct?.list == null) {
                    uiData.add(ProductFeedbackErrorUiModel(false))
                } else {
                    uiData.addAll(SellerReviewProductDetailMapper.mapToFeedbackUiModel(data.productFeedBackResponse!!.productrevFeedbackDataPerProduct,userSession))
                }
            }

            _ratingFilterData.postValue(Success(uiData to productName))
        }) {
            _ratingFilterData.postValue(Fail(it))
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
}