package com.tokopedia.review.feature.reviewlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.reviewlist.domain.GetProductRatingOverallUseCase
import com.tokopedia.review.feature.reviewlist.domain.GetReviewProductListUseCase
import com.tokopedia.review.feature.reviewlist.util.mapper.SellerReviewProductListMapper
import com.tokopedia.review.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.review.feature.reviewlist.view.model.ProductRatingWrapperUiModel
import com.tokopedia.review.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SellerReviewListViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatchers,
        private val getProductRatingOverallUseCase: GetProductRatingOverallUseCase,
        private val getReviewProductListUseCase: GetReviewProductListUseCase
) : BaseViewModel(dispatcherProvider.main) {

    private val _reviewProductList = MutableLiveData<Result<Pair<Boolean, List<ProductReviewUiModel>>>>()
    val reviewProductList: LiveData<Result<Pair<Boolean, List<ProductReviewUiModel>>>>
        get() = _reviewProductList

    private val _productRatingOverall = MutableLiveData<Result<ProductRatingOverallUiModel>>()
    val productRatingOverall: LiveData<Result<ProductRatingOverallUiModel>>
        get() = _productRatingOverall

    fun getProductRatingData(sortBy: String, filterBy: String) {
        launchCatchError(block = {

            val productRatingResult = withContext(dispatcherProvider.io) {
                getRatingProduct(filterBy, sortBy)
            }

            val getProductRatingOverallSuccess = (productRatingResult.productRatingOverall as? Success)?.data
            val getProductRatingOverallFail = (productRatingResult.productRatingOverall as? Fail)?.throwable ?: Throwable()
            if (getProductRatingOverallSuccess != null) {
                val productRatingOverallMapper = SellerReviewProductListMapper.mapToProductRatingOverallModel(getProductRatingOverallSuccess)
                _productRatingOverall.postValue(Success(productRatingOverallMapper))
            } else {
                _productRatingOverall.postValue(Fail(getProductRatingOverallFail))
            }

            val getProductListSuccess = (productRatingResult.reviewProductList as? Success)?.data
            val getProductListFail = (productRatingResult.reviewProductList as? Fail)?.throwable ?: Throwable()
            if (getProductListSuccess != null) {
                val productListMapper = SellerReviewProductListMapper.mapToProductReviewListUiModel(getProductListSuccess)
                _reviewProductList.postValue(Success(Pair(getProductListSuccess.hasNext, productListMapper)))
            } else {
                _reviewProductList.postValue(Fail(getProductListFail))
            }
        }) {
            _productRatingOverall.postValue(Fail(it))
        }
    }

    fun getNextProductReviewList(sortBy: String, filterBy: String, page: Int) {
        launchCatchError(block = {
            val productReviewList = withContext(dispatcherProvider.io) {
                getProductReviewList(sortBy, filterBy, page)
            }
            _reviewProductList.postValue(Success(productReviewList))
        }, onError = {
            _reviewProductList.postValue(Fail(it))
        })
    }

    private suspend fun getRatingProduct(filterBy: String, sortBy: String, page: Int = 1): ProductRatingWrapperUiModel {
        getProductRatingOverallUseCase.requestParams = GetProductRatingOverallUseCase.createParams(
                filterBy,
                sortBy,
                ReviewConstants.DEFAULT_PER_PAGE,
                page)
        return getProductRatingOverallUseCase.executeOnBackground()
    }

    private suspend fun getProductReviewList(sortBy: String, filterBy: String, page: Int = 1): Pair<Boolean, List<ProductReviewUiModel>> {
        getReviewProductListUseCase.params = GetReviewProductListUseCase.createParams(
                sortBy,
                filterBy,
                ReviewConstants.DEFAULT_PER_PAGE,
                page
        )

        val productRatingListResponse = getReviewProductListUseCase.executeOnBackground()
        return Pair(
                productRatingListResponse.hasNext,
                SellerReviewProductListMapper.mapToProductReviewListUiModel(productRatingListResponse)
        )
    }


}