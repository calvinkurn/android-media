package com.tokopedia.review.feature.reviewlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.review.common.util.CoroutineDispatcherProvider
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.reviewlist.domain.GetProductRatingOverallUseCase
import com.tokopedia.review.feature.reviewlist.domain.GetReviewProductListUseCase
import com.tokopedia.review.feature.reviewlist.util.mapper.SellerReviewProductListMapper
import com.tokopedia.review.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.review.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SellerReviewListViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val getProductRatingOverallUseCase: GetProductRatingOverallUseCase,
        private val getReviewProductListUseCase: GetReviewProductListUseCase
) : BaseViewModel(dispatcherProvider.main()) {

    private val _reviewProductList = MutableLiveData<Result<Pair<Boolean, List<ProductReviewUiModel>>>>()
    val reviewProductList: LiveData<Result<Pair<Boolean, List<ProductReviewUiModel>>>>
        get() = _reviewProductList

    private val _productRatingOverall = MutableLiveData<Result<ProductRatingOverallUiModel>>()
    val productRatingOverall: LiveData<Result<ProductRatingOverallUiModel>>
        get() = _productRatingOverall

    fun getProductRatingData(sortBy: String, filterBy: String) {
        launchCatchError(block = {

            val productRatingOverall = asyncCatchError(dispatcherProvider.io(),
                    block = {
                        getProductRatingOverall(filterBy)
                    }, onError = {
                        _productRatingOverall.value = Fail(it)
                        null }
            )

            val reviewProductList = asyncCatchError(dispatcherProvider.io(),
                    block = {
                        getProductReviewList(
                                sortBy = sortBy,
                                filterBy = filterBy)
                    }, onError = {
                        _reviewProductList.value = Fail(it)
                        null
                    })

            productRatingOverall.await()?.let {
                reviewProductList.await()?.also { reviewProductData ->
                    _productRatingOverall.value = Success(it)
                    _reviewProductList.value = Success(reviewProductData)
                }
            }
        }) {
        }
    }

    fun getNextProductReviewList(sortBy: String, filterBy: String, page: Int) {
        launchCatchError(block = {
            val productReviewList = withContext(dispatcherProvider.io()) {
                getProductReviewList(sortBy, filterBy, page)
            }
            _reviewProductList.value = Success(productReviewList)
        }, onError = {
            _reviewProductList.value = Fail(it)
        })
    }

    private suspend fun getProductRatingOverall(filterBy: String): ProductRatingOverallUiModel {
        getProductRatingOverallUseCase.params = GetProductRatingOverallUseCase.createParams(filterBy)
        return SellerReviewProductListMapper.mapToProductRatingOverallModel(getProductRatingOverallUseCase.executeOnBackground())
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