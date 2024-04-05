package com.tokopedia.review.feature.reading.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.common.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.data.ProductReviewImageAttachments
import com.tokopedia.review.feature.reading.data.ProductReviewResponse
import com.tokopedia.review.feature.reading.data.ProductReviewUser
import com.tokopedia.review.feature.reading.data.ProductReviewVideoAttachments
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductReviewList
import com.tokopedia.review.feature.reading.data.ProductrevGetShopRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetShopReviewList
import com.tokopedia.review.feature.reading.data.ShopReview
import com.tokopedia.review.feature.reading.domain.usecase.GetProductRatingAndTopicsUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetProductReviewListUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetShopRatingAndTopicsUseCase
import com.tokopedia.review.feature.reading.domain.usecase.GetShopReviewListUseCase
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.uimodel.FilterType
import com.tokopedia.review.feature.reading.presentation.uimodel.SelectedFilters
import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterBottomSheetType
import com.tokopedia.review.feature.reading.presentation.uimodel.SortTypeConstants
import com.tokopedia.review.feature.reading.presentation.uimodel.ToggleLikeUiModel
import com.tokopedia.review.feature.reading.utils.ReadReviewUtils
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ReadReviewViewModel @Inject constructor(
        private val getProductRatingAndTopicsUseCase: GetProductRatingAndTopicsUseCase,
        private val getShopRatingAndTopicsUseCase: GetShopRatingAndTopicsUseCase,
        private val getProductReviewListUseCase: GetProductReviewListUseCase,
        private val getShopReviewListUseCase: GetShopReviewListUseCase,
        private val toggleLikeReviewUseCase: ToggleLikeReviewUseCase,
        private val userSessionInterface: UserSessionInterface,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        const val INITIAL_PAGE = 1
    }

    val userId: String
        get() = userSessionInterface.userId

    val isLoggedIn: Boolean
        get() = userSessionInterface.isLoggedIn

    private val _ratingAndTopics = MediatorLiveData<Result<ProductrevGetProductRatingAndTopic>>()
    val ratingAndTopic: LiveData<Result<ProductrevGetProductRatingAndTopic>>
        get() = _ratingAndTopics

    private val _topicExtraction = MutableLiveData<Result<ProductrevGetProductRatingAndTopic>>()
    val topicExtraction: LiveData<Result<ProductrevGetProductRatingAndTopic>>
        get() = _topicExtraction

    private val _shopRatingAndTopics = MediatorLiveData<Result<ProductrevGetShopRatingAndTopic>>()
    val shopRatingAndTopic: LiveData<Result<ProductrevGetShopRatingAndTopic>>
        get() = _shopRatingAndTopics

    private val _productReviews = MediatorLiveData<Result<ProductrevGetProductReviewList>>()
    val productReviews: LiveData<Result<ProductrevGetProductReviewList>>
        get() = _productReviews

    private val _shopReviews = MediatorLiveData<Result<ProductrevGetShopReviewList>>()
    val shopReviews: LiveData<Result<ProductrevGetShopReviewList>>
        get() = _shopReviews

    private val _toggleLikeReview = MutableLiveData<Result<ToggleLikeUiModel>>()
    val toggleLikeReview: LiveData<Result<ToggleLikeUiModel>>
        get() = _toggleLikeReview

    private val currentPage = MutableLiveData<Int>()
    private val currentPageShopReview = MutableLiveData<Int>()

    private val productId: MutableLiveData<String> = MutableLiveData()
    private var shopId: MutableLiveData<String> = MutableLiveData()
    private var sort: String = SortTypeConstants.MOST_HELPFUL_PARAM
    private var filter: SelectedFilters = SelectedFilters()
    private var isTopicExtraction: Boolean = false
    private var opt: String = ""

    init {
        _ratingAndTopics.addSource(productId) {
            getRatingAndTopics(it)
        }
        _shopRatingAndTopics.addSource(shopId) {
            getShopRatingAndTopics(it)
        }
        _productReviews.addSource(currentPage) {
            getProductReviews(it)
        }
        _shopReviews.addSource(currentPageShopReview) {
            getShopReviews(it)
        }
    }

    fun setProductId(productId: String) {
        this.productId.value = productId
        filter.clear()
        sort = SortTypeConstants.MOST_HELPFUL_PARAM
    }

    fun setShopId(shopid: String) {
        this.shopId.value = shopid
        filter.clear()
        sort = SortTypeConstants.LATEST_PARAM
    }

    fun getProductId(): String {
        return this.productId.value ?: ""
    }

    fun getShopId(): String {
        return this.shopId.value ?: ""
    }

    fun setPage(page: Int, isProductReview: Boolean) {
        if(isProductReview)
            currentPage.value = page
        else
            currentPageShopReview.value = page
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
                productId = productId.value ?: "",
                mediaThumbnails = ReviewMediaThumbnailUiModel(mapProductReviewAttachmentsToReviewMediaThumbnails(it))
            )
        }
    }

    private fun mapProductReviewAttachmentsToReviewMediaThumbnails(productReview: ProductReview): List<ReviewMediaThumbnailVisitable> {
        return mutableListOf<ReviewMediaThumbnailVisitable>().apply {
            val imageThumbnails = productReview.imageAttachments.map {
                ReviewMediaImageThumbnailUiModel(
                    uiState = ReviewMediaImageThumbnailUiState.Showing(
                        attachmentID = it.attachmentID,
                        reviewID = productReview.feedbackID,
                        thumbnailUrl = it.imageThumbnailUrl,
                        fullSizeUrl = it.uri
                    )
                )
            }
            val videoThumbnails = productReview.videoAttachments.map {
                ReviewMediaVideoThumbnailUiModel(
                    uiState = ReviewMediaVideoThumbnailUiState.Showing(
                        attachmentID = it.attachmentID,
                        reviewID = productReview.feedbackID,
                        url = it.url
                    )
                )
            }
            addAll(videoThumbnails)
            addAll(imageThumbnails)
        }
    }

    fun mapShopReviewToReadReviewUiModel(
        listShopReview: List<ShopReview>,
        shopId: String,
        shopName: String
    ): List<ReadReviewUiModel> {
        return listShopReview.map {
            ReadReviewUiModel(
                reviewData = mapShopReviewDataToProductReviewData(it),
                isShopViewHolder = true,
                shopId = shopId,
                shopName = shopName,
                productImage = it.product.productImageURL,
                productName = it.product.productName,
                productId = it.product.productID,
                mediaThumbnails = ReviewMediaThumbnailUiModel(mapShopReviewAttachmentsToReviewMediaThumbnails(it))
            )
        }
    }

    private fun mapShopReviewAttachmentsToReviewMediaThumbnails(shopReview: ShopReview): List<ReviewMediaThumbnailVisitable> {
        return shopReview.videoAttachments.map {
            ReviewMediaVideoThumbnailUiModel(
                uiState = ReviewMediaVideoThumbnailUiState.Showing(
                    attachmentID = it.attachmentID,
                    reviewID = shopReview.reviewID,
                    url = it.videoUrl
                )
            )
        }.plus(shopReview.imageAttachments.map {
            ReviewMediaImageThumbnailUiModel(
                uiState = ReviewMediaImageThumbnailUiState.Showing(
                    attachmentID = it.attachmentID,
                    reviewID = shopReview.reviewID,
                    thumbnailUrl = it.thumbnailURL,
                    fullSizeUrl = it.fullsizeURL
                )
            )
        })
    }

    private fun mapShopReviewDataToProductReviewData(shopReview: ShopReview) = ProductReview().apply {
        feedbackID = shopReview.reviewID
        message = shopReview.reviewText
        productRating = shopReview.rating
        reviewCreateTimestamp = shopReview.reviewTime
        isAnonymous = shopReview.state.isAnonymous
        isReportable = shopReview.state.isReportable
        reviewResponse = ProductReviewResponse(shopReview.replyText, shopReview.replyTime)
        user = ProductReviewUser().apply {
            userID = shopReview.reviewerID
            fullName = shopReview.reviewerName
        }
        imageAttachments = shopReview.imageAttachments.map {
            ProductReviewImageAttachments(it.thumbnailURL, it.fullsizeURL)
        }
        videoAttachments = shopReview.videoAttachments.map {
            ProductReviewVideoAttachments(it.videoUrl)
        }
        likeDislike = shopReview.likeDislike
        shopProductId = shopReview.product.productID
        badRatingReasonFmt = shopReview.badRatingReasonFmt
        variantName = shopReview.product.productVariant.variantName
    }

    fun toggleLikeReview(reviewId: String, likeStatus: Int, index: Int) {
        launchCatchError(block = {
            toggleLikeReviewUseCase.setParams(
                reviewId, ReadReviewUtils.invertLikeStatus(likeStatus)
            )
            val data = toggleLikeReviewUseCase.executeOnBackground()
            _toggleLikeReview.postValue(
                Success(
                    ToggleLikeUiModel(
                        data.productrevLikeReview.likeStatus,
                        data.productrevLikeReview.totalLike,
                        index
                    )
                )
            )
        }) {
            _toggleLikeReview.postValue(Fail(it))
        }
    }

    fun toggleLikeShopReview(reviewId: String, likeStatus: Int, index: Int) {
        launchCatchError(block = {
            toggleLikeReviewUseCase.setParams(
                reviewId,
                ReadReviewUtils.invertLikeStatus(likeStatus)
            )
            val data = toggleLikeReviewUseCase.executeOnBackground()

            _toggleLikeReview.postValue(
                    Success(
                            ToggleLikeUiModel(
                                    data.productrevLikeReview.likeStatus,
                                    data.productrevLikeReview.totalLike,
                                    index
                            )
                    )
            )
        }) {
            _toggleLikeReview.postValue(Fail(it))
        }
    }

    fun setFilter(selectedFilters: Set<ListItemUnify>, type: SortFilterBottomSheetType, isProductReview: Boolean) {
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
                this.filter.topic = mapTopicFilterToFilterType(selectedFilters, isProductReview)
            }
        }
        resetPage(isProductReview)
    }

    fun setTopicFilter(
        keyword: String,
        isProductReview: Boolean
    ) {
        this.filter.topic = if (keyword.isEmpty()) null
        else FilterType.FilterTopic(keyword)
        resetPage(isProductReview)
    }

    fun setVariantFilter(
        variantFilter: String,
        opt: String,
        isProductReview: Boolean
    ) {
        this.filter.variant = if (variantFilter.isEmpty()) null
        else FilterType.FilterVariant(variantFilter)
        this.opt = opt
        resetPage(isProductReview)
    }

    fun setFilterFromHighlightedTopic(topic: String, isProductReview: Boolean) {
        val topicsMap = getTopicsMap(isProductReview)
        this.filter.topic = FilterType.FilterTopic(topicsMap[topic] ?: "")
        resetPage(isProductReview)
    }

    fun setFilterWithMedia(isActive: Boolean, isProductReview: Boolean) {
        if (isActive) {
            this.filter.withMedia = null
        } else {
            this.filter.withMedia = getFilterWithMediaParam()
        }
        resetPage(isProductReview)
    }

    fun setSort(selectedSort: String, isProductReview: Boolean) {
        this.sort = mapSortOptionToSortParam(selectedSort)
        resetPage(isProductReview)
    }

    fun getSelectedRatingFilter(): Set<String> {
        val selectedFilters = filter.rating?.value ?: return emptySet()
        return selectedFilters.split(",").map { it.trim() }.toSet()
    }

    fun getSelectedTopicFilter(isProductReview: Boolean): Set<String> {
        val selectedFilters = filter.topic?.value?.split(",")?.map { it.trim() } ?: return setOf()
        val topicsMap = getTopicsMap(isProductReview)
        if (topicsMap.isEmpty()) return emptySet()
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
                filter.mapFilterToRequestParams(),
                opt
            )
            val data = getProductReviewListUseCase.executeOnBackground()
            _productReviews.postValue(Success(data.productrevGetProductReviewList))
        }) {
            _productReviews.postValue(Fail(it))
        }
    }

    private fun getShopReviews(page: Int) {
        launchCatchError(block = {
            getShopReviewListUseCase.setParams(
                    shopId.value.orEmpty(),
                    page,
                    sort,
                    filter.mapFilterToRequestParams()
            )
            val data = getShopReviewListUseCase.executeOnBackground()
            _shopReviews.postValue(Success(data.productrevGetShopReviewList))
        }) {
            _shopReviews.postValue(Fail(it))
        }
    }

    private fun getRatingAndTopics(productId: String) {
        launchCatchError(block = {
            getProductRatingAndTopicsUseCase.setParams(productId)
            val data = getProductRatingAndTopicsUseCase.executeOnBackground()
            isTopicExtraction = data.productrevGetProductRatingAndTopics.keywords.isNotEmpty()
            _ratingAndTopics.postValue(Success(data.productrevGetProductRatingAndTopics))
        }) {
            _ratingAndTopics.postValue(Fail(it))
        }
    }

    fun updateTopicExtraction() {
        if (!isTopicExtraction) return
        launchCatchError(block = {
            filter.topic = null
            val filterBy = filter.getSelectedParam()
            getProductRatingAndTopicsUseCase.setParams(
                productId.value ?: "",
                filterBy
            )
            val data = getProductRatingAndTopicsUseCase.executeOnBackground()
            _topicExtraction.postValue(Success(data.productrevGetProductRatingAndTopics))
        }) {
            _topicExtraction.postValue(Fail(it))
        }
    }

    private fun getShopRatingAndTopics(shopId: String) {
        launchCatchError(block = {
            getShopRatingAndTopicsUseCase.setParams(shopId)
            val data = getShopRatingAndTopicsUseCase.executeOnBackground()
            _shopRatingAndTopics.postValue(Success(data.productrevGetShopRatingAndTopics))
        }) {
            _shopRatingAndTopics.postValue(Fail(it))
        }
    }

    private fun resetPage(isProductReview: Boolean) {
        setPage(INITIAL_PAGE, isProductReview)
    }

    private fun mapSortOptionToSortParam(sort: String): String {
        return SortTypeConstants.sortMap[sort] ?: SortTypeConstants.MOST_HELPFUL_PARAM
    }

    private fun getFilterWithMediaParam(): FilterType.FilterWithMedia {
        return FilterType.FilterWithMedia()
    }

    private fun mapRatingFilterToFilterType(ratingFilters: Set<ListItemUnify>): FilterType.FilterRating {
        val selectedRatings = ratingFilters.map {
            it.listTitleText
        }.joinToString(separator = ",") { it }
        return FilterType.FilterRating(selectedRatings)
    }

    private fun mapTopicFilterToFilterType(topicFilters: Set<ListItemUnify>, isProductReview: Boolean): FilterType.FilterTopic {
        val topicsMap = getTopicsMap(isProductReview)
        val selectedTopics = topicFilters.map {
            topicsMap[it.listTitleText] ?: ""
        }.joinToString(separator = ",") { it }
        return FilterType.FilterTopic(selectedTopics)
    }

    private fun getTopicsMap(isProductReview: Boolean): Map<String, String> {
        return if(isProductReview)
            (_ratingAndTopics.value as? Success)?.data?.getTopicsMap() ?: mapOf()
        else
            (_shopRatingAndTopics.value as? Success)?.data?.getTopicsMap() ?: mapOf()
    }

}
