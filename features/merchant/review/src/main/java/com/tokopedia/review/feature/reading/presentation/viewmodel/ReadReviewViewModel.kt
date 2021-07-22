package com.tokopedia.review.feature.reading.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductReviewList
import com.tokopedia.review.feature.reading.domain.usecase.GetProductRatingAndTopicsUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetProductReviewListUseCase
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.uimodel.*
import com.tokopedia.review.feature.reading.utils.ReadReviewUtils
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ReadReviewViewModel @Inject constructor(
    private val getProductRatingAndTopicsUseCase: GetProductRatingAndTopicsUseCase,
    private val getProductReviewListUseCase: GetProductReviewListUseCase,
    private val toggleLikeReviewUseCase: ToggleLikeReviewUseCase,
    private val userSessionInterface: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        const val INITIAL_PAGE = 1
    }

    val userId: String
        get() = userSessionInterface.userId

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
    private var filter: SelectedFilters = SelectedFilters()

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
        filter.clear()
        sort = SortTypeConstants.MOST_HELPFUL_PARAM
    }

    fun getProductId(): String {
        return this.productId.value ?: ""
    }

    fun setPage(page: Int) {
        currentPage.value = page
    }

    fun mapProductReviewToReadReviewUiModel(
        productReviews: List<ProductReview>,
        shopId: String,
        shopName: String
    ): List<ReadReviewUiModel> {
        return productReviews.map {
            ReadReviewUiModel(
                reviewData = it,
                isShopViewHolder = false,
                shopId = shopId,
                shopName = shopName,
                productId = productId.value ?: ""
            )
        }
    }

    fun toggleLikeReview(reviewId: String, shopId: String, likeStatus: Int, index: Int) {
        launchCatchError(block = {
            toggleLikeReviewUseCase.setParams(
                reviewId, shopId, productId.value
                    ?: "", ReadReviewUtils.invertLikeStatus(likeStatus)
            )
            val data = toggleLikeReviewUseCase.executeOnBackground()
            _toggleLikeReview.postValue(
                Success(
                    ToggleLikeUiModel(
                        data.toggleProductReviewLike.likeStatus,
                        data.toggleProductReviewLike.totalLike,
                        index
                    )
                )
            )
        }) {
            _toggleLikeReview.postValue(Fail(it))
        }
    }

    fun setFilter(selectedFilters: Set<ListItemUnify>, type: SortFilterBottomSheetType) {
        if (type == SortFilterBottomSheetType.RatingFilterBottomSheet) {
            if (selectedFilters.isEmpty()) {
                this.filter.rating = null
            } else {
                this.filter.rating = mapRatingFilterToFilterType(selectedFilters)
            }
        } else {
            if (selectedFilters.isEmpty()) {
                this.filter.topic = null
            } else {
                this.filter.topic = mapTopicFilterToFilterType(selectedFilters)
            }
        }
        resetPage()
    }

    fun setFilterWithImage(isActive: Boolean) {
        if (isActive) {
            this.filter.withImage = null
        } else {
            this.filter.withImage = getFilterWithImageParam()
        }
        resetPage()
    }

    fun setSort(selectedSort: String) {
        this.sort = mapSortOptionToSortParam(selectedSort)
        resetPage()
    }

    fun getSelectedRatingFilter(): Set<String> {
        val selectedFilters = filter.rating?.value ?: return emptySet()
        return selectedFilters.split(",").map { it.trim() }.toSet()
    }

    fun getSelectedTopicFilter(): Set<String> {
        val selectedFilters = filter.topic?.value?.split(",")?.map { it.trim() } ?: return setOf()
        val topicsMap = getTopicsMap()
        if(topicsMap.isEmpty()) return emptySet()
        var result = emptySet<String>()
        selectedFilters.forEach {
            result = result.plus(getKey(topicsMap, it))
        }
        return result
    }

    fun clearFilters() {
        this.filter.clear()
    }

    fun isFilterSelected(): Boolean {
        return this.filter.isAnyFilterSelected()
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
            getProductReviewListUseCase.setParams(
                productId.value ?: "",
                page,
                sort,
                filter.mapFilterToRequestParams()
            )
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

    private fun getFilterWithImageParam(): FilterType.FilterWithImage {
        return FilterType.FilterWithImage()
    }

    private fun mapRatingFilterToFilterType(ratingFilters: Set<ListItemUnify>): FilterType.FilterRating {
        val selectedRatings = ratingFilters.map {
            it.listTitleText
        }.joinToString(separator = ",") { it }
        return FilterType.FilterRating(selectedRatings)
    }

    private fun mapTopicFilterToFilterType(topicFilters: Set<ListItemUnify>): FilterType.FilterTopic {
        val topicsMap = getTopicsMap()
        val selectedTopics = topicFilters.map {
            topicsMap[it.listTitleText] ?: ""
        }.joinToString(separator = ",") { it }
        return FilterType.FilterTopic(selectedTopics)
    }

    private fun getTopicsMap(): Map<String, String> {
        return (_ratingAndTopics.value as? Success)?.data?.getTopicsMap() ?: mapOf()
    }
}