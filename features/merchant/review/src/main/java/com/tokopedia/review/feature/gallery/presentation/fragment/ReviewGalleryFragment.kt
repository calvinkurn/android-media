package com.tokopedia.review.feature.gallery.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.BuildConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.gallery.analytics.ReviewGalleryTracking
import com.tokopedia.review.feature.gallery.data.Detail
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImage
import com.tokopedia.review.feature.gallery.di.DaggerReviewGalleryComponent
import com.tokopedia.review.feature.gallery.di.ReviewGalleryComponent
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryAdapterTypeFactory
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryUiModel
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryHeaderListener
import com.tokopedia.review.feature.gallery.presentation.uimodel.ReviewGalleryRoutingUiModel
import com.tokopedia.review.feature.gallery.presentation.viewmodel.ReviewGalleryViewModel
import com.tokopedia.review.feature.gallery.presentation.widget.ReviewGalleryLoadingView
import com.tokopedia.review.feature.imagepreview.presentation.activity.ReviewImagePreviewActivity
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.review.feature.reading.presentation.fragment.ReadReviewFragment
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHeaderListener
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewHeader
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewStatisticsBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReviewGalleryFragment :
    BaseListFragment<ReviewGalleryUiModel, ReviewGalleryAdapterTypeFactory>(),
    HasComponent<ReviewGalleryComponent>, ReviewPerformanceMonitoringContract,
    ReadReviewHeaderListener, ReviewGalleryHeaderListener {

    companion object {
        const val REVIEW_GALLERY_SPAN_COUNT = 2
        const val KEY_REVIEW_GALLERY_ROUTING_DATA = "reviewGalleryData"
        const val IMAGE_PREVIEW_ACTIVITY_CODE = 200
        fun createNewInstance(productId: String): ReviewGalleryFragment {
            return ReviewGalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ReviewConstants.ARGS_PRODUCT_ID, productId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ReviewGalleryViewModel

    private var reviewGalleryCoordinatorLayout: CoordinatorLayout? = null
    private var reviewHeader: ReadReviewHeader? = null
    private var loadingView: ReviewGalleryLoadingView? = null
    private var statisticsBottomSheet: ReadReviewStatisticsBottomSheet? = null
    private var reviewPerformanceMonitoringListener: ReviewPerformanceMonitoringListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        }
        getProductIdFromArguments()
        ReviewGalleryTracking.trackOpenScreen(viewModel.getProductId())
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        showFullPageLoading()
        observeReviewImages()
        observeRatingAndTopics()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.tokopedia.review.R.layout.fragment_review_gallery, container, false)
    }

    override fun getComponent(): ReviewGalleryComponent? {
        return activity?.run {
            DaggerReviewGalleryComponent.builder()
                .reviewComponent(ReviewInstance.getComponent(application))
                .build()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(t: ReviewGalleryUiModel) {
        ReviewGalleryTracking.trackClickImage(
            t.attachmentId,
            t.feedbackId,
            viewModel.getProductId()
        )
        goToImagePreview(t)
    }

    override fun loadData(page: Int) {
        getReviewImages(page)
    }

    override fun getAdapterTypeFactory(): ReviewGalleryAdapterTypeFactory {
        return ReviewGalleryAdapterTypeFactory()
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.review_gallery_recyclerview
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return view?.findViewById(R.id.review_gallery_swipe_refresh)
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, REVIEW_GALLERY_SPAN_COUNT, RecyclerView.VERTICAL, false)
    }

    override fun openStatisticsBottomSheet() {
        ReviewGalleryTracking.trackClickSatisfactionScore(
            getSatisfactionRate(),
            getRating(),
            getTotalReview(),
            viewModel.getProductId()
        )
        val satisfactionRateInt = getSatisfactionRate().filter {
            it.isDigit()
        }
        if (statisticsBottomSheet == null) {
            statisticsBottomSheet = ReadReviewStatisticsBottomSheet.createInstance(
                getReviewStatistics(),
                "$satisfactionRateInt${ReadReviewFragment.PRODUCT_SATISFACTION_RATE}"
            )
        }
        activity?.supportFragmentManager?.let {
            statisticsBottomSheet?.show(
                it,
                ReadReviewStatisticsBottomSheet.READ_REVIEW_STATISTICS_BOTTOM_SHEET_TAG
            )
        }
    }

    override fun onSeeAllClicked() {
        ReviewGalleryTracking.trackClickSeeAll(viewModel.getProductId())
        goToReadingPage()
    }

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        showFullPageLoading()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_PREVIEW_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            showFullPageLoading()
            clearAllData()
            loadInitialData()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun loadInitialData() {
        getProductIdFromArguments()
        super.loadInitialData()
    }

    private fun getProductIdFromArguments() {
        viewModel.setProductId(arguments?.getString(ReviewConstants.ARGS_PRODUCT_ID, "") ?: "")
    }

    private fun bindViews(view: View) {
        reviewGalleryCoordinatorLayout = view.findViewById(R.id.review_gallery_coordinator_layout)
        reviewHeader = view.findViewById(R.id.review_gallery_header)
        loadingView = view.findViewById(R.id.review_gallery_shimmering)
    }

    private fun observeRatingAndTopics() {
        viewModel.rating.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessGetRating(it.data)
                is Fail -> onFailGetRating(it.throwable)
            }
        })
    }

    private fun observeReviewImages() {
        viewModel.reviewImages.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessGetReviewImages(it.data)
                is Fail -> onFailGetReviewImages(it.throwable)
            }
        })
    }

    private fun onSuccessGetRating(rating: ProductRating) {
        reviewHeader?.apply {
            setRatingData(rating)
            setListener(this@ReviewGalleryFragment)
            setSeeAll(this@ReviewGalleryFragment)
            show()
        }
    }

    private fun onFailGetRating(throwable: Throwable) {
        logToCrashlytics(throwable)
    }

    private fun onSuccessGetReviewImages(productrevGetReviewImage: ProductrevGetReviewImage) {
        hideFullPageLoading()
        swipeToRefresh.isRefreshing = false
        renderList(mapToUiModel(productrevGetReviewImage), productrevGetReviewImage.hasNext)
    }

    private fun onFailGetReviewImages(throwable: Throwable) {
        if (isFirstPage()) showFullPageError()
        showToasterError(throwable.message ?: getString(R.string.review_reading_connection_error)) {
            if (isFirstPage()) loadInitialData() else {
                loadData(currentPage)
            }
        }
        logToCrashlytics(throwable)
    }

    private fun getReviewImages(page: Int) {
        viewModel.setPage(page)
    }

    private fun logToCrashlytics(throwable: Throwable) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } else {
            throwable.printStackTrace()
        }
    }

    private fun mapToUiModel(productrevGetReviewImage: ProductrevGetReviewImage): List<ReviewGalleryUiModel> {
        return productrevGetReviewImage.reviewImages.map {
                getReviewGalleryUiModelBasedOnDetail(
                    productrevGetReviewImage.detail,
                    it.feedbackId,
                    it.imageId,
                    it.imageNumber
                )
        }
    }

    private fun getReviewGalleryUiModelBasedOnDetail(
        detail: Detail,
        feedbackId: String,
        attachmentId: String,
        imageNumber: Int
    ): ReviewGalleryUiModel {
        var reviewGalleryUiModel = ReviewGalleryUiModel()
        detail.reviewDetail.firstOrNull { it.feedbackId == feedbackId }?.apply {
            reviewGalleryUiModel = reviewGalleryUiModel.copy(
                rating = this.rating,
                variantName = this.variantName,
                reviewerName = this.user.fullName,
                isLiked = this.isLiked,
                totalLiked = this.totalLike,
                review = this.review,
                reviewTime = this.createTimestamp,
                isReportable = this.isReportable,
                userStats = this.userStats,
                isAnonymous = this.isAnonymous,
                userId = this.user.userId,
                userImage = this.user.image,
                badRatingReason = this.badRatingReasonFmt
            )
        }
        detail.reviewGalleryImages.firstOrNull { it.attachmentId == attachmentId }?.apply {
            reviewGalleryUiModel = reviewGalleryUiModel.copy(
                imageUrl = this.fullsizeURL,
                fullImageUrl = this.fullsizeURL,
                attachmentId = this.attachmentId
            )
        }
        reviewGalleryUiModel = reviewGalleryUiModel.copy(
            feedbackId = feedbackId,
            imageNumber = imageNumber
        )
        return reviewGalleryUiModel
    }

    private fun getReviewStatistics(): List<ProductReviewDetail> {
        return (viewModel.rating.value as? Success)?.data?.detail ?: listOf()
    }

    private fun getSatisfactionRate(): String {
        return (viewModel.rating.value as? Success)?.data?.satisfactionRate ?: ""
    }

    private fun getRating(): String {
        return (viewModel.rating.value as? Success)?.data?.ratingScore ?: ""
    }

    private fun getTotalReview(): String {
        return (viewModel.rating.value as? Success)?.data?.totalRatingTextAndImage.toString()
    }

    private fun goToReadingPage() {
        RouteManager.route(context, ApplinkConst.PRODUCT_REVIEW, viewModel.getProductId())
    }

    private fun showFullPageLoading() {
        loadingView?.apply {
            showLoading()
            show()
        }
    }

    private fun hideFullPageLoading() {
        loadingView?.hide()
    }

    private fun showFullPageError() {
        loadingView?.apply {
            showError()
            show()
        }
    }

    private fun showToasterError(message: String, action: () -> Unit) {
        reviewGalleryCoordinatorLayout?.let {
            Toaster.build(
                it,
                message,
                Toaster.toasterLength,
                Toaster.TYPE_ERROR,
                getString(R.string.review_refresh)
            ) { action.invoke() }.show()
        }
    }

    private fun goToImagePreview(reviewGalleryUiModel: ReviewGalleryUiModel) {
        context?.let {
            val cacheManager = SaveInstanceCacheManager(it, true)
            cacheManager.put(
                KEY_REVIEW_GALLERY_ROUTING_DATA, ReviewGalleryRoutingUiModel(
                    viewModel.getProductId(),
                    currentPage,
                    getImageCount(),
                    adapter.data,
                    reviewGalleryUiModel.imageNumber,
                    viewModel.getShopId()
                )
            )
            startActivityForResult(
                ReviewImagePreviewActivity.getIntent(it, cacheManager.id ?: "", true),
                IMAGE_PREVIEW_ACTIVITY_CODE
            )
        }
    }

    private fun getImageCount(): Long {
        return (viewModel.reviewImages.value as? Success)?.data?.detail?.imageCount ?: 0L
    }

    private fun isFirstPage(): Boolean {
        return currentPage + 1 == defaultInitialPage
    }
}