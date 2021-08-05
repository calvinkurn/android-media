package com.tokopedia.review.feature.gallery.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.BuildConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImage
import com.tokopedia.review.feature.gallery.data.ReviewDetail
import com.tokopedia.review.feature.gallery.di.DaggerReviewGalleryComponent
import com.tokopedia.review.feature.gallery.di.ReviewGalleryComponent
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGalleryAdapterTypeFactory
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryUiModel
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryHeaderListener
import com.tokopedia.review.feature.gallery.presentation.viewmodel.ReviewGalleryViewModel
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHeaderListener
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewHeader
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewStatisticsBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.review_layout.*
import javax.inject.Inject

class ReviewGalleryFragment :
    BaseListFragment<ReviewGalleryUiModel, ReviewGalleryAdapterTypeFactory>(),
    HasComponent<ReviewGalleryComponent>, ReviewPerformanceMonitoringContract,
    ReadReviewHeaderListener, ReviewGalleryHeaderListener {

    companion object {
        const val REVIEW_GALLERY_SPAN_COUNT = 2
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

    private var reviewHeader: ReadReviewHeader? = null
    private var statisticsBottomSheet: ReadReviewStatisticsBottomSheet? = null
    private var reviewPerformanceMonitoringListener: ReviewPerformanceMonitoringListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getProductIdFromArguments()
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
        observeReviewImages()
        observeRatingAndTopics()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review_gallery, container, false)
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

    override fun onItemClicked(t: ReviewGalleryUiModel?) {

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

    override fun onSeeAllClicked() {
        goToReadingPage()
    }

    private fun getProductIdFromArguments() {
        viewModel.setProductId(arguments?.getString(ReviewConstants.ARGS_PRODUCT_ID, "") ?: "")
    }

    private fun bindViews(view: View) {
        reviewHeader = view.findViewById(R.id.review_gallery_header)
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
        swipeToRefresh.isRefreshing = false
        renderList(mapToUiModel(productrevGetReviewImage), productrevGetReviewImage.hasNext)
    }

    private fun onFailGetReviewImages(throwable: Throwable) {
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
        val reviewImages = mutableListOf<ReviewGalleryUiModel>()
        productrevGetReviewImage.detail.images.forEachIndexed { index, images ->
            val ratingAndVariant = getRatingAndVariantBasedOnFeedbackID(productrevGetReviewImage.detail.reviewDetail, images.feedbackId)
            reviewImages.add(
                ReviewGalleryUiModel(
                    images.thumbnailURL,
                    ratingAndVariant?.first ?: 0,
                    ratingAndVariant?.second ?: ""
                )
            )
        }
        return reviewImages
    }

    private fun getRatingAndVariantBasedOnFeedbackID(reviews: List<ReviewDetail>, feedbackId: String): Pair<Int, String>? {
        reviews.firstOrNull { it.feedbackId == feedbackId }?.apply {
            return Pair(rating, variantName)
        }
        return null
    }

    private fun getReviewStatistics(): List<ProductReviewDetail> {
        return (viewModel.rating.value as? Success)?.data?.detail ?: listOf()
    }

    private fun getSatisfactionRate(): String {
        return (viewModel.rating.value as? Success)?.data?.satisfactionRate ?: ""
    }

    private fun goToReadingPage() {
        RouteManager.route(context, ApplinkConst.PRODUCT_REVIEW, viewModel.getProductId())
    }
}