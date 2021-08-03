package com.tokopedia.review.feature.gallery.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.review.BuildConfig
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.gallery.data.ProductrevGetReviewImage
import com.tokopedia.review.feature.gallery.presentation.adapter.ReviewGridGalleryAdapterTypeFactory
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGridGalleryUiModel
import com.tokopedia.review.feature.gallery.presentation.di.DaggerReviewGalleryComponent
import com.tokopedia.review.feature.gallery.presentation.di.ReviewGalleryComponent
import com.tokopedia.review.feature.gallery.presentation.viewmodel.ReviewGridGalleryViewModel
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReviewGridGalleryFragment :
    BaseListFragment<ReviewGridGalleryUiModel, ReviewGridGalleryAdapterTypeFactory>(),
    HasComponent<ReviewGalleryComponent>, ReviewPerformanceMonitoringContract {

    companion object {
        const val REVIEW_GALLERY_SPAN_COUNT = 2
        fun createNewInstance(productId: String): ReviewGridGalleryFragment {
            return ReviewGridGalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ReviewConstants.ARGS_PRODUCT_ID, productId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: ReviewGridGalleryViewModel

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
        observeReviewImages()
        observeRatingAndTopics()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review_grid_gallery, container, false)
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

    override fun onItemClicked(t: ReviewGridGalleryUiModel?) {

    }

    override fun loadData(page: Int) {
        getReviewImages(page)
    }

    override fun getAdapterTypeFactory(): ReviewGridGalleryAdapterTypeFactory {
        return ReviewGridGalleryAdapterTypeFactory()
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.review_gallery_recyclerview
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.review_gallery_swipe_refresh
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, REVIEW_GALLERY_SPAN_COUNT)
    }

    private fun getProductIdFromArguments() {
        viewModel.setProductId(arguments?.getString(ReviewConstants.ARGS_PRODUCT_ID, "") ?: "")
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

    private fun onSuccessGetRating(ratingAndTopics: ProductrevGetProductRatingAndTopic) {

    }

    private fun onFailGetRating(throwable: Throwable) {
        logToCrashlytics(throwable)
    }

    private fun onSuccessGetReviewImages(productrevGetReviewImage: ProductrevGetReviewImage) {
        renderList(mapToUiModel(productrevGetReviewImage))
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

    private fun mapToUiModel(productrevGetReviewImage: ProductrevGetReviewImage): List<ReviewGridGalleryUiModel> {
        val reviewImages = mutableListOf<ReviewGridGalleryUiModel>()
        productrevGetReviewImage.detail.reviewDetail.forEachIndexed { index, reviewDetail ->
            reviewImages.add(
                ReviewGridGalleryUiModel(
                    productrevGetReviewImage.detail.images[index].thumbnailURL,
                    reviewDetail.rating,
                    reviewDetail.variantName
                )
            )
        }
        return reviewImages
    }
}