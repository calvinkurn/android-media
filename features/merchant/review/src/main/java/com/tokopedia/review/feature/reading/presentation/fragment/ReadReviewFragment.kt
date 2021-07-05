package com.tokopedia.review.feature.reading.presentation.fragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.BuildConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.presentation.listener.ReviewReportBottomSheetListener
import com.tokopedia.review.common.presentation.widget.ReviewReportBottomSheet
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.gallery.presentation.activity.ReviewGalleryActivity
import com.tokopedia.review.feature.reading.data.*
import com.tokopedia.review.feature.reading.di.DaggerReadReviewComponent
import com.tokopedia.review.feature.reading.di.ReadReviewComponent
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewAdapter
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewAdapterTypeFactory
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewItemListener
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.factory.ReadReviewSortFilterFactory
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewAttachedImagesListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterBottomSheetListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterChipsListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHeaderListener
import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterBottomSheetType
import com.tokopedia.review.feature.reading.presentation.uimodel.SortTypeConstants
import com.tokopedia.review.feature.reading.presentation.uimodel.ToggleLikeUiModel
import com.tokopedia.review.feature.reading.presentation.viewmodel.ReadReviewViewModel
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewFilterBottomSheet
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewHeader
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewRatingOnlyEmptyState
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewStatisticsBottomSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReadReviewFragment : BaseListFragment<ReadReviewUiModel, ReadReviewAdapterTypeFactory>(),
        HasComponent<ReadReviewComponent>, ReadReviewItemListener, ReadReviewHeaderListener,
        ReadReviewFilterChipsListener, ReadReviewFilterBottomSheetListener, ReviewReportBottomSheetListener,
        ReadReviewAttachedImagesListener, ReviewPerformanceMonitoringContract {

    companion object {
        const val MAX_RATING = 5
        const val MIN_RATING = 1
        const val PRODUCT_REVIEW_KEY = "ProductReview"
        const val INDEX_KEY = "Index"
        const val SHOP_ID_KEY = "ShopId"
        const val PRODUCT_ID_KEY = "ProductId"
        const val EMPTY_FILTERED_STATE_IMAGE_URL = "https://images.tokopedia.net/img/android/others/review-reading-filtered-empty.png"
        const val GALLERY_ACTIVITY_CODE = 420
        fun createNewInstance(productId: String): ReadReviewFragment {
            return ReadReviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ReviewConstants.ARGS_PRODUCT_ID, productId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ReadReviewViewModel

    private var reviewPerformanceMonitoringListener: ReviewPerformanceMonitoringListener? = null

    private var reviewReadingCoordinatorLayout: CoordinatorLayout? = null
    private var reviewHeader: ReadReviewHeader? = null
    private var statisticsBottomSheet: ReadReviewStatisticsBottomSheet? = null
    private var loadingView: View? = null
    private var listOnlyLoading: View? = null
    private var globalError: GlobalError? = null
    private var emptyFilteredState: View? = null
    private var emptyFilteredStateImage: ImageUnify? = null
    private var emptyRatingOnly: ReadReviewRatingOnlyEmptyState? = null
    private var goToTopFab: FloatingButtonUnify? = null

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
        return ReadReviewAdapterTypeFactory(this, this)
    }

    override fun createAdapterInstance(): BaseListAdapter<ReadReviewUiModel, ReadReviewAdapterTypeFactory> {
        return ReadReviewAdapter(adapterTypeFactory)
    }

    override fun getScreenName(): String {
        return ""
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
        activity?.supportFragmentManager?.let { ReviewReportBottomSheet.newInstance(reviewId, shopId, this).show(it, ReviewReportBottomSheet.TAG) }
    }

    override fun onLikeButtonClicked(reviewId: String, shopId: String, likeStatus: Int, index: Int) {
        viewModel.toggleLikeReview(reviewId, shopId, likeStatus, index)
    }

    override fun onChevronClicked() {
        if (statisticsBottomSheet == null) {
            statisticsBottomSheet = ReadReviewStatisticsBottomSheet.createInstance(getReviewStatistics(), getSatisfactionRate())
        }
        activity?.supportFragmentManager?.let { statisticsBottomSheet?.show(it, ReadReviewStatisticsBottomSheet.READ_REVIEW_STATISTICS_BOTTOM_SHEET_TAG) }
    }

    override fun onFilterWithAttachmentClicked(isActive: Boolean) {
        clearAllData()
        viewModel.setFilterWithImage(isActive)
        reviewHeader?.updateFilterWithImage()
        showListOnlyLoading()
    }

    override fun onFilterWithTopicClicked(topics: List<ProductTopic>, index: Int) {
        val filterOptions = readReviewFilterFactory.getTopicFilters(topics)
        activity?.supportFragmentManager?.let { ReadReviewFilterBottomSheet.newInstance(getString(R.string.review_reading_topic_filter_title), filterOptions, this, SortFilterBottomSheetType.TopicFilterBottomSheet, viewModel.getSelectedTopicFilter(), "", index).show(it, ReadReviewFilterBottomSheet.TAG) }
    }

    override fun onFilterWithRatingClicked(index: Int) {
        val filterOptions = readReviewFilterFactory.getRatingFilters((MAX_RATING downTo MIN_RATING).map { it.toString() })
        activity?.supportFragmentManager?.let {
            ReadReviewFilterBottomSheet.newInstance(getString(R.string.review_reading_rating_filter_title), filterOptions, this, SortFilterBottomSheetType.RatingFilterBottomSheet, viewModel.getSelectedRatingFilter()
                    , "", index).show(it, ReadReviewFilterBottomSheet.TAG)
        }
    }

    override fun onFilterSubmitted(selectedFilter: Set<ListItemUnify>, filterType: SortFilterBottomSheetType, index: Int) {
        clearAllData()
        viewModel.setFilter(selectedFilter, filterType)
        showListOnlyLoading()
        reviewHeader?.updateFilter(selectedFilter, filterType, index)
    }

    override fun onSortSubmitted(selectedSort: ListItemUnify) {
        clearAllData()
        reviewHeader?.updateSelectedSort(selectedSort.listTitleText)
        viewModel.setSort(selectedSort.listTitleText)
        showListOnlyLoading()
    }

    override fun onReportOptionClicked(reviewId: String, shopId: String) {
        goToReportReview(reviewId, shopId)
    }

    override fun onSortClicked(chipTitle: String) {
        val filterOptions = readReviewFilterFactory.getSortOptions(listOf(getString(R.string.review_reading_sort_most_helpful), getString(R.string.review_reading_sort_latest), getString(R.string.review_reading_sort_highest_rating), getString(R.string.review_reading_sort_lowest_rating)))
        activity?.supportFragmentManager?.let { ReadReviewFilterBottomSheet.newInstance(getString(R.string.review_reading_sort_title), filterOptions, this, SortFilterBottomSheetType.SortBottomSheet, setOf(), chipTitle, 0).show(it, ReadReviewFilterBottomSheet.TAG) }
    }

    override fun onClearFiltersClicked() {
        clearAllData()
        viewModel.clearFilters()
        viewModel.setSort(SortTypeConstants.MOST_HELPFUL_PARAM)
        viewModel.getSelectedRatingFilter()
        with(getRatingAndTopics()) {
            reviewHeader?.setAvailableFilters(topics, availableFilters, this@ReadReviewFragment)
        }
    }

    override fun onAttachedImagesClicked(productReview: ProductReview, positionClicked: Int, shopId: String) {
        context?.let {
            val cacheManager = SaveInstanceCacheManager(it, true)
            cacheManager.put(PRODUCT_REVIEW_KEY, productReview)
            cacheManager.put(INDEX_KEY, positionClicked)
            cacheManager.put(SHOP_ID_KEY, shopId)
            cacheManager.put(PRODUCT_ID_KEY, viewModel.getProductId())
            startActivityForResult(ReviewGalleryActivity.getIntent(it, cacheManager.id ?: ""), GALLERY_ACTIVITY_CODE)
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
        getRecyclerView(view)?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
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
        getProductIdFromArguments()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        reviewPerformanceMonitoringListener = castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        observeProductReviews()
        observeToggleLikeReview()
    }

    override fun loadInitialData() {
        isLoadingInitialData = true
        adapter.clearAllElements()
        showFullPageLoading()
        getProductIdFromArguments()
        loadData(defaultInitialPage)
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return view?.findViewById(R.id.read_review_swipe_refresh_layout)
    }

    override fun showEmpty() {
        // No Op
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GALLERY_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            loadInitialData()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getProductIdFromArguments() {
        viewModel.setProductId(arguments?.getString(ReviewConstants.ARGS_PRODUCT_ID, "") ?: "")
    }

    private fun bindViews(view: View) {
        reviewReadingCoordinatorLayout = view.findViewById(R.id.read_review_coordinator_layout)
        reviewHeader = view.findViewById(R.id.read_review_header)
        loadingView = view.findViewById(R.id.read_review_loading)
        listOnlyLoading = view.findViewById(R.id.read_review_list_only_loading)
        globalError = view.findViewById(R.id.read_review_network_error)
        emptyFilteredState = view.findViewById(R.id.read_review_list_empty)
        emptyFilteredStateImage = view.findViewById(R.id.read_review_empty_list_image)
        goToTopFab = view.findViewById(R.id.read_review_go_to_top_fab)
        emptyRatingOnly = view.findViewById(R.id.read_review_rating_only)
    }

    private fun setupFab() {
        goToTopFab?.circleMainMenu?.setOnClickListener {
            getRecyclerView(view)?.smoothScrollToPosition(0)
        }
    }

    private fun getProductReview(page: Int) {
        viewModel.setPage(page)
    }

    private fun observeRatingAndTopics() {
        viewModel.ratingAndTopic.observe(viewLifecycleOwner, Observer {
            hideFullPageLoading()
            when (it) {
                is Success -> onSuccessGetRatingAndTopic(it.data)
                is Fail -> onFailGetRatingAndTopic()
            }
        })
    }

    private fun observeProductReviews() {
        viewModel.productReviews.observe(viewLifecycleOwner, Observer {
            hideListOnlyLoading()
            when (it) {
                is Success -> onSuccessGetProductReviews(it.data)
                is Fail -> onFailGetProductReviews()
            }
        })
    }

    private fun observeToggleLikeReview() {
        viewModel.toggleLikeReview.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> updateLike(it.data)
                is Fail -> logToCrashlytics(it.throwable)
            }
        })
    }

    private fun onSuccessGetRatingAndTopic(ratingAndTopics: ProductrevGetProductRatingAndTopic) {
        if (ratingAndTopics.rating.totalRating == 0) {
            showPageNotFound()
            hideFullPageLoading()
            return
        }
        hideError()
        if (ratingAndTopics.rating.totalRatingTextAndImage == 0 && ratingAndTopics.rating.totalRatingWithImage == 0) {
            emptyRatingOnly?.apply {
                setRatingData(ratingAndTopics.rating)
                show()
            }
            hideFullPageLoading()
            hideListOnlyLoading()
            return
        }
        reviewHeader?.apply {
            setRatingData(ratingAndTopics.rating)
            setListener(this@ReadReviewFragment)
            setAvailableFilters(ratingAndTopics.topics, ratingAndTopics.availableFilters, this@ReadReviewFragment)
            show()
        }
    }

    private fun onFailGetRatingAndTopic() {
        showError()
    }

    private fun onSuccessGetProductReviews(productrevGetProductReviewList: ProductrevGetProductReviewList) {
        stopNetworkRequestPerformanceMonitoring()
        startRenderPerformanceMonitoring()
        hideError()
        if (productrevGetProductReviewList.reviewList.isEmpty() && viewModel.isFilterSelected()) {
            showFilteredEmpty()
            return
        }
        hideFilteredEmpty()
        with(productrevGetProductReviewList) {
            renderList(viewModel.mapProductReviewToReadReviewUiModel(reviewList, shopInfo.shopID, shopInfo.name), hasNext)
            if(isListEmpty) hideFab() else showFab()
        }
    }

    private fun onFailGetProductReviews() {
        if (currentPage == 0) return
        showToasterError(getString(R.string.review_reading_connection_error)) { loadData(currentPage) }
    }

    private fun showError() {
        globalError?.apply {
            setActionClickListener {
                loadInitialData()
            }
            show()
        }
    }

    private fun showToasterError(message: String, action: () -> Unit) {
        reviewReadingCoordinatorLayout?.let {
            Toaster.build(it, message, Toaster.toasterLength, Toaster.TYPE_ERROR, getString(R.string.review_refresh), View.OnClickListener { action.invoke() }).show()
        }
    }

    private fun hideError() {
        if (globalError?.getErrorType() == GlobalError.NO_CONNECTION) {
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

    private fun showFilteredEmpty() {
        hideFab()
        emptyFilteredStateImage?.setImageUrl(EMPTY_FILTERED_STATE_IMAGE_URL)
        emptyFilteredState?.show()
    }

    private fun hideFilteredEmpty() {
        emptyFilteredState?.hide()
    }

    private fun showPageNotFound() {
        hideFab()
        globalError?.apply {
            setType(GlobalError.PAGE_NOT_FOUND)
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
        (adapter as? ReadReviewAdapter)?.updateLikeStatus(toggleLikeUiModel.itemIndex, toggleLikeUiModel.totalLike, toggleLikeUiModel.likeStatus)
    }

    private fun getReviewStatistics(): List<ProductReviewDetail> {
        return (viewModel.ratingAndTopic.value as? Success)?.data?.rating?.detail ?: listOf()
    }

    private fun getSatisfactionRate(): String {
        return (viewModel.ratingAndTopic.value as? Success)?.data?.rating?.satisfactionRate ?: ""
    }

    private fun getRatingAndTopics(): ProductrevGetProductRatingAndTopic {
        return (viewModel.ratingAndTopic.value as? Success)?.data ?: ProductrevGetProductRatingAndTopic()
    }

    private fun goToReportReview(reviewId: String, shopId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_REVIEW_ID, reviewId)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_SHOP_ID, shopId.toIntOrZero())
        startActivity(intent)
    }

    private fun logToCrashlytics(throwable: Throwable) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } else {
            throwable.printStackTrace()
        }
    }

}