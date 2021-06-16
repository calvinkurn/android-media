package com.tokopedia.review.feature.reading.presentation.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.reading.data.ProductReviewDetail
import com.tokopedia.review.feature.reading.data.ProductTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductRatingAndTopic
import com.tokopedia.review.feature.reading.data.ProductrevGetProductReviewList
import com.tokopedia.review.feature.reading.di.DaggerReadReviewComponent
import com.tokopedia.review.feature.reading.di.ReadReviewComponent
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewAdapterTypeFactory
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewItemListener
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.factory.ReadReviewSortFilterFactory
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterBottomSheetListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewFilterChipsListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHeaderListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewReportBottomSheetListener
import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterType
import com.tokopedia.review.feature.reading.presentation.viewmodel.ReadReviewViewModel
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewFilterBottomSheet
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewHeader
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewReportBottomSheet
import com.tokopedia.review.feature.reading.presentation.widget.ReadReviewStatisticsBottomSheet
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReadReviewFragment : BaseListFragment<ReadReviewUiModel, ReadReviewAdapterTypeFactory>(),
        HasComponent<ReadReviewComponent>, ReadReviewItemListener, ReadReviewHeaderListener,
        ReadReviewFilterChipsListener, ReadReviewFilterBottomSheetListener, ReadReviewReportBottomSheetListener {

    companion object {
        const val MAX_RATING = 5
        const val MIN_RATING = 1
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

    private var reviewHeader: ReadReviewHeader? = null
    private var statisticsBottomSheet: ReadReviewStatisticsBottomSheet? = null
    private var loadingView: View? = null

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
        return ReadReviewAdapterTypeFactory(this)
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
        ReadReviewReportBottomSheet.newInstance(reviewId, shopId, this)
    }

    override fun onLikeButtonClicked(reviewId: String, shopId: String, likeStatus: Int) {
        viewModel.toggleLikeReview(reviewId, shopId, likeStatus)
    }

    override fun onChevronClicked() {
        if (statisticsBottomSheet == null) {
            statisticsBottomSheet = ReadReviewStatisticsBottomSheet.createInstance(getReviewStatistics(), getSatisfactionRate())
        }
        activity?.supportFragmentManager?.let { statisticsBottomSheet?.show(it, ReadReviewStatisticsBottomSheet.READ_REVIEW_STATISTICS_BOTTOM_SHEET_TAG) }
    }

    override fun onFilterWithAttachmentClicked(isActive: Boolean) {
        // update list based on filter
    }

    override fun onFilterWithTopicClicked(topics: List<ProductTopic>, isActive: Boolean) {
        val filterOptions = readReviewFilterFactory.getTopicFilters(topics)
        activity?.supportFragmentManager?.let { ReadReviewFilterBottomSheet.newInstance(getString(R.string.review_reading_topic_filter_title), filterOptions, this, SortFilterType.TopicFilter).show(it, ReadReviewFilterBottomSheet.TAG) }
    }

    override fun onFilterWithRatingClicked(isActive: Boolean) {
        val filterOptions = readReviewFilterFactory.getRatingFilters((MAX_RATING downTo MIN_RATING).map { it.toString() })
        activity?.supportFragmentManager?.let { ReadReviewFilterBottomSheet.newInstance(getString(R.string.review_reading_rating_filter_title), filterOptions, this, SortFilterType.RatingFilter).show(it, ReadReviewFilterBottomSheet.TAG) }
    }

    override fun onFilterSubmitted(selectedFilter: List<ListItemUnify>) {
        // update list based on filter
    }

    override fun onReportOptionClicked(reviewId: String, shopId: String) {
        goToReportReview(reviewId, shopId)
    }

    override fun onSortClicked() {
        val filterOptions = readReviewFilterFactory.getSortOptions(listOf(getString(R.string.review_reading_sort_most_helpful), getString(R.string.review_reading_sort_latest), getString(R.string.review_reading_sort_highest_rating), getString(R.string.review_reading_sort_lowest_rating)))
        activity?.supportFragmentManager?.let { ReadReviewFilterBottomSheet.newInstance(getString(R.string.review_reading_sort_title), filterOptions, this, SortFilterType.Sort).show(it, ReadReviewFilterBottomSheet.TAG) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getProductIdFromArguments()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_read_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        showLoading()
        observeRatingAndTopics()
        observeProductReviews()
        observeToggleLikeReview()
    }

    override fun showLoading() {
        loadingView?.show()
    }

    override fun hideLoading() {
        loadingView?.hide()
    }

    private fun getProductIdFromArguments() {
        viewModel.setProductId(arguments?.getString(ReviewConstants.ARGS_PRODUCT_ID, "") ?: "")
    }

    private fun bindViews(view: View) {
        reviewHeader = view.findViewById(R.id.read_review_header)
        loadingView = view.findViewById(R.id.read_review_loading)
    }

    private fun getProductReview(page: Int) {
        viewModel.setPage(page)
    }

    private fun observeRatingAndTopics() {
        viewModel.ratingAndTopic.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetRatingAndTopic(it.data)
                is Fail -> {
                }
            }
        })
    }

    private fun observeProductReviews() {
        viewModel.productReviews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetProductReviews(it.data)
                is Fail -> {

                }
            }
        })
    }

    private fun observeToggleLikeReview() {
        viewModel.toggleLikeReview.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    // No Op
                }
                is Fail -> {

                }
            }
        })
    }

    private fun onSuccessGetRatingAndTopic(ratingAndTopics: ProductrevGetProductRatingAndTopic) {
        reviewHeader?.apply {
            setRatingData(ratingAndTopics.rating)
            setListener(this@ReadReviewFragment)
            setAvailableFilters(ratingAndTopics.topics, ratingAndTopics.availableFilters, this@ReadReviewFragment)
        }
        hideLoading()
    }

    private fun onSuccessGetProductReviews(productrevGetProductReviewList: ProductrevGetProductReviewList) {
        with(productrevGetProductReviewList) {
            renderList(viewModel.mapProductReviewToReadReviewUiModel(reviewList, shopInfo.shopID, shopInfo.name), hasNext)
        }
    }

    private fun getReviewStatistics(): List<ProductReviewDetail> {
        return viewModel.getReviewStatistics()
    }

    private fun getSatisfactionRate(): String {
        return viewModel.getReviewSatisfactionRate()
    }

    private fun goToReportReview(reviewId: String, shopId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_REVIEW_ID, reviewId)
        intent.putExtra(ApplinkConstInternalMarketplace.ARGS_SHOP_ID, shopId.toIntOrZero())
        startActivity(intent)
    }

}