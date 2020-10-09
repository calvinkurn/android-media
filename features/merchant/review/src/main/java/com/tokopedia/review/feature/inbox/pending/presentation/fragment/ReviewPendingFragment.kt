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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
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
import com.tokopedia.review.feature.createreputation.presentation.activity.CreateReviewActivity
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.common.analytics.ReviewInboxTrackingConstants
import com.tokopedia.review.feature.inbox.pending.analytics.ReviewPendingTracking
import com.tokopedia.review.feature.inbox.pending.data.mapper.ReviewPendingMapper
import com.tokopedia.review.feature.inbox.pending.di.DaggerReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.di.ReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapterTypeFactory
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.feature.inbox.pending.presentation.viewmodel.ReviewPendingViewModel
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.presentation.bottomsheet.IncentiveOvoBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.fragment_review_pending.*
import kotlinx.android.synthetic.main.incentive_ovo_bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.item_incentive_ovo.view.*
import kotlinx.android.synthetic.main.partial_review_connection_error.*
import kotlinx.android.synthetic.main.partial_review_empty.*
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSucess

class ReviewPendingFragment : BaseListFragment<ReviewPendingUiModel, ReviewPendingAdapterTypeFactory>(),
        ReviewPendingItemListener, HasComponent<ReviewPendingComponent>, ReviewPerformanceMonitoringContract {

    companion object {
        const val CREATE_REVIEW_REQUEST_CODE = 420
        fun createNewInstance(): ReviewPendingFragment {
            return ReviewPendingFragment()
        }
    }

    @Inject
    lateinit var viewModel: ReviewPendingViewModel

    private var reviewPerformanceMonitoringListener: ReviewPerformanceMonitoringListener? = null

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

    override fun trackCardClicked(reputationId: Int, productId: Int) {
        ReviewPendingTracking.eventClickCard(reputationId, productId, viewModel.getUserId())
    }

    override fun trackStarsClicked(reputationId: Int, productId: Int, rating: Int) {
        ReviewPendingTracking.eventClickRatingStar(reputationId, productId, rating, viewModel.getUserId())
    }

    override fun onStarsClicked(reputationId: Int, productId: Int, rating: Int, inboxReviewId: Int, seen: Boolean) {
        if (!seen) {
            viewModel.markAsSeen(inboxReviewId)
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
                reviewPendingRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
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
        getIncentiveOvoData()
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
            if (resultCode == Activity.RESULT_OK) {
                onSuccessCreateReview()
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                onFailCreateReview(data?.getStringExtra(ReviewInboxConstants.CREATE_REVIEW_ERROR_MESSAGE)
                        ?: getString(R.string.review_pending_invalid_to_review))
            }
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
        view?.let { Toaster.build(reviewPendingContainer, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, ctaText, View.OnClickListener { action() }).show() }
    }

    private fun showToaster(message: String, ctaText: String) {
        view?.let { Toaster.build(reviewPendingContainer, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, ctaText).show() }
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

    private fun onSuccessGetIncentiveOvo(data: ProductRevIncentiveOvoDomain) {
        data.productrevIncentiveOvo?.ticker?.let {
            ReviewTracking.onSuccessGetIncentiveOvoTracker(it.title, ReviewInboxTrackingConstants.PENDING_TAB)
            it.let {
                ovoPointsTicker.apply {
                    setHtmlDescription(it.subtitle)
                    setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            onClickOvoIncentiveTickerDescription(data)
                        }

                        override fun onDismiss() {
                            onDismissOvoIncentiveTicker(it.title)
                        }
                    })
                    show()
                }
            }
        }
    }

    private fun onErrorGetIncentiveOvo() {
        ovoPointsTicker.hide()
    }

    private fun onClickOvoIncentiveTickerDescription(productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain) {
        val bottomSheet = BottomSheetUnify()
        val view = View.inflate(context, R.layout.incentive_ovo_bottom_sheet_dialog, null)
        bottomSheet.setChild(view)
        activity?.supportFragmentManager?.let { bottomSheet.show(it, bottomSheet.tag) }
        bottomSheet.setCloseClickListener {
            ReviewTracking.onClickDismissIncentiveOvoBottomSheetTracker(ReviewInboxTrackingConstants.PENDING_TAB)
            bottomSheet.dismiss()
        }
        initView(view, productRevIncentiveOvoDomain, bottomSheet)
        ReviewTracking.onClickReadSkIncentiveOvoTracker(productRevIncentiveOvoDomain.productrevIncentiveOvo?.ticker?.title, ReviewInboxTrackingConstants.PENDING_TAB)
    }

    private fun initView(view: View, productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain, bottomSheet: BottomSheetUnify) {
        with(bottomSheet) {
            view.apply {
                tgIncentiveOvoTitle.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.title
                tgIncentiveOvoSubtitle.text = HtmlLinkHelper(context, productRevIncentiveOvoDomain.productrevIncentiveOvo?.subtitle ?: "").spannedString
                incentiveOvoBtnContinueReview.apply {
                    setOnClickListener {
                        dismiss()
                        ReviewTracking.onClickContinueIncentiveOvoBottomSheetTracker(ReviewInboxTrackingConstants.PENDING_TAB)
                    }
                    text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.ctaText
                }
            }

            view.tgIncentiveOvoDescription.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.description

            val adapterIncentiveOvo = ProductRevIncentiveOvoAdapter(productRevIncentiveOvoDomain.productrevIncentiveOvo?.numberedList ?: emptyList())
            view.rvIncentiveOvoExplain.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterIncentiveOvo
            }
            isFullpage = false
            showKnob = true
            showCloseIcon = false
            clearContentPadding = true
        }
    }

    private fun onDismissOvoIncentiveTicker(title: String) {
        ReviewTracking.onClickDismissIncentiveOvoTracker(title, ReviewInboxTrackingConstants.PENDING_TAB)
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

    private fun goToCreateReviewActivity(reputationId: Int, productId: Int, rating: Int, inboxId: String) {
        val intent = RouteManager.getIntent(context,
                Uri.parse(UriUtil.buildUri(ApplinkConstInternalMarketplace.CREATE_REVIEW, reputationId.toString(), productId.toString()))
                        .buildUpon()
                        .appendQueryParameter(CreateReviewActivity.PARAM_RATING, rating.toString())
                        .build()
                        .toString())
        startActivityForResult(intent, CREATE_REVIEW_REQUEST_CODE)
    }

    private inner class ProductRevIncentiveOvoAdapter(private val list: List<String>)
        : RecyclerView.Adapter<ProductRevIncentiveOvoAdapter.ViewHolder>() {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindHero(list[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_incentive_ovo, parent, false))
        }

        override fun getItemCount(): Int = list.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bindHero(explanation: String) {
                itemView.apply {
                    tgIncentiveOvoNumber.text = "${adapterPosition+1}."
                    tgIncentiveOvoExplanation.text = HtmlLinkHelper(context, explanation).spannedString
                }
            }
        }
    }

}