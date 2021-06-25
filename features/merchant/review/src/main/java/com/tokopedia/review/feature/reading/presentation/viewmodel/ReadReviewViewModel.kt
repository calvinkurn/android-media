package com.tokopedia.review.feature.reading.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductReviewList
import com.tokopedia.review.feature.reading.domain.usecase.GetProductRatingAndTopicsUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetProductReviewListUseCase
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.uimodel.FilterType
import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterBottomSheetType
import com.tokopedia.review.feature.reading.presentation.uimodel.SortTypeConstants
import com.tokopedia.review.feature.reading.presentation.uimodel.ToggleLikeUiModel
import com.tokopedia.review.feature.reading.utils.ReadReviewUtils
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReadReviewViewModel @Inject constructor(
        private val getProductRatingAndTopicsUseCase: GetProductRatingAndTopicsUseCase,
        private val getProductReviewListUseCase: GetProductReviewListUseCase,
        private val toggleLikeReviewUseCase: ToggleLikeReviewUseCase,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        const val INITIAL_PAGE = 1
    }

    private val _ratingAndTopics = MediatorLiveData<Result<ProductrevGetProductRatingAndTopic>>()
    val ratingAndTopic: LiveData<Result<ProductrevGetProductRatingAndTopic>>
        get() = _ratingAndTopics

    private val _productReviews = MediatorLiveData<Result<ProductrevGetProductReviewList>>()
    val productReviews: LiveData<Result<ProductrevGetProductReviewList>>
        get() = _productReviews

    private val _toggleLikeReview = MutableLiveData<Result<ToggleLikeUiModel>>()
    val toggleLikeReview: LiveData<Result<ToggleLikeUiModel>>
        get() = _toggleLikeReview

    private val currentPage = MutableLiveData<Int>()
    private var productId: MutableLiveData<String> = MutableLiveData()
    private var sort: String = SortTypeConstants.MOST_HELPFUL_PARAM
    private var filter: MutableList<FilterType> = mutableListOf()

    init {
        _ratingAndTopics.addSource(productId) {
            getRatingAndTopics(it)
        }
        _productReviews.addSource(currentPage) {
            getProductReviews(it)
        }
    }

    fun setProductId(productId: String) {
        this.productId.value = productId
    }

    fun getProductId(): String {
        return this.productId.value ?: ""
    }

    fun getReviewStatistics(): List<ProductReviewDetail> {
        return (ratingAndTopic.value as? Success)?.data?.rating?.detail ?: listOf()
    }

    fun getReviewSatisfactionRate(): String {
        return (ratingAndTopic.value as? Success)?.data?.rating?.satisfactionRate ?: ""
    }

    fun setPage(page: Int) {
        currentPage.value = page
    }

    fun mapProductReviewToReadReviewUiModel(productReviews: List<ProductReview>, shopId: String, shopName: String): List<ReadReviewUiModel> {
        return productReviews.map {
            ReadReviewUiModel(it, false, shopId, shopName)
        }
    }

    fun toggleLikeReview(reviewId: String, shopId: String, likeStatus: Int, index: Int) {
        launchCatchError(block = {
            toggleLikeReviewUseCase.setParams(reviewId, shopId, productId.value
                    ?: "", ReadReviewUtils.invertLikeStatus(likeStatus))
            val data = toggleLikeReviewUseCase.executeOnBackground()
            _toggleLikeReview.postValue(Success(ToggleLikeUiModel(data.toggleProductReviewLike.likeStatus, data.toggleProductReviewLike.totalLike, index)))
        }) {
            _toggleLikeReview.postValue(Fail(it))
        }
    }

    fun setFilter(selectedFilters: List<ListItemUnify>, type: SortFilterBottomSheetType) {
        if (type == SortFilterBottomSheetType.RatingFilterBottomSheet) {
            if (selectedFilters.isEmpty()) {
                this.filter.removeBasedOnClass(FilterType.FilterRating())
            } else {
                this.filter.replace(mapRatingFilterToFilterType(selectedFilters))
            }
        } else {
            if (selectedFilters.isEmpty()) {
                this.filter.removeBasedOnClass(FilterType.FilterTopic())
            } else {
                this.filter.replace(mapTopicFilterToFilterType(selectedFilters))
            }
        }
        resetPage()
    }

    fun setFilterWithImage(isActive: Boolean) {
        if (isActive) {
            this.filter.forEach {
                if (it is FilterType.FilterWithImage) {
                    filter.remove(it)
                    return@forEach
                }
            }
        } else {
            this.filter.add(getFilterWithImageParam())
        }
        resetPage()
    }

    fun setSort(selectedSort: String) {
        this.sort = mapSortOptionToSortParam(selectedSort)
        resetPage()
    }

    fun getSelectedRatingFilter(): List<String> {
        val selectedFilters = filter.getBasedOnClass(FilterType.FilterRating())?.value
        return selectedFilters?.split(",")?.map { it.trim() } ?: listOf()
    }

    fun getSelectedTopicFilter(): List<String> {
        val selectedFilters = filter.getBasedOnClass(FilterType.FilterTopic())?.value?.split(",")?.map { it.trim() } ?: listOf()
        val topicsMap = getTopicsMap()
        val result = mutableListOf<String>()
        selectedFilters.forEach {
            result.add(getKey(topicsMap, it))
        }
        return result
    }

    fun clearFilters() {
        this.filter = mutableListOf()
    }

    fun resetToDefaultSort() {
        this.sort = SortTypeConstants.MOST_HELPFUL_PARAM
        resetPage()
    }

    private fun getKey(map: Map<String, String>, target: String): String {
        map.keys.forEach {
            if (target == map[it]) {
                return it
            }
        }
        return ""
    }

    private fun getProductReviews(page: Int) {
        launchCatchError(block = {
            getProductReviewListUseCase.setParams(productId.value
                    ?: "", page, sort, mapFilterToRequestParams())
            val data = getProductReviewListUseCase.executeOnBackground()
            _productReviews.postValue(Success(data.productrevGetProductReviewList))
        }) {
            _productReviews.postValue(Fail(it))
        }
    }

    private fun getRatingAndTopics(productId: String) {
        launchCatchError(block = {
            getProductRatingAndTopicsUseCase.setParams(productId)
            val data = getProductRatingAndTopicsUseCase.executeOnBackground()
            _ratingAndTopics.postValue(Success(data.productrevGetProductRatingAndTopics))
        }) {
            _ratingAndTopics.postValue(Fail(it))
        }
    }

    private fun resetPage() {
        setPage(INITIAL_PAGE)
    }

    private fun mapSortOptionToSortParam(sort: String): String {
        return SortTypeConstants.sortMap[sort] ?: SortTypeConstants.MOST_HELPFUL_PARAM
    }

    private fun getFilterWithImageParam(): FilterType {
        return FilterType.FilterWithImage()
    }

    private fun mapFilterToRequestParams(): String {
        if (filter.isEmpty()) return ""
        return filter.joinToString(separator = ";") { "${it.param}=${it.value}" }
    }

    private fun mapRatingFilterToFilterType(ratingFilters: List<ListItemUnify>): FilterType.FilterRating {
        val selectedRatings = ratingFilters.map {
            it.listTitleText
        }.joinToString { it }
        return FilterType.FilterRating(selectedRatings)
    }

    private fun mapTopicFilterToFilterType(topicFilters: List<ListItemUnify>): FilterType.FilterTopic {
        val topicsMap = getTopicsMap()
        val selectedTopics = topicFilters.map {
            topicsMap[it.listTitleText] ?: ""
        }.joinToString { it }
        return FilterType.FilterTopic(selectedTopics)
    }

    private fun getTopicsMap(): Map<String, String> {
        return (_ratingAndTopics.value as? Success)?.data?.getTopicsMap() ?: mapOf()
    }

    private fun MutableList<FilterType>.replace(filter: FilterType) {
        this.forEach {
            if (it::class == filter::class) {
                remove(it)
            }
        }
        add(filter)
    }

    private fun MutableList<FilterType>.removeBasedOnClass(filter: FilterType) {
        this.forEach {
            if (it::class == filter::class) {
                remove(it)
            }
        }
    }

    private fun MutableList<FilterType>.getBasedOnClass(filter: FilterType): FilterType? {
        this.forEach {
            if (it::class == filter::class) {
                return it
            }
        }
        return null
    }
}