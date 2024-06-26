package com.tokopedia.review.feature.reading.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.review.ReviewApplinkConst
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.content.product.preview.data.mapper.ProductPreviewSourceMapper
import com.tokopedia.content.product.preview.utils.enableRollenceContentProductPreview
import com.tokopedia.content.product.preview.view.activity.ProductPreviewActivity
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.review.BuildConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.presentation.listener.ReviewReportBottomSheetListener
import com.tokopedia.review.common.presentation.widget.ReviewReportBottomSheet
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.common.util.getErrorMessage
import com.tokopedia.review.feature.reading.analytics.ReadReviewTracking
import com.tokopedia.review.feature.reading.analytics.ReadReviewTrackingConstants
import com.tokopedia.review.feature.reading.data.Keyword
import com.tokopedia.review.feature.reading.data.LikeDislike
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.review.feature.reading.data.ProductTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductReviewList
import com.tokopedia.review.feature.reading.data.ProductrevGetShopRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetShopReviewList
import com.tokopedia.review.feature.reading.data.VariantFilter
import com.tokopedia.review.feature.reading.di.DaggerReadReviewComponent
import com.tokopedia.review.feature.reading.di.ReadReviewComponent
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewAdapter
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewAdapterTypeFactory
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.factory.ReadReviewSortFilterFactory
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewAttachedImagesListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterBottomSheetListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterChipsListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHeaderListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHighlightedTopicListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewItemListener
import com.tokopedia.review.feature.reading.presentation.mapper.ReadReviewDataMapper
import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterBottomSheetType
import com.tokopedia.review.feature.reading.presentation.uimodel.SortTypeConstants
import com.tokopedia.review.feature.reading.presentation.uimodel.ToggleLikeUiModel
import com.tokopedia.review.feature.reading.presentation.viewmodel.ReadReviewViewModel
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewFilterBottomSheet
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewHeader
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewRatingOnlyEmptyState
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewStatisticsBottomSheet
import com.tokopedia.review.feature.reading.presentation.widget.SelectVariantUiModel
import com.tokopedia.review.feature.reading.presentation.widget.VariantFilterBottomSheet
import com.tokopedia.review.feature.reading.presentation.widget.toVariantUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.reviewer.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

open class ReadReviewFragment :
    BaseListFragment<ReadReviewUiModel, ReadReviewAdapterTypeFactory>(),
    HasComponent<ReadReviewComponent>,
    ReadReviewItemListener,
    ReadReviewHeaderListener,
    ReadReviewFilterChipsListener,
    ReadReviewFilterBottomSheetListener,
    ReviewReportBottomSheetListener,
    ReadReviewHighlightedTopicListener,
    ReadReviewAttachedImagesListener,
    ReviewPerformanceMonitoringContract,
    ReviewBasicInfoListener {

    companion object {
        const val MAX_RATING = 5
        const val MIN_RATING = 1
        const val PRODUCT_REVIEW_KEY = "ProductReview"
        const val INDEX_KEY = "Index"
        const val SHOP_ID_KEY = "ShopId"
        const val PRODUCT_ID_KEY = "ProductId"
        const val IS_PRODUCT_REVIEW_KEY = "isProductReview"
        const val EMPTY_FILTERED_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/others/review-reading-filtered-empty.png"
        const val GALLERY_ACTIVITY_CODE = 420
        const val REPORT_REVIEW_ACTIVITY_CODE = 421
        const val PRODUCT_SATISFACTION_RATE = "% pembeli puas belanja barang ini"
        fun createNewInstance(
            productId: String = "",
            shopId: String = "",
            isProductReview: Boolean,
            selectedTopic: String?
        ): ReadReviewFragment {
            val fragment = ReadReviewFragment()
            val fragmentArguments = Bundle()
            fragmentArguments.putString(ReviewConstants.ARGS_PRODUCT_ID, productId)
            fragmentArguments.putString(ReviewConstants.ARGS_SHOP_ID, shopId)
            fragmentArguments.putBoolean(ReviewConstants.ARGS_IS_PRODUCT_REVIEW, isProductReview)
            fragmentArguments.putString(ReviewConstants.ARGS_SELECTED_TOPIC, selectedTopic)
            fragment.arguments = fragmentArguments
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: ReadReviewViewModel

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private val enableContentProductPreview: Boolean
        get() = remoteConfig.getBoolean(RemoteConfigKey.ANDROID_CONTENT_PRODUCT_PREVIEW, false)

    protected var goToTopFab: FloatingButtonUnify? = null
    protected var reviewHeader: ReadReviewHeader? = null
    protected var currentScrollPosition = 0

    private var reviewPerformanceMonitoringListener: ReviewPerformanceMonitoringListener? = null

    private var reviewReadingCoordinatorLayout: CoordinatorLayout? = null
    private var statisticsBottomSheet: ReadReviewStatisticsBottomSheet? = null
    private var loadingView: View? = null
    private var listOnlyLoading: View? = null
    private var globalError: GlobalError? = null
    private var emptyFilteredState: View? = null
    private var emptyFilteredStateImage: ImageUnify? = null
    private var emptyRatingOnly: ReadReviewRatingOnlyEmptyState? = null
    private var errorType = GlobalError.NO_CONNECTION
    private var isProductReview: Boolean = false
    private var imageClickedPosition = 0
    private var tickerInfo: Ticker? = null

    private var selectedTopic: String? = null

    private var filterVariants: List<SelectVariantUiModel.Variant> = emptyList()
    private var pairedOptions: List<List<String>> = emptyList()

    private val readReviewFilterFactory by lazy {
        ReadReviewSortFilterFactory()
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.read_review_swipe_refresh_layout
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.read_review_recycler_view
    }

    override fun getAdapterTypeFactory(): ReadReviewAdapterTypeFactory {
        return ReadReviewAdapterTypeFactory(
            readReviewItemListener = this,
            attachedImagesClickListener = this,
            reviewBasicInfoListener = this,
            reviewMediaThumbnailListener = ReviewMediaThumbnailListener(),
            reviewMediaThumbnailRecycledViewPool = RecyclerView.RecycledViewPool()
        )
    }

    override fun createAdapterInstance(): BaseListAdapter<ReadReviewUiModel, ReadReviewAdapterTypeFactory> {
        return ReadReviewAdapter(adapterTypeFactory)
    }

    override fun getScreenName(): String {
        return ReadReviewTrackingConstants.SCREEN_NAME
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(t: ReadReviewUiModel?) {
        // No Op
    }

    override fun loadData(page: Int) {
        getProductReview(page)
    }

    override fun getComponent(): ReadReviewComponent? {
        return activity?.run {
            DaggerReadReviewComponent.builder()
                .reviewComponent(ReviewInstance.getComponent(application))
                .build()
        }
    }

    override fun onThreeDotsClicked(reviewId: String, shopId: String) {
        activity?.supportFragmentManager?.let {
            ReviewReportBottomSheet.newInstance(
                reviewId,
                shopId,
                this
            ).show(it, ReviewReportBottomSheet.TAG)
        }
    }

    override fun onThreeDotsProductInfoClicked(reviewId: String, shopId: String) {
        ReadReviewTracking.trackOnClickProductInfoThreeDots(reviewId, shopId)
        activity?.supportFragmentManager?.let {
            ReviewReportBottomSheet.newInstance(
                reviewId,
                shopId,
                this
            ).show(it, ReviewReportBottomSheet.TAG)
        }
    }

    override fun onProductInfoClicked(
        reviewId: String,
        shopName: String,
        productName: String,
        position: Int,
        shopId: String,
        productId: String
    ) {
        ReadReviewTracking.trackOnClickProductInfo(
            reviewId,
            shopName,
            productName,
            position,
            "",
            shopId,
            productId,
            viewModel.userId,
            trackingQueue

        )
        redirectToPDP(productId)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollUpListener(getRecyclerView(view)?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstCompletelyVisibleItemPosition =
                    (getRecyclerView(view)?.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition()
                        .orZero()
                currentScrollPosition += dy
                if (currentScrollPosition == 0 || firstCompletelyVisibleItemPosition == 0) {
                    goToTopFab?.hide()
                } else {
                    goToTopFab?.show()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val canScrollVertically = getRecyclerView(view)?.canScrollVertically(RecyclerView.NO_POSITION).orFalse()
                if (canScrollVertically && newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    reviewHeader?.hideRatingContainer()
                } else if (!canScrollVertically) {
                    reviewHeader?.showRatingContainer()
                }
            }
        }
    }

    override fun onLikeButtonClicked(
        reviewId: String,
        likeStatus: Int,
        index: Int
    ) {
        if (!viewModel.isLoggedIn) {
            goToLogin()
            return
        }
        ReadReviewTracking.trackOnLikeClicked(
            reviewId,
            isLiked(likeStatus),
            viewModel.getProductId()
        )
        viewModel.toggleLikeReview(reviewId, likeStatus, index)
    }

    override fun onShopReviewLikeButtonClicked(
        reviewId: String,
        shopId: String,
        productId: String,
        likeStatus: Int,
        index: Int
    ) {
        if (!viewModel.isLoggedIn) {
            goToLogin()
            return
        }
        ReadReviewTracking.trackOnShopReviewLikeClicked(
            reviewId,
            isLiked(likeStatus),
            shopId
        )
        viewModel.toggleLikeShopReview(reviewId, likeStatus, index)
    }

    override fun onItemImpressed(
        reviewId: String,
        position: Int,
        characterCount: Int,
        imageCount: Int
    ) {
        if (isProductReview) {
            with(getRatingAndTopics().rating) {
                ReadReviewTracking.trackOnItemImpressed(
                    reviewId,
                    position,
                    viewModel.userId,
                    totalRating,
                    totalRatingTextAndImage,
                    characterCount,
                    imageCount,
                    viewModel.getProductId(),
                    trackingQueue
                )
            }
        } else {
            with(getShopRatingAndTopics().rating) {
                ReadReviewTracking.trackOnShopReviewItemImpressed(
                    reviewId,
                    position,
                    viewModel.userId,
                    totalRating,
                    totalRatingTextAndImage,
                    characterCount,
                    imageCount,
                    viewModel.getShopId(),
                    trackingQueue
                )
            }
        }
    }

    override fun openStatisticsBottomSheet() {
        if (isProductReview) {
            ReadReviewTracking.trackOnClickPositiveReviewPercentage(
                getSatisfactionRate(),
                getRatingAndTopics().rating.totalRating,
                getRatingAndTopics().rating.totalRatingTextAndImage,
                viewModel.getProductId()
            )
        } else {
            ReadReviewTracking.trackOnClickShopPositiveReviewPercentage(
                getSatisfactionRate(),
                getShopRatingAndTopics().rating.totalRating,
                getShopRatingAndTopics().rating.totalRatingTextAndImage,
                viewModel.getShopId()
            )
        }
        if (statisticsBottomSheet == null) {
            statisticsBottomSheet = ReadReviewStatisticsBottomSheet.createInstance(
                getReviewStatistics(),
                getSatisfactionRate()
            )
        }
        activity?.supportFragmentManager?.let {
            statisticsBottomSheet?.show(
                it,
                ReadReviewStatisticsBottomSheet.READ_REVIEW_STATISTICS_BOTTOM_SHEET_TAG
            )
        }
    }

    override fun onFilterWithAttachmentClicked(isActive: Boolean) {
        clearAllData()
        if (isProductReview) {
            ReadReviewTracking.trackOnFilterClicked(
                context?.getString(R.string.review_reading_filter_with_attachment) ?: "",
                isActive,
                viewModel.getProductId()
            )
        } else {
            ReadReviewTracking.trackOnFilterShopReviewClicked(
                context?.getString(R.string.review_reading_filter_with_attachment) ?: "",
                isActive,
                viewModel.getProductId()
            )
        }
        viewModel.setFilterWithMedia(isActive, isProductReview)
        reviewHeader?.updateFilterWithMedia()
        showListOnlyLoading()
        updateTopicExtraction()
    }

    override fun onFilterWithTopicClicked(
        topics: List<ProductTopic>,
        index: Int,
        isActive: Boolean
    ) {
        val topicFilterTitle = getString(R.string.review_reading_topic_filter_title)
        if (isProductReview) {
            ReadReviewTracking.trackOnFilterClicked(
                topicFilterTitle,
                isActive,
                viewModel.getProductId()
            )
        } else {
            ReadReviewTracking.trackOnFilterShopReviewClicked(
                topicFilterTitle,
                isActive,
                viewModel.getProductId()
            )
        }
        val filterOptions = readReviewFilterFactory.getTopicFilters(topics)
        activity?.supportFragmentManager?.let {
            ReadReviewFilterBottomSheet.newInstance(
                topicFilterTitle,
                filterOptions,
                this,
                SortFilterBottomSheetType.TopicFilterBottomSheet,
                viewModel.getSelectedTopicFilter(isProductReview),
                "",
                index
            ).show(it, ReadReviewFilterBottomSheet.TAG)
        }
    }

    override fun onFilterTopic(keyword: String) {
        clearAllData()
        showListOnlyLoading()
        viewModel.setTopicFilter(keyword, isProductReview)
    }

    override fun onClickTopicChip(keyword: Keyword, position: Int, isActive: Boolean) {
        ReadReviewTracking.trackOnClickChipTopicExtraction(
            productId = viewModel.getProductId(),
            position = position,
            keyword = keyword,
            isActive = isActive,
            userId = viewModel.userId,
            trackingQueue = trackingQueue
        )
    }

    override fun onImpressTopicChip(keyword: Keyword, position: Int) {
        ReadReviewTracking.trackOnImpressChipTopicExtraction(
            productId = viewModel.getProductId(),
            position = position,
            keyword = keyword,
            userId = viewModel.userId,
            trackingQueue = trackingQueue
        )
    }

    override fun onClickLihatSemua() {
        ReadReviewTracking.onClickLihatSemuaTopicExtraction(
            productId = viewModel.getProductId(),
            userId = viewModel.userId
        )
    }

    override fun onFilterWithRatingClicked(index: Int, isActive: Boolean) {
        val ratingFilterTitle = getString(R.string.review_reading_rating_filter_title)
        if (isProductReview) {
            ReadReviewTracking.trackOnFilterClicked(
                ratingFilterTitle,
                isActive,
                viewModel.getProductId()
            )
        } else {
            ReadReviewTracking.trackOnFilterShopReviewClicked(
                ratingFilterTitle,
                isActive,
                viewModel.getProductId()
            )
        }
        val filterOptions =
            readReviewFilterFactory.getRatingFilters((MAX_RATING downTo MIN_RATING).map { it.toString() })
        activity?.supportFragmentManager?.let {
            ReadReviewFilterBottomSheet.newInstance(
                ratingFilterTitle,
                filterOptions,
                this,
                SortFilterBottomSheetType.RatingFilterBottomSheet,
                viewModel.getSelectedRatingFilter(),
                "",
                index
            ).show(it, ReadReviewFilterBottomSheet.TAG)
        }
    }

    override fun onFilterWithVariantClicked(isActive: Boolean) {
        val title = context?.getString(R.string.review_reading_filter_all_variants) ?: ""
        if (isProductReview) {
            ReadReviewTracking.trackOnFilterClicked(
                title,
                isActive,
                viewModel.getProductId()
            )
        } else {
            ReadReviewTracking.trackOnFilterShopReviewClicked(
                title,
                isActive,
                viewModel.getProductId()
            )
        }

        reviewHeader?.removeNewBadge(
            context?.getString(R.string.review_reading_filter_all_variants) ?: ""
        )

        VariantFilterBottomSheet.instance(
            this,
            filterVariants,
            pairedOptions
        ).show(parentFragmentManager, VariantFilterBottomSheet.TAG)
    }

    override fun onFilterSubmitted(
        filterName: String,
        selectedFilter: Set<ListItemUnify>,
        filterType: SortFilterBottomSheetType,
        index: Int
    ) {
        clearAllData()
        if (isProductReview) {
            ReadReviewTracking.trackOnApplyFilterClicked(
                filterName,
                selectedFilter.joinToString { it.listTitleText },
                viewModel.getProductId()
            )
        } else {
            when (filterName) {
                context?.getString(R.string.review_reading_rating_filter_title) -> ReadReviewTracking.trackOnShopReviewApplyRatingFilter(
                    filterName,
                    selectedFilter.joinToString { it.listTitleText },
                    viewModel.getShopId()
                )
                context?.getString(R.string.review_reading_topic_filter_title) -> ReadReviewTracking.trackOnShopReviewApplyTopicFilter(
                    filterName,
                    selectedFilter.joinToString { it.listTitleText },
                    viewModel.getShopId()
                )
            }
        }
        viewModel.setFilter(selectedFilter, filterType, isProductReview)
        showListOnlyLoading()
        reviewHeader?.updateFilter(selectedFilter, filterType, index)
        updateTopicExtraction()
    }

    override fun onSortSubmitted(selectedSort: ListItemUnify) {
        clearAllData()
        if (isProductReview) {
            ReadReviewTracking.trackOnApplySortClicked(
                selectedSort.listTitleText,
                viewModel.getProductId()
            )
        } else {
            ReadReviewTracking.trackOnShopReviewApplySortClicked(
                selectedSort.listTitleText,
                viewModel.getShopId()
            )
        }
        reviewHeader?.updateSelectedSort(selectedSort.listTitleText)
        viewModel.setSort(selectedSort.listTitleText, isProductReview)
        showListOnlyLoading()
    }

    override fun onFilterVariant(selectVariantUiModel: SelectVariantUiModel) = with(selectVariantUiModel) {
        this@ReadReviewFragment.filterVariants = variants
        clearAllData()
        reviewHeader?.updateSelectedVariant(count)
        viewModel.setVariantFilter(filter, opt, isProductReview)
        showListOnlyLoading()
        updateTopicExtraction()
    }

    override fun onReportOptionClicked(reviewId: String, shopId: String) {
        if (isProductReview) {
            ReadReviewTracking.trackOnReportClicked(reviewId, viewModel.getProductId())
        } else {
            ReadReviewTracking.trackOnShopReviewReportClicked(reviewId, shopId)
        }
        goToReportReview(reviewId, shopId)
    }

    override fun onSortClicked(chipTitle: String) {
        trackOnSortClicked()
        val listSortOptions = if (isProductReview) {
            listOf(
                getString(R.string.review_reading_sort_most_helpful),
                getString(R.string.review_reading_sort_latest),
                getString(R.string.review_reading_sort_highest_rating),
                getString(R.string.review_reading_sort_lowest_rating)
            )
        } else {
            listOf(
                getString(R.string.review_reading_sort_latest),
                getString(R.string.review_reading_sort_most_helpful),
                getString(R.string.review_reading_sort_highest_rating),
                getString(R.string.review_reading_sort_lowest_rating)
            )
        }
        val sortOptions = readReviewFilterFactory.getSortOptions(listSortOptions)
        activity?.supportFragmentManager?.let {
            ReadReviewFilterBottomSheet.newInstance(
                getString(R.string.review_reading_sort_title),
                sortOptions,
                this,
                SortFilterBottomSheetType.SortBottomSheet,
                setOf(),
                chipTitle,
                0
            ).show(it, ReadReviewFilterBottomSheet.TAG)
        }
    }

    override fun onClearFiltersClicked() {
        showListOnlyLoading()
        clearAllData()
        if (isProductReview) {
            ReadReviewTracking.trackOnClearFilter(viewModel.getProductId())
        } else {
            ReadReviewTracking.trackOnShopReviewClearFilter(viewModel.getShopId())
        }
        viewModel.clearFilters()
        viewModel.setSort(SortTypeConstants.MOST_HELPFUL_COPY, isProductReview)
        viewModel.getSelectedRatingFilter()
        if (isProductReview) {
            with(getRatingAndTopics()) {
                reviewHeader?.setAvailableFilters(topics, availableFilters, this@ReadReviewFragment)
            }
        } else {
            with(getShopRatingAndTopics()) {
                reviewHeader?.setAvailableFilters(topics, availableFilters, this@ReadReviewFragment)
            }
        }
        updateTopicExtraction()
        resetFilterVariants()
    }

    override fun onAttachedImagesClicked(
        productReview: ProductReview,
        attachmentId: String,
        reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel,
        positionClicked: Int,
        shopId: String,
        reviewItemPosition: Int
    ) {
        imageClickedPosition = reviewItemPosition
        if (isProductReview) {
            ReadReviewTracking.trackOnImageClicked(
                productReview.feedbackID,
                viewModel.getProductId()
            )
        } else {
            ReadReviewTracking.trackOnShopReviewImageClicked(productReview.feedbackID, shopId)
        }

        if (enableContentProductPreview && enableRollenceContentProductPreview) {
            goToProductPreviewActivityReviewSource(
                reviewId = productReview.feedbackID,
                attachmentId = attachmentId
            )
        } else {
            goToReviewMediaGallery(
                positionClicked = positionClicked,
                reviewMediaThumbnailUiModel = reviewMediaThumbnailUiModel,
                productReview = productReview,
                shopId = shopId
            )
        }
    }

    override fun stopPreparePerfomancePageMonitoring() {
        reviewPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        reviewPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        reviewPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        reviewPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        getRecyclerView(view)?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    reviewPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                    reviewPerformanceMonitoringListener?.stopPerformanceMonitoring()
                    getRecyclerView(view)?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                }
            })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): ReviewPerformanceMonitoringListener? {
        return if (context is ReviewPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isProductReview =
            arguments?.getBoolean(ReviewConstants.ARGS_IS_PRODUCT_REVIEW, false).orTrue()
        if (isProductReview) {
            getProductIdFromArguments()
        } else {
            getShopIdFromArguments()
        }
        if (isProductReview) {
            ReadReviewTracking.trackOpenScreen(screenName, viewModel.getProductId())
        } else {
            ReadReviewTracking.trackOpenScreenShopReview(viewModel.getShopId())
        }
        selectedTopic = arguments?.getString(ReviewConstants.ARGS_SELECTED_TOPIC, null)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        reviewPerformanceMonitoringListener =
            castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_read_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setupFab()
        showFullPageLoading()
        showListOnlyLoading()
        observeRatingAndTopics()
        observeShopRatingAndTopics()
        observeProductReviews()
        observeShopReviews()
        observeToggleLikeReview()
        observeTopicExtraction()
    }

    override fun loadInitialData() {
        isLoadingInitialData = true
        reviewHeader?.setIsProductReview(isProductReview)
        adapter.clearAllElements()
        hideError()
        showFullPageLoading()
        if (isProductReview) {
            getProductIdFromArguments()
        } else {
            getShopIdFromArguments()
        }

        val selectedTopic = selectedTopic ?: ""
        if (selectedTopic.isNotEmpty()) {
            viewModel.setTopicFilter(selectedTopic, isProductReview)
        } else {
            loadData(defaultInitialPage)
        }
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return view?.findViewById(R.id.read_review_swipe_refresh_layout)
    }

    override fun showEmpty() {
        // No Op
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == GALLERY_ACTIVITY_CODE && resultCode == Activity.RESULT_OK -> {
                loadInitialData()
            }
            requestCode == REPORT_REVIEW_ACTIVITY_CODE && resultCode == Activity.RESULT_OK -> {
                showToaster(getString(R.string.review_reading_success_submit_report))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSwipeRefresh() {
        hideSnackBarRetry()
        swipeToRefresh.isRefreshing = true
        adapter.clearAllElements()
        showListOnlyLoading()
        loadData(defaultInitialPage)
    }

    override fun onHighlightedTopicClicked(topicName: String, topicPosition: Int) {
        clearAllData()
        showListOnlyLoading()
        ReadReviewTracking.trackOnClickTopicRating(
            topicName,
            topicPosition,
            viewModel.userId,
            viewModel.getProductId()
        )
        reviewHeader?.updateFilterFromHighlightedTopic(topicName)
        viewModel.setFilterFromHighlightedTopic(topicName, isProductReview)
    }

    override fun onHighlightedTopicImpressed(topicName: String, topicPosition: Int) {
        ReadReviewTracking.trackOnImpressHighlightedTopic(
            topicName,
            topicPosition,
            viewModel.userId,
            viewModel.getProductId()
        )
    }

    override fun onUserNameClicked(
        feedbackId: String,
        userId: String,
        statistics: String,
        label: String
    ) {
        if (goToReviewCredibility(userId)) {
            ReadReviewTracking.trackOnGoToCredibility(
                feedbackId,
                userId,
                statistics,
                viewModel.getProductId(),
                viewModel.userId,
                label
            )
        }
    }

    protected fun showShopPageReviewHeader() {
        reviewHeader?.showShopPageReviewHeader()
    }

    fun logToCrashlytics(throwable: Throwable) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } else {
            throwable.printStackTrace()
        }
    }

    open fun redirectToPDP(productId: String) {
        context?.let {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                productId
            )
            startActivity(intent)
        }
    }

    private fun resetFilterVariants() {
        val options = filterVariants.flatMap { it.options }
        options.forEach { it.isSelected = false }
    }

    private fun updateTopicExtraction() {
        reviewHeader?.loadingTopicExtraction()
        viewModel.updateTopicExtraction()
    }

    private fun getProductIdFromArguments() {
        viewModel.setProductId(arguments?.getString(ReviewConstants.ARGS_PRODUCT_ID, "") ?: "")
    }

    private fun getShopIdFromArguments() {
        viewModel.setShopId(arguments?.getString(ReviewConstants.ARGS_SHOP_ID, "") ?: "")
    }

    private fun bindViews(view: View) {
        reviewReadingCoordinatorLayout = view.findViewById(R.id.read_review_coordinator_layout)
        reviewHeader = view.findViewById(R.id.read_review_header)
        reviewHeader?.setIsProductReview(isProductReview)
        loadingView = view.findViewById(R.id.read_review_loading)
        listOnlyLoading = view.findViewById(R.id.read_review_list_only_loading)
        globalError = view.findViewById(R.id.read_review_network_error)
        emptyFilteredState = view.findViewById(R.id.read_review_list_empty)
        emptyFilteredStateImage = view.findViewById(R.id.read_review_empty_list_image)
        goToTopFab = view.findViewById(R.id.read_review_go_to_top_fab)
        emptyRatingOnly = view.findViewById(R.id.read_review_rating_only)
        tickerInfo = view.findViewById(R.id.read_review_ticker_info)
    }

    private fun setupFab() {
        goToTopFab?.circleMainMenu?.setOnClickListener {
            getRecyclerView(view)?.smoothScrollToPosition(0)
        }
    }

    private fun getProductReview(page: Int) {
        viewModel.setPage(page, isProductReview)
    }

    private fun observeRatingAndTopics() {
        viewModel.ratingAndTopic.observe(viewLifecycleOwner, {
            hideFullPageLoading()
            when (it) {
                is Success -> onSuccessGetRatingAndTopic(it.data)
                is Fail -> onFailGetRatingAndTopic(it.throwable)
            }
        })
    }

    private fun observeShopRatingAndTopics() {
        viewModel.shopRatingAndTopic.observe(viewLifecycleOwner, {
            hideFullPageLoading()
            when (it) {
                is Success -> onSuccessGetShopRatingAndTopic(it.data)
                is Fail -> onFailGetRatingAndTopic(it.throwable)
            }
        })
    }

    private fun observeTopicExtraction() {
        viewModel.topicExtraction.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessUpdateTopicExtraction(it.data)
                is Fail -> onFailGetRatingAndTopic(it.throwable)
            }
        }
    }

    private fun onSuccessUpdateTopicExtraction(data: ProductrevGetProductRatingAndTopic) {
        reviewHeader?.setTopicExtraction(data.keywords, null, this)
    }

    private fun observeProductReviews() {
        viewModel.productReviews.observe(viewLifecycleOwner, {
            hideListOnlyLoading()
            when (it) {
                is Success -> onSuccessGetProductReviews(it.data)
                is Fail -> onFailGetProductReviews(it.throwable)
            }
        })
    }

    private fun observeShopReviews() {
        viewModel.shopReviews.observe(viewLifecycleOwner, {
            hideListOnlyLoading()
            when (it) {
                is Success -> onSuccessGetShopReviews(it.data)
                is Fail -> onFailGetProductReviews(it.throwable)
            }
        })
    }

    private fun observeToggleLikeReview() {
        viewModel.toggleLikeReview.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> updateLike(it.data)
                is Fail -> logToCrashlytics(it.throwable)
            }
        })
    }

    open fun onSuccessGetRatingAndTopic(ratingAndTopics: ProductrevGetProductRatingAndTopic) {
        if (ratingAndTopics.rating.totalRating == 0L) {
            showPageNotFound()
            hideFullPageLoading()
            return
        }
        hideError()
        hideFullPageLoading()
        if (ratingAndTopics.rating.totalRatingTextAndImage == 0L && ratingAndTopics.rating.totalRatingWithImage == 0L) {
            emptyRatingOnly?.apply {
                setRatingData(ratingAndTopics.rating)
                show()
            }
            hideListOnlyLoading()
            swipeToRefresh?.hide()
            return
        }
        reviewHeader?.apply {
            setRatingData(ratingAndTopics.rating)
            setListener(this@ReadReviewFragment)
            setAvailableFilters(
                ratingAndTopics.topics,
                ratingAndTopics.availableFilters,
                this@ReadReviewFragment
            )
            getRecyclerView(view)?.show()
            setHighlightedTopics(ratingAndTopics.topics, this@ReadReviewFragment)
            setSeeAll(false)
            setTopicExtraction(ratingAndTopics.keywords, selectedTopic, this@ReadReviewFragment)
            show()
        }

        filterVariants = ratingAndTopics.variantsData.toVariantUiModel()
        pairedOptions = ratingAndTopics.pairedVariantsData.map { it.optionIds }
    }

    open fun onSuccessGetShopRatingAndTopic(shopRatingAndTopics: ProductrevGetShopRatingAndTopic) {
        if (shopRatingAndTopics.rating.totalRating == 0L) {
            showPageNotFound()
            hideFullPageLoading()
            return
        }
        hideError()
        hideFullPageLoading()
        if (shopRatingAndTopics.rating.totalRatingTextAndImage == 0L && shopRatingAndTopics.rating.totalRatingWithImage == 0L) {
            emptyRatingOnly?.apply {
                setRatingData(shopRatingAndTopics.rating)
                show()
            }
            hideListOnlyLoading()
            swipeToRefresh?.hide()
            return
        }
        reviewHeader?.apply {
            setRatingData(shopRatingAndTopics.rating)
            setListener(this@ReadReviewFragment)
            setAvailableFilters(
                shopRatingAndTopics.topics,
                shopRatingAndTopics.availableFilters,
                this@ReadReviewFragment
            )
            getRecyclerView(view)?.show()
            setSeeAll(false)
            show()
        }
    }

    private fun onFailGetRatingAndTopic(throwable: Throwable) {
        logToCrashlytics(throwable)
        showError(throwable)
    }

    private fun onSuccessGetProductReviews(productrevGetProductReviewList: ProductrevGetProductReviewList) {
        stopNetworkRequestPerformanceMonitoring()
        startRenderPerformanceMonitoring()
        hideError()
        if (productrevGetProductReviewList.reviewList.isEmpty() && viewModel.isFilterSelected()) {
            showFilteredEmpty()
            swipeToRefresh?.isRefreshing = false
            return
        }
        hideFilteredEmpty()
        with(productrevGetProductReviewList) {
            renderList(
                viewModel.mapProductReviewToReadReviewUiModel(
                    reviewList,
                    shopInfo.shopID,
                    shopInfo.name
                ),
                hasNext
            )
            if (isListEmpty || currentPage == 0) hideFab() else showFab()
        }
        setTickerInfo(productrevGetProductReviewList.variantFilter)
    }

    private fun setTickerInfo(data: VariantFilter) {
        if (data.isUnavailable) {
            tickerInfo?.setTextDescription(data.ticker)
            tickerInfo?.show()
        } else tickerInfo?.hide()
    }

    private fun onSuccessGetShopReviews(productrevGetShopReviewList: ProductrevGetShopReviewList) {
        stopNetworkRequestPerformanceMonitoring()
        startRenderPerformanceMonitoring()
        hideError()
        if (productrevGetShopReviewList.shopReviewList.isEmpty() && viewModel.isFilterSelected()) {
            showFilteredEmpty()
            swipeToRefresh?.isRefreshing = false
            return
        }
        hideFilteredEmpty()
        with(productrevGetShopReviewList) {
            renderList(
                viewModel.mapShopReviewToReadReviewUiModel(
                    shopReviewList,
                    viewModel.getShopId(),
                    shopName
                ),
                hasNext
            )
            if (isListEmpty || currentPage == 0) hideFab() else showFab()
        }
    }

    open fun onFailGetProductReviews(throwable: Throwable) {
        logToCrashlytics(throwable)
        if (currentPage == 0) {
            showError(throwable)
        } else {
            showToasterError(getString(R.string.review_reading_connection_error)) {
                loadData(
                    currentPage
                )
            }
        }
    }

    open fun showError(throwable: Throwable) {
        globalError?.apply {
            if (throwable is SocketTimeoutException || throwable is UnknownHostException) {
                setType(GlobalError.NO_CONNECTION)
            } else {
                setType(GlobalError.SERVER_ERROR)
            }
            errorDescription.text = throwable.getErrorMessage(context)
            setActionClickListener {
                loadInitialData()
            }
            show()
        }
    }

    private fun showToasterError(message: String, action: () -> Unit) {
        reviewReadingCoordinatorLayout?.let {
            Toaster.build(
                it,
                message,
                Toaster.LENGTH_INDEFINITE,
                Toaster.TYPE_ERROR,
                getString(R.string.review_refresh)
            ) { action.invoke() }.show()
        }
    }

    private fun showToaster(message: String) {
        reviewReadingCoordinatorLayout?.let {
            Toaster.build(it, message, Toaster.toasterLength, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun hideError() {
        if (errorType == GlobalError.NO_CONNECTION) {
            globalError?.hide()
        }
    }

    private fun showListOnlyLoading() {
        hideFab()
        listOnlyLoading?.show()
    }

    private fun hideListOnlyLoading() {
        listOnlyLoading?.hide()
    }

    private fun showFullPageLoading() {
        hideFab()
        loadingView?.show()
    }

    private fun hideFullPageLoading() {
        loadingView?.hide()
    }

    open fun showFilteredEmpty() {
        hideFab()
        emptyFilteredStateImage?.setImageUrl(EMPTY_FILTERED_STATE_IMAGE_URL)
        emptyFilteredState?.show()
    }

    open fun hideFilteredEmpty() {
        emptyFilteredState?.hide()
    }

    open fun showPageNotFound() {
        hideFab()
        globalError?.apply {
            errorType = GlobalError.PAGE_NOT_FOUND
            setType(GlobalError.PAGE_NOT_FOUND)
            setActionClickListener {
                goToHome()
            }
            errorAction.text = getString(R.string.review_back_to_home)
            show()
        }
    }

    private fun hideFab() {
        goToTopFab?.hide()
    }

    private fun showFab() {
        goToTopFab?.show()
    }

    private fun updateLike(toggleLikeUiModel: ToggleLikeUiModel) {
        (adapter as? ReadReviewAdapter)?.updateLikeStatus(
            toggleLikeUiModel.itemIndex,
            toggleLikeUiModel.totalLike,
            toggleLikeUiModel.likeStatus
        )
    }

    private fun getReviewStatistics(): List<ProductReviewDetail> {
        return if (isProductReview) {
            (viewModel.ratingAndTopic.value as? Success)?.data?.rating?.detail ?: listOf()
        } else {
            (viewModel.shopRatingAndTopic.value as? Success)?.data?.rating?.detail ?: listOf()
        }
    }

    private fun getSatisfactionRate(): String {
        return if (isProductReview) {
            val satisfactionRateInt =
                (viewModel.ratingAndTopic.value as? Success)?.data?.rating?.satisfactionRate?.filter {
                    it.isDigit()
                }.orEmpty()
            "$satisfactionRateInt$PRODUCT_SATISFACTION_RATE"
        } else {
            val satisfactionRateInt =
                (viewModel.shopRatingAndTopic.value as? Success)?.data?.rating?.satisfactionRate?.filter {
                    it.isDigit()
                }.orEmpty()
            val satisfactionStringFormat = "$satisfactionRateInt% pembeli puas belanja di toko ini"
            satisfactionStringFormat
        }
    }

    private fun getRatingAndTopics(): ProductrevGetProductRatingAndTopic {
        return (viewModel.ratingAndTopic.value as? Success)?.data
            ?: ProductrevGetProductRatingAndTopic()
    }

    private fun getShopRatingAndTopics(): ProductrevGetShopRatingAndTopic {
        return (viewModel.shopRatingAndTopic.value as? Success)?.data
            ?: ProductrevGetShopRatingAndTopic()
    }

    private fun goToProductPreviewActivityReviewSource(
        reviewId: String,
        attachmentId: String
    ) {
        val productId = viewModel.getProductId()
        val intent = ProductPreviewActivity.createIntent(
            context = requireContext(),
            productPreviewSourceModel = ProductPreviewSourceMapper(
                productId = productId
            ).mapReviewSourceModel(
                reviewId = reviewId,
                attachmentId = attachmentId
            )
        )
        startActivity(intent)
    }

    private fun goToReviewMediaGallery(
        positionClicked: Int,
        reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel,
        productReview: ProductReview,
        shopId: String
    ) {
        ReviewMediaGalleryRouter.routeToReviewMediaGallery(
            context = requireContext(),
            pageSource = ReviewMediaGalleryRouter.PageSource.PDP,
            productID = viewModel.getProductId(),
            shopID = viewModel.getShopId(),
            isProductReview = isProductReview,
            isFromGallery = false,
            mediaPosition = positionClicked.plus(1),
            showSeeMore = false,
            preloadedDetailedReviewMediaResult = ReadReviewDataMapper.mapReadReviewDataToReviewMediaPreviewData(
                productReview,
                reviewMediaThumbnailUiModel,
                shopId
            )
        ).let {
            startActivityForResult(it, GALLERY_ACTIVITY_CODE)
        }
    }

    private fun goToReportReview(reviewId: String, shopId: String) {
        val intent =
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_REVIEW_ID, reviewId)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_SHOP_ID, shopId)
        startActivityForResult(intent, REPORT_REVIEW_ACTIVITY_CODE)
    }

    private fun goToLogin() {
        RouteManager.route(context, ApplinkConst.LOGIN)
    }

    private fun goToHome() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

    private fun isLiked(likeStatus: Int): Boolean {
        return likeStatus == LikeDislike.LIKED
    }

    private fun goToReviewCredibility(userId: String): Boolean {
        return RouteManager.route(
            context,
            Uri.parse(
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY,
                    userId,
                    ReviewApplinkConst.REVIEW_CREDIBILITY_SOURCE_REVIEW_READING
                )
            ).buildUpon()
                .appendQueryParameter(
                    ReviewApplinkConst.PARAM_PRODUCT_ID,
                    viewModel.getProductId()
                ).build()
                .toString()
        )
    }

    private fun trackOnSortClicked() {
        if (isProductReview) {
            ReadReviewTracking.trackOnFilterClicked(
                ReadReviewTrackingConstants.FILTER_NAME_SORT,
                !reviewHeader?.isSortFilterActive().orTrue(),
                viewModel.getProductId()
            )
        } else {
            ReadReviewTracking.trackOnFilterShopReviewClicked(
                ReadReviewTrackingConstants.FILTER_NAME_SORT,
                !reviewHeader?.isSortFilterActive().orTrue(),
                viewModel.getShopId()
            )
        }
    }

    private inner class ReviewMediaThumbnailListener : ReviewMediaThumbnailTypeFactory.Listener {
        override fun onMediaItemClicked(item: ReviewMediaThumbnailVisitable, position: Int) {
            (adapter as? ReadReviewAdapter)?.findReviewContainingThumbnail(item)?.let { (reviewItemPosition, element) ->
                onAttachedImagesClicked(
                    productReview = element.reviewData,
                    attachmentId = item.getAttachmentID(),
                    reviewMediaThumbnailUiModel = element.mediaThumbnails,
                    positionClicked = position,
                    shopId = element.shopId,
                    reviewItemPosition = reviewItemPosition
                )
            }
        }
    }
}
