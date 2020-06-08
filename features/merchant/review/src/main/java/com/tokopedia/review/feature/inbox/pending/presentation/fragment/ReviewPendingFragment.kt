package com.tokopedia.review.feature.inbox.pending.presentation.fragment

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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.pending.data.ReviewPendingViewState
import com.tokopedia.review.feature.inbox.pending.data.mapper.ReviewPendingMapper
import com.tokopedia.review.feature.inbox.pending.di.DaggerReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.di.ReviewPendingComponent
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapter
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapterTypeFactory
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.feature.inbox.pending.presentation.viewmodel.ReviewPendingViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_review_pending.*
import kotlinx.android.synthetic.main.partial_review_pending_connection_error.*
import kotlinx.android.synthetic.main.partial_review_pending_empty.*
import javax.inject.Inject

class ReviewPendingFragment : BaseListFragment<ReviewPendingUiModel, ReviewPendingAdapterTypeFactory>(),
        ReviewPendingItemListener, HasComponent<ReviewPendingComponent> {

    companion object {
        const val REVIEW_PENDING_NO_PRODUCTS_BOUGHT_IMAGE = "https://ecs7.tokopedia.net/android/others/review_inbox_no_products.png"
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
        return ""
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

    override fun onCardClicked(reputationId: Int, productId: String) {
        goToCreateReviewActivity(reputationId, productId)
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
        showLoading()
        observeReviewList()
        observeReviewViewState()
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
        removeObservers(viewModel.reviewViewState)
    }

    private fun initView() {
        setupErrorPage()
        setupEmptyState()
    }

    private fun setupErrorPage() {
        reviewPendingConnectionErrorRetryButton.setOnClickListener {
            getPendingReviewData(ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE)
        }
        reviewPendingConnectionErrorGoToSettingsButton.setOnClickListener {
            goToSettings()
        }
    }

    private fun setupEmptyState() {
        reviewPendingEmptyImage.loadImage(REVIEW_PENDING_NO_PRODUCTS_BOUGHT_IMAGE)
        reviewPendingEmptyTitle.text = getString(R.string.review_pending_no_product_empty_title)
        reviewPendingEmptySubtitle.text = getString(R.string.review_pending_no_product_empty_content)
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

    private fun showErrorToaster(errorMessage: String, ctaText: String, action: () -> Unit) {
        view?.let { Toaster.build(it, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, ctaText, View.OnClickListener { action() }) }
    }

    private fun getPendingReviewData(page: Int) {
        viewModel.getReviewData(page)
    }

    private fun observeReviewList() {
        viewModel.reviewList.observe(this, Observer {
            when(it) {
                is Success -> {
                    renderReviewData(ReviewPendingMapper.mapProductRevWaitForFeedbackResponseToReviewPendingUiModel(
                            it.data.list
                    ), it.data.hasNext)
                }
            }
        })
    }

    private fun observeReviewViewState() {
        viewModel.reviewViewState.observe(this, Observer {
            when(it) {
                is ReviewPendingViewState.ReviewPendingInitialLoadError -> {
                    showError()
                    hideList()
                    hideEmptyState()
                }
                is ReviewPendingViewState.ReviewPendingLazyLoadError -> {
                    showErrorToaster(getString(R.string.review_pending_lazy_load_network_error_toaster), getString(R.string.review_pending_lazy_load_network_error_toaster_refresh)) { getPendingReviewData(currentPage) }
                }
                is ReviewPendingViewState.ReviewPendingLoading -> {
                    showLoading()
                    hideError()
                    hideEmptyState()
                }
                is ReviewPendingViewState.ReviewPendingSuccess -> {
                    if(it.isEmpty && it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE) {
                        showEmptyState()
                    } else {
                        showList()
                    }
                    hideError()
                }
            }
        })
    }

    private fun renderReviewData(reviewData: List<ReviewPendingUiModel>, hasNextPage: Int) {
        renderList(reviewData, hasNextPage == 1)
    }

    private fun goToCreateReviewActivity(reputationId: Int, productId: String) {
        RouteManager.route(context, ApplinkConstInternalMarketplace.CREATE_REVIEW, reputationId.toString(), productId)
    }

    private fun goToSettings() {
        RouteManager.route(context, ApplinkConstInternalGlobal.GENERAL_SETTING)
    }

}