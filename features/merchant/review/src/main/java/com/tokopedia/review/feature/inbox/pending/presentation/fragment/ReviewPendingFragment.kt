package com.tokopedia.review.feature.inbox.pending.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.inboxcommon.InboxFragmentContainer
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.util.ReviewUtil
import com.tokopedia.review.feature.createreputation.presentation.activity.CreateReviewActivity
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.common.analytics.ReviewInboxTrackingConstants
import com.tokopedia.review.feature.inbox.common.presentation.InboxUnifiedRemoteConfig
import com.tokopedia.review.feature.inbox.container.presentation.listener.ReviewInboxListener
import com.tokopedia.review.feature.inbox.pending.analytics.ReviewPendingTracking
import com.tokopedia.review.feature.inbox.pending.data.mapper.ReviewPendingMapper
import com.tokopedia.review.feature.inbox.pending.di.DaggerReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.di.ReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapter
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapterTypeFactory
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingOvoIncentiveUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.feature.inbox.pending.presentation.viewmodel.ReviewPendingViewModel
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoBottomSheetBuilder
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_review_pending.*
import kotlinx.android.synthetic.main.partial_review_connection_error.*
import kotlinx.android.synthetic.main.partial_review_empty.*
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSucess

class ReviewPendingFragment : BaseListFragment<ReviewPendingUiModel, ReviewPendingAdapterTypeFactory>(),
        ReviewPendingItemListener, HasComponent<ReviewPendingComponent>, ReviewPerformanceMonitoringContract,
        IncentiveOvoListener {

    companion object {
        const val CREATE_REVIEW_REQUEST_CODE = 420
        fun createNewInstance(reviewInboxListener: ReviewInboxListener): ReviewPendingFragment {
            return ReviewPendingFragment().apply {
                this.reviewInboxListener = reviewInboxListener
            }
        }
    }

    @Inject
    lateinit var viewModel: ReviewPendingViewModel

    private var reviewPerformanceMonitoringListener: ReviewPerformanceMonitoringListener? = null
    private var ovoIncentiveBottomSheet: BottomSheetUnify? = null
    private var reviewInboxListener: ReviewInboxListener? = null
    private var source: String = ""
    private var containerListener: InboxFragmentContainer? = null

    override fun getAdapterTypeFactory(): ReviewPendingAdapterTypeFactory {
        return ReviewPendingAdapterTypeFactory(this)
    }

    override fun getScreenName(): String {
        return ReviewInboxTrackingConstants.SCREEN_NAME
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(t: ReviewPendingUiModel) {
        // No Op
    }

    override fun loadData(page: Int) {
        getPendingReviewData(page)
    }

    override fun trackCardClicked(reputationId: Long, productId: Long, isEligible: Boolean) {
        ReviewPendingTracking.eventClickCard(reputationId, productId, viewModel.getUserId(), isEligible, source)
    }

    override fun trackStarsClicked(reputationId: Long, productId: Long, rating: Int, isEligible: Boolean) {
        ReviewPendingTracking.eventClickRatingStar(reputationId, productId, rating, viewModel.getUserId(), isEligible, source)
    }

    override fun onStarsClicked(reputationId: Long, productId: Long, rating: Int, inboxReviewId: Long, seen: Boolean) {
        if (!seen) {
            viewModel.markAsSeen(inboxReviewId)
            containerListener?.decreaseReviewUnreviewedCounter()
        }
        goToCreateReviewActivity(reputationId, productId, rating, inboxReviewId.toString())
    }

    override fun getComponent(): ReviewPendingComponent? {
        return activity?.run {
            DaggerReviewPendingComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
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
        reviewPendingRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                reviewPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                reviewPerformanceMonitoringListener?.stopPerformanceMonitoring()
                reviewPendingRecyclerView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        reviewPerformanceMonitoringListener = castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return reviewPendingRecyclerView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        showFullPageLoading()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeReviewList()
        observeOvoIncentive()
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return reviewPendingSwipeRefresh
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers(viewModel.reviewList)
    }

    override fun getDefaultInitialPage(): Int {
        return ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE
    }

    override fun onStart() {
        super.onStart()
        activity?.run {
            ReviewPendingTracking.sendScreen(screenName)
        }
    }

    override fun loadInitialData() {
        clearAllData()
        super.loadInitialData()
        getIncentiveOvoData()
    }

    override fun renderList(list: List<ReviewPendingUiModel>, hasNextPage: Boolean) {
        hideLoading()
        adapter.addElement(list)
        updateScrollListenerState(hasNextPage)
        isLoadingInitialData = false
        if (adapter.dataSize < minimumScrollableNumOfItems && isAutoLoadEnabled
                && hasNextPage && endlessRecyclerViewScrollListener != null) {
            endlessRecyclerViewScrollListener.loadMoreNextPage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_REVIEW_REQUEST_CODE) {
            loadInitialData()
            reviewInboxListener?.reloadCounter()
            if (resultCode == Activity.RESULT_OK) {
                onSuccessCreateReview()
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                onFailCreateReview(data?.getStringExtra(ReviewInboxConstants.CREATE_REVIEW_ERROR_MESSAGE)
                        ?: getString(R.string.review_pending_invalid_to_review))
            }
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<ReviewPendingUiModel, ReviewPendingAdapterTypeFactory> {
        return ReviewPendingAdapter(adapterTypeFactory)
    }

    override fun onUrlClicked(url: String): Boolean {
        context?.let {
            return ReviewUtil.routeToWebview(it, ovoIncentiveBottomSheet, url)
        }
        return false
    }

    override fun onClickCloseThankYouBottomSheet() {
        // No Op
    }

    override fun onClickReviewAnother() {
        // No Op
    }

    override fun onDismissOvoIncentiveTicker(subtitle: String) {
        ReviewTracking.onClickDismissIncentiveOvoTracker(subtitle, ReviewInboxTrackingConstants.PENDING_TAB)
    }

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        reviewInboxListener?.reloadCounter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSourceData()
    }

    override fun onAttachActivity(context: Context?) {
        if (context is InboxFragmentContainer) {
            containerListener = context
        }
    }

    private fun initView() {
        setupErrorPage()
        setupEmptyState()
    }

    private fun setupErrorPage() {
        reviewConnectionErrorRetryButton.setOnClickListener {
            getPendingReviewData(ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE, true)
        }
    }

    private fun setupEmptyState() {
        reviewEmptyImage.loadImage(ReviewInboxConstants.REVIEW_INBOX_NO_PRODUCTS_BOUGHT_IMAGE)
        reviewEmptyTitle.text = getString(R.string.review_pending_no_product_empty_title)
        reviewEmptySubtitle.text = getString(R.string.review_pending_no_product_empty_content)
        reviewEmptyButton.setOnClickListener {
            goToHome()
        }
    }

    private fun showError() {
        reviewPendingConnectionError.show()
    }

    private fun hideError() {
        reviewPendingConnectionError.hide()
    }

    private fun showList() {
        reviewPendingSwipeRefresh.show()
    }

    private fun hideList() {
        reviewPendingSwipeRefresh.hide()
    }

    private fun showEmptyState() {
        reviewPendingEmpty.show()
    }

    private fun hideEmptyState() {
        reviewPendingEmpty.hide()
    }

    private fun showFullPageLoading() {
        reviewPendingLoading.show()
    }

    private fun hideFullPageLoading() {
        reviewPendingLoading.hide()
    }

    private fun showErrorToaster(errorMessage: String, ctaText: String, action: () -> Unit = {}) {
        try {
            view?.let { Toaster.build(it, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, ctaText, View.OnClickListener { action() }).show() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showToaster(message: String, ctaText: String) {
        try {
            view?.let { Toaster.build(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, ctaText).show() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPendingReviewData(page: Int, isRefresh: Boolean = false) {
        viewModel.getReviewData(page, isRefresh)
    }

    private fun getIncentiveOvoData() {
        viewModel.getProductIncentiveOvo()
    }

    private fun observeOvoIncentive() {
        viewModel.incentiveOvo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is CoroutineSucess -> onSuccessGetIncentiveOvo(it.data)
                is CoroutineFail -> onErrorGetIncentiveOvo()
                else -> onSuccessGetIncentiveOvo(null)
            }
        })
    }

    private fun observeReviewList() {
        viewModel.reviewList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    hideFullPageLoading()
                    hideError()
                    hideLoading()
                    if (it.data.list.isEmpty() && it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE) {
                        showEmptyState()
                    } else {
                        renderReviewData(ReviewPendingMapper.mapProductRevWaitForFeedbackResponseToReviewPendingUiModel(
                                it.data.list
                        ), it.data.hasNext)
                    }
                    hideError()
                }
                is LoadingView -> {
                    showFullPageLoading()
                    hideError()
                    hideEmptyState()
                }
                is Fail -> {
                    hideFullPageLoading()
                    if (it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE) {
                        showError()
                        hideEmptyState()
                        hideList()
                    } else {
                        showErrorToaster(getString(R.string.review_toaster_page_error), getString(R.string.review_refresh)) { getPendingReviewData(currentPage) }
                    }
                }
            }
        })
    }

    private fun onSuccessGetIncentiveOvo(data: ProductRevIncentiveOvoDomain?) {
        data?.productrevIncentiveOvo?.ticker?.let {
            (adapter as? ReviewPendingAdapter)?.insertOvoIncentive(ReviewPendingOvoIncentiveUiModel((data)))
        }
    }

    private fun onErrorGetIncentiveOvo() {
        // No Op
    }

    override fun onClickOvoIncentiveTickerDescription(productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain) {
        if (ovoIncentiveBottomSheet == null) {
            ovoIncentiveBottomSheet = context?.let { IncentiveOvoBottomSheetBuilder.getTermsAndConditionsBottomSheet(it, productRevIncentiveOvoDomain, this, ReviewInboxTrackingConstants.PENDING_TAB) }
        }
        ovoIncentiveBottomSheet?.let { bottomSheet ->
            activity?.supportFragmentManager?.let { bottomSheet.show(it, bottomSheet.tag) }
            ReviewTracking.onClickReadSkIncentiveOvoTracker(productRevIncentiveOvoDomain.productrevIncentiveOvo?.ticker?.subtitle, ReviewInboxTrackingConstants.PENDING_TAB)
        }
    }

    private fun onSuccessCreateReview() {
        showToaster(getString(R.string.review_create_success_toaster, viewModel.getUserName()), getString(R.string.review_oke))
    }

    private fun onFailCreateReview(errorMessage: String) {
        showErrorToaster(errorMessage, getString(R.string.review_oke))
    }

    private fun renderReviewData(reviewData: List<ReviewPendingUiModel>, hasNextPage: Boolean) {
        hideEmptyState()
        showList()
        renderList(reviewData, hasNextPage)
    }

    private fun goToCreateReviewActivity(reputationId: Long, productId: Long, rating: Int, inboxId: String) {
        val intent = RouteManager.getIntent(context,
                Uri.parse(UriUtil.buildUri(ApplinkConstInternalMarketplace.CREATE_REVIEW, reputationId.toString(), productId.toString()))
                        .buildUpon()
                        .appendQueryParameter(CreateReviewActivity.PARAM_RATING, rating.toString())
                        .build()
                        .toString())
        startActivityForResult(intent, CREATE_REVIEW_REQUEST_CODE)
    }

    private fun getSourceData() {
        if(InboxUnifiedRemoteConfig.isInboxUnified()) {
            source = containerListener?.getPageSource() ?: ReviewInboxConstants.DEFAULT_SOURCE
            return
        }
        source = arguments?.getString(ReviewInboxConstants.PARAM_SOURCE, ReviewInboxConstants.DEFAULT_SOURCE) ?: ReviewInboxConstants.DEFAULT_SOURCE
    }

    private fun goToHome() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

}