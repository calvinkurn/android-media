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
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig
.RollenceKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.review.ReviewInboxInstance
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.common.analytics.ReviewInboxTrackingConstants
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewPerformanceMonitoringListener
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.presentation.InboxUnifiedRemoteConfig
import com.tokopedia.review.common.util.ReviewInboxUtil
import com.tokopedia.review.feature.inbox.container.presentation.listener.ReviewInboxListener
import com.tokopedia.review.feature.inbox.pending.analytics.ReviewPendingTracking
import com.tokopedia.review.feature.inbox.pending.data.mapper.ReviewPendingMapper
import com.tokopedia.review.feature.inbox.pending.di.DaggerReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.di.ReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapter
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapterTypeFactory
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingEmptyUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingOvoIncentiveUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.feature.inbox.pending.presentation.viewmodel.ReviewPendingViewModel
import com.tokopedia.review.feature.inbox.pending.util.ReviewPendingPreference
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoBottomSheetBuilder
import com.tokopedia.review.feature.ovoincentive.presentation.IncentiveOvoListener
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.FragmentReviewPendingBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import com.tokopedia.usecase.coroutines.Success as CoroutineSucess

class ReviewPendingFragment :
    BaseListFragment<ReviewPendingUiModel, ReviewPendingAdapterTypeFactory>(),
    ReviewPendingItemListener, HasComponent<ReviewPendingComponent>,
    ReviewPerformanceMonitoringContract,
    IncentiveOvoListener {

    companion object {
        const val PARAM_RATING = "rating"
        const val CREATE_REVIEW_REQUEST_CODE = 420
        const val INBOX_SOURCE = "inbox"
        const val COACH_MARK_SHOWN = false
        const val PARAM_WRITE_FORM_SOURCE = "source"
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
    private var reviewPendingPreference: ReviewPendingPreference? = null

    private var binding by autoClearedNullable<FragmentReviewPendingBinding>()

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

    override fun trackCardClicked(reputationId: String, productId: String, isEligible: Boolean) {
        ReviewPendingTracking.eventClickCard(
            reputationId,
            productId,
            viewModel.getUserId(),
            isEligible,
            source
        )
    }

    override fun trackStarsClicked(
        reputationId: String,
        productId: String,
        rating: Int,
        isEligible: Boolean
    ) {
        ReviewPendingTracking.eventClickRatingStar(
            reputationId,
            productId,
            rating,
            viewModel.getUserId(),
            isEligible,
            source
        )
    }

    override fun onStarsClicked(
        reputationId: String,
        productId: String,
        rating: Int,
        inboxReviewId: String,
        seen: Boolean
    ) {
        if (!seen) {
            viewModel.markAsSeen(inboxReviewId)
            containerListener?.decreaseReviewUnreviewedCounter()
        }
        goToCreateReviewActivity(reputationId, productId, rating)
    }

    override fun getComponent(): ReviewPendingComponent? {
        return activity?.run {
            DaggerReviewPendingComponent
                .builder()
                .reviewInboxComponent(ReviewInboxInstance.getComponent(application))
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
        binding?.reviewPendingRecyclerView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                reviewPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                reviewPerformanceMonitoringListener?.stopPerformanceMonitoring()
                binding?.reviewPendingRecyclerView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
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
        reviewPerformanceMonitoringListener =
            castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun getRecyclerView(view: View?): RecyclerView? {
        return binding?.reviewPendingRecyclerView
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewPendingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPrefs()
        initView()
        showFullPageLoading()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeReviewList()
        observeOvoIncentive()
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return binding?.reviewPendingSwipeRefresh
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
            && hasNextPage && endlessRecyclerViewScrollListener != null
        ) {
            endlessRecyclerViewScrollListener.loadMoreNextPage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_REVIEW_REQUEST_CODE) {
            reviewInboxListener?.reloadCounter()
            if (resultCode == Activity.RESULT_OK) {
                onSuccessCreateReview()
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                onFailCreateReview(
                    data?.getStringExtra(ReviewInboxConstants.CREATE_REVIEW_ERROR_MESSAGE)
                        ?: getString(R.string.review_pending_invalid_to_review)
                )
            }
            loadInitialData()
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<ReviewPendingUiModel, ReviewPendingAdapterTypeFactory> {
        return ReviewPendingAdapter(adapterTypeFactory)
    }

    override fun onUrlClicked(url: String): Boolean {
        context?.let {
            return ReviewInboxUtil.routeToWebview(it, ovoIncentiveBottomSheet, url)
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
        ReviewTracking.onClickDismissIncentiveOvoTracker(
            subtitle,
            ReviewInboxTrackingConstants.PENDING_TAB
        )
    }

    override fun onReviewCredibilityWidgetClicked() {
        goToCredibility()
        ReviewPendingTracking.trackOnCredibilityClicked(viewModel.getUserId())
    }

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        reviewInboxListener?.reloadCounter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSourceData()
        loadInitialData()
    }

    override fun onAttachActivity(context: Context?) {
        if (context is InboxFragmentContainer) {
            containerListener = context
        }
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun shouldShowCoachMark(): Boolean {
        return reviewPendingPreference?.isShowCoachMark() ?: false
    }

    override fun updateCoachMark() {
        reviewPendingPreference?.updateSharedPrefs(COACH_MARK_SHOWN)
    }

    private fun initView() {
        setupErrorPage()
        setupEmptyState()
    }

    private fun setupErrorPage() {
        binding?.reviewPendingConnectionError?.reviewConnectionErrorRetryButton?.setOnClickListener {
            getPendingReviewData(ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE, true)
        }
    }

    private fun setupEmptyState() {
        binding?.reviewPendingEmpty?.apply {
            reviewEmptyImage.loadImage(ReviewInboxConstants.REVIEW_INBOX_NO_PRODUCTS_BOUGHT_IMAGE)
            reviewEmptyTitle.text = getString(R.string.review_pending_no_product_empty_title)
            reviewEmptySubtitle.text = getString(R.string.review_pending_no_product_empty_content)
            reviewEmptyButton.setOnClickListener {
                goToHome()
            }
        }
    }

    private fun showError() {
        binding?.reviewPendingConnectionError?.root?.show()
    }

    private fun hideError() {
        binding?.reviewPendingConnectionError?.root?.hide()
    }

    private fun showList() {
        binding?.reviewPendingSwipeRefresh?.show()
    }

    private fun hideList() {
        binding?.reviewPendingSwipeRefresh?.hide()
    }

    private fun showEmptyState() {
        binding?.reviewPendingEmpty?.root?.show()
    }

    private fun hideEmptyState() {
        binding?.reviewPendingEmpty?.root?.hide()
    }

    private fun showFullPageLoading() {
        binding?.reviewPendingLoading?.root?.show()
    }

    private fun hideFullPageLoading() {
        binding?.reviewPendingLoading?.root?.hide()
    }

    private fun showErrorToaster(errorMessage: String, ctaText: String, action: () -> Unit = {}) {
        try {
            view?.let {
                Toaster.build(
                    it, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, ctaText
                ) { action() }.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showToaster(message: String, ctaText: String) {
        try {
            view?.let {
                Toaster.build(
                    it,
                    message,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    ctaText
                ).show()
            }
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
        viewModel.incentiveOvo.observe(viewLifecycleOwner, {
            when (it) {
                is CoroutineSucess -> onSuccessGetIncentiveOvo(it.data)
                is CoroutineFail -> onErrorGetIncentiveOvo()
                else -> onSuccessGetIncentiveOvo(null)
            }
        })
    }

    private fun observeReviewList() {
        viewModel.reviewList.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    hideFullPageLoading()
                    hideError()
                    hideLoading()
                    if (it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE && shouldShowCredibility()) {
                        with(it.data.credibilityWidget) {
                            addCredibilityWidget(imageURL, labelTitle, labelSubtitle)
                        }
                    }
                    if (it.data.list.isEmpty() && it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE) {
                        with(it.data.emptyState) {
                            handleEmptyState(imageURL, labelTitle, labelSubtitle)
                            renderList(listOf(), it.data.hasNext)
                        }
                    } else {
                        renderReviewData(
                            ReviewPendingMapper.mapProductRevWaitForFeedbackResponseToReviewPendingUiModel(
                                it.data.list
                            ), it.data.hasNext
                        )
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
                        showErrorToaster(
                            getString(R.string.review_toaster_page_error),
                            getString(R.string.review_refresh)
                        ) { getPendingReviewData(currentPage) }
                    }
                }
            }
        })
    }

    private fun onSuccessGetIncentiveOvo(data: ProductRevIncentiveOvoDomain?) {
        data?.productrevIncentiveOvo?.ticker?.let {
            (adapter as? ReviewPendingAdapter)?.insertOvoIncentive(
                ReviewPendingOvoIncentiveUiModel(
                    (data)
                )
            )
        }
    }

    private fun onErrorGetIncentiveOvo() {
        // No Op
    }

    override fun onClickOvoIncentiveTickerDescription(productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain) {
        if (ovoIncentiveBottomSheet == null) {
            ovoIncentiveBottomSheet = context?.let {
                IncentiveOvoBottomSheetBuilder.getTermsAndConditionsBottomSheet(
                    it,
                    productRevIncentiveOvoDomain,
                    this,
                    ReviewInboxTrackingConstants.PENDING_TAB
                )
            }
        }
        ovoIncentiveBottomSheet?.let { bottomSheet ->
            activity?.supportFragmentManager?.let { bottomSheet.show(it, bottomSheet.tag) }
            ReviewTracking.onClickReadSkIncentiveOvoTracker(
                productRevIncentiveOvoDomain.productrevIncentiveOvo?.ticker?.subtitle,
                ReviewInboxTrackingConstants.PENDING_TAB
            )
        }
    }

    private fun onSuccessCreateReview() {
        showToaster(
            getString(R.string.review_create_success_toaster, viewModel.getUserName()),
            getString(R.string.review_oke)
        )
    }

    private fun onFailCreateReview(errorMessage: String) {
        showErrorToaster(errorMessage, getString(R.string.review_oke))
    }

    private fun renderReviewData(reviewData: List<ReviewPendingUiModel>, hasNextPage: Boolean) {
        hideEmptyState()
        showList()
        renderList(reviewData, hasNextPage)
    }

    private fun goToCreateReviewActivity(
        reputationId: String,
        productId: String,
        rating: Int
    ) {
        context?.let {
            val intent = RouteManager.getIntent(
                it,
                Uri.parse(
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.CREATE_REVIEW,
                        reputationId,
                        productId
                    )
                )
                    .buildUpon()
                    .appendQueryParameter(PARAM_RATING, rating.toString())
                    .appendQueryParameter(PARAM_WRITE_FORM_SOURCE, INBOX_SOURCE)
                    .build()
                    .toString()
            )
            startActivityForResult(intent, CREATE_REVIEW_REQUEST_CODE)
        }
    }

    private fun getSourceData() {
        if (InboxUnifiedRemoteConfig.isInboxUnified()) {
            source = containerListener?.getPageSource() ?: ReviewInboxConstants.DEFAULT_SOURCE
            if (source.isBlank()) {
                source = ReviewInboxConstants.DEFAULT_SOURCE
            }
            return
        }
        source = arguments?.getString(
            ReviewInboxConstants.PARAM_SOURCE,
            ReviewInboxConstants.DEFAULT_SOURCE
        ) ?: ReviewInboxConstants.DEFAULT_SOURCE
    }

    private fun goToHome() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

    private fun goToCredibility() {
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY,
            viewModel.getUserId(),
            INBOX_SOURCE
        )
    }

    private fun addCredibilityWidget(imageUrl: String, title: String, subtitle: String) {
        (adapter as? ReviewPendingAdapter)?.insertCredibilityWidget(
            ReviewPendingCredibilityUiModel(
                imageUrl,
                title,
                subtitle
            )
        )
    }

    private fun initSharedPrefs() {
        reviewPendingPreference = ReviewPendingPreference(context)
    }

    private fun shouldShowCredibility(): Boolean {
        return isNewCredibilityEnabled()
    }

    private fun isNewCredibilityEnabled(): Boolean {
        getAbTestPlatform()?.let {
            return it.getString(
                RollenceKey.EXPERIMENT_NAME_REVIEW_CREDIBILITY,
                RollenceKey.VARIANT_REVIEW_CREDIBILITY_WITHOUT_BOTTOM_SHEET
            ) == RollenceKey.VARIANT_REVIEW_CREDIBILITY_WITH_BOTTOM_SHEET
        }
        return false
    }

    private fun getAbTestPlatform(): AbTestPlatform? {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform
        } catch (e: java.lang.IllegalStateException) {
            null
        }
    }

    private fun showCredibilityEmptyState(imageUrl: String, title: String, subtitle: String) {
        (adapter as? ReviewPendingAdapter)?.insertEmptyModel(
            ReviewPendingEmptyUiModel(
                imageUrl,
                title,
                subtitle
            )
        )
    }

    private fun handleEmptyState(emptyImageUrl: String, labelTitle: String, labelSubtitle: String) {
        if (shouldShowCredibility()) {
            showCredibilityEmptyState(emptyImageUrl, labelTitle, labelSubtitle)
            showList()
        } else {
            showEmptyState()
        }
    }
}