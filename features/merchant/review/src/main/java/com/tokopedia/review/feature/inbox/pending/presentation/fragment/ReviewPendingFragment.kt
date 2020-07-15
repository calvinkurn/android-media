package com.tokopedia.review.feature.inbox.pending.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.common.analytics.ReviewTrackingConstant
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.feature.createreputation.ui.activity.CreateReviewActivity
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.common.analytics.ReviewInboxTrackingConstants
import com.tokopedia.review.feature.inbox.pending.analytics.ReviewPendingTracking
import com.tokopedia.review.feature.inbox.pending.data.mapper.ReviewPendingMapper
import com.tokopedia.review.feature.inbox.pending.di.DaggerReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.di.ReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapter
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapterTypeFactory
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.feature.inbox.pending.presentation.viewmodel.ReviewPendingViewModel
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.data.mapper.IncentiveOvoMapper
import com.tokopedia.review.feature.ovoincentive.presentation.bottomsheet.IncentiveOvoBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.fragment_create_review.*
import com.tokopedia.usecase.coroutines.Success as CoroutineSucess
import com.tokopedia.usecase.coroutines.Fail as CoroutineFail
import kotlinx.android.synthetic.main.fragment_review_pending.*
import kotlinx.android.synthetic.main.fragment_review_pending.ovoPointsTicker
import kotlinx.android.synthetic.main.partial_review_connection_error.*
import kotlinx.android.synthetic.main.partial_review_empty.*
import javax.inject.Inject

class ReviewPendingFragment : BaseListFragment<ReviewPendingUiModel, ReviewPendingAdapterTypeFactory>(),
        ReviewPendingItemListener, HasComponent<ReviewPendingComponent> {

    companion object {
        fun createNewInstance() : ReviewPendingFragment {
            return ReviewPendingFragment()
        }
    }

    @Inject
    lateinit var viewModel: ReviewPendingViewModel

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
        ReviewPendingTracking.eventClickCard(reputationId, productId, viewModel.userId)
    }

    override fun trackStarsClicked(reputationId: Int, productId: Int, rating: Int) {
        ReviewPendingTracking.eventClickRatingStar(reputationId, productId, rating, viewModel.userId)
    }

    override fun onStarsClicked(reputationId: Int, productId: Int, rating: Int) {

        goToCreateReviewActivity(reputationId, productId, rating)
    }

    override fun getComponent(): ReviewPendingComponent? {
        return activity?.run {
            DaggerReviewPendingComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
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

    override fun createAdapterInstance(): BaseListAdapter<ReviewPendingUiModel, ReviewPendingAdapterTypeFactory> {
        return ReviewPendingAdapter(adapterTypeFactory)
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

    private fun showErrorToaster(errorMessage: String, ctaText: String, action: () -> Unit) {
        view?.let { Toaster.build(it, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, ctaText, View.OnClickListener { action() }).show() }
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
            when(it) {
                is Success -> {
                    hideFullPageLoading()
                    hideError()
                    if(it.data.list.isEmpty() && it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE) {
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
                    if(it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE) {
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
        with(data.productrevIncentiveOvo) {
            ovoPointsTicker.apply {
                show()
                tickerTitle = ticker.title
                setHtmlDescription(ticker.subtitle)
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        val bottomSheet: BottomSheetUnify = IncentiveOvoBottomSheet(IncentiveOvoMapper.mapIncentiveOvoReviewtoIncentiveOvoInbox(data), ReviewInboxTrackingConstants.PENDING_TAB)
                        bottomSheet.isFullpage = true
                        fragmentManager?.let { bottomSheet.show(it, bottomSheet.tag)}
                        bottomSheet.setCloseClickListener {
                            ReviewTracking.onClickDismissIncentiveOvoBottomSheetTracker(ReviewInboxTrackingConstants.PENDING_TAB)
                            bottomSheet.dismiss()
                        }
                        ReviewTracking.onClickReadSkIncentiveOvoTracker(tickerTitle, ReviewInboxTrackingConstants.PENDING_TAB)
                    }

                    override fun onDismiss() {
                        ReviewTracking.onClickDismissIncentiveOvoTracker(tickerTitle, ReviewInboxTrackingConstants.PENDING_TAB)
                    }
                })
                ReviewTracking.onSuccessGetIncentiveOvoTracker(tickerTitle, ReviewInboxTrackingConstants.PENDING_TAB)
            }
        }
    }

    private fun onErrorGetIncentiveOvo() {
        ovoPointsTicker.hide()
    }

    private fun renderReviewData(reviewData: List<ReviewPendingUiModel>, hasNextPage: Boolean) {
        hideEmptyState()
        showList()
        renderList(reviewData, hasNextPage)
    }

    private fun goToCreateReviewActivity(reputationId: Int, productId: Int, rating: Int) {
        RouteManager.route(context,
                Uri.parse(UriUtil.buildUri(ApplinkConstInternalMarketplace.CREATE_REVIEW, reputationId.toString(), productId.toString()))
                        .buildUpon()
                        .appendQueryParameter(CreateReviewActivity.PARAM_RATING, rating.toString())
                        .build()
                        .toString())
    }

}