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
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.review.BuildConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.gallery.analytics.ReviewGalleryTracking
import com.tokopedia.review.feature.gallery.di.DaggerReviewGalleryComponent
import com.tokopedia.review.feature.gallery.di.ReviewGalleryComponent
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryAdapter
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryAdapterTypeFactory
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryImageThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryMediaThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryVideoThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryHeaderListener
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryMediaThumbnailListener
import com.tokopedia.review.feature.gallery.presentation.viewmodel.ReviewGalleryViewModel
import com.tokopedia.review.feature.gallery.presentation.widget.ReviewGalleryLoadingView
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.review.feature.reading.presentation.fragment.ReadReviewFragment
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHeaderListener
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewHeader
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewStatisticsBottomSheet
import com.tokopedia.reviewcommon.extension.isMoreThanZero
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReviewGalleryFragment :
    BaseListFragment<ReviewGalleryMediaThumbnailUiModel, ReviewGalleryAdapterTypeFactory>(),
    HasComponent<ReviewGalleryComponent>, ReviewPerformanceMonitoringContract,
    ReadReviewHeaderListener, ReviewGalleryHeaderListener, ReviewGalleryMediaThumbnailListener {

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

    override fun onItemClicked(t: ReviewGalleryMediaThumbnailUiModel) {
        // noop
    }

    override fun loadData(page: Int) {
        getReviewImages(page)
    }

    override fun getAdapterTypeFactory(): ReviewGalleryAdapterTypeFactory {
        return ReviewGalleryAdapterTypeFactory(this)
    }

    override fun createAdapterInstance(): BaseListAdapter<ReviewGalleryMediaThumbnailUiModel, ReviewGalleryAdapterTypeFactory> {
        return ReviewGalleryAdapter(adapterTypeFactory)
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

    override fun onThumbnailClicked(reviewGalleryMediaThumbnailUiModel: ReviewGalleryMediaThumbnailUiModel) {
        ReviewGalleryTracking.trackClickImage(
            reviewGalleryMediaThumbnailUiModel.attachmentId,
            reviewGalleryMediaThumbnailUiModel.feedbackId,
            viewModel.getProductId()
        )
        goToMediaPreview(reviewGalleryMediaThumbnailUiModel)
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
        viewModel.reviewMedia.observe(viewLifecycleOwner, {
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

    private fun onSuccessGetReviewImages(productrevGetReviewMedia: ProductrevGetReviewMedia) {
        hideFullPageLoading()
        swipeToRefresh.isRefreshing = false
        renderList(mapToUiModel(productrevGetReviewMedia), productrevGetReviewMedia.hasNext)
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

    private fun mapToUiModel(productrevGetReviewImage: ProductrevGetReviewMedia): List<ReviewGalleryMediaThumbnailUiModel> {
        return productrevGetReviewImage.reviewMedia.mapNotNull {
            if ((it.imageId.isNotBlank() && it.imageId.toLongOrNull() == null) || it.imageId.toLongOrZero().isMoreThanZero()) {
                getReviewGalleryImageThumbnailUiModelBasedOnDetail(
                    productrevGetReviewImage.detail,
                    it.feedbackId,
                    it.imageId,
                    it.mediaNumber
                )
            } else if ((it.videoId.isNotBlank() && it.videoId.toLongOrNull() == null) || it.videoId.toLongOrZero().isMoreThanZero()) {
                getReviewGalleryVideoThumbnailUiModelBasedOnDetail(
                    productrevGetReviewImage.detail,
                    it.feedbackId,
                    it.videoId,
                    it.mediaNumber
                )
            } else null
        }
    }

    private fun getReviewGalleryImageThumbnailUiModelBasedOnDetail(
        detail: Detail,
        feedbackId: String,
        attachmentId: String,
        mediaNumber: Int
    ): ReviewGalleryImageThumbnailUiModel {
        var reviewGalleryImageThumbnailUiModel = ReviewGalleryImageThumbnailUiModel()
        detail.reviewDetail.firstOrNull { it.feedbackId == feedbackId }?.apply {
            reviewGalleryImageThumbnailUiModel = reviewGalleryImageThumbnailUiModel.copy(
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
            reviewGalleryImageThumbnailUiModel = reviewGalleryImageThumbnailUiModel.copy(
                mediaUrl = this.fullsizeURL,
                thumbnailUrl = this.fullsizeURL,
                attachmentId = this.attachmentId
            )
        }
        reviewGalleryImageThumbnailUiModel = reviewGalleryImageThumbnailUiModel.copy(
            feedbackId = feedbackId,
            mediaNumber = mediaNumber
        )
        return reviewGalleryImageThumbnailUiModel
    }

    private fun getReviewGalleryVideoThumbnailUiModelBasedOnDetail(
        detail: Detail,
        feedbackId: String,
        attachmentId: String,
        mediaNumber: Int
    ): ReviewGalleryVideoThumbnailUiModel {
        var reviewGalleryVideoThumbnailUiModel = ReviewGalleryVideoThumbnailUiModel()
        detail.reviewDetail.firstOrNull { it.feedbackId == feedbackId }?.apply {
            reviewGalleryVideoThumbnailUiModel = reviewGalleryVideoThumbnailUiModel.copy(
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
        detail.reviewGalleryVideos.firstOrNull { it.attachmentId == attachmentId }?.apply {
            reviewGalleryVideoThumbnailUiModel = reviewGalleryVideoThumbnailUiModel.copy(
                thumbnailUrl = this.url,
                mediaUrl = this.url,
                attachmentId = this.attachmentId
            )
        }
        reviewGalleryVideoThumbnailUiModel = reviewGalleryVideoThumbnailUiModel.copy(
            feedbackId = feedbackId,
            mediaNumber = mediaNumber
        )
        return reviewGalleryVideoThumbnailUiModel
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

    private fun goToMediaPreview(reviewGalleryMediaThumbnailUiModel: ReviewGalleryMediaThumbnailUiModel) {
        ReviewMediaGalleryRouter.routeToReviewMediaGallery(
            context = requireContext(),
            productID = viewModel.getProductId(),
            shopID = viewModel.getShopId(),
            isProductReview = true,
            isFromGallery = true,
            mediaPosition = reviewGalleryMediaThumbnailUiModel.mediaNumber,
            showSeeMore = false,
            preloadedDetailedReviewMediaResult = viewModel.concatenatedReviewImages.value
        ).also { startActivityForResult(it, IMAGE_PREVIEW_ACTIVITY_CODE) }
    }

    private fun isFirstPage(): Boolean {
        return currentPage + 1 == defaultInitialPage
    }
}