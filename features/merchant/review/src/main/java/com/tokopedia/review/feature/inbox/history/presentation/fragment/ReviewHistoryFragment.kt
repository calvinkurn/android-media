package com.tokopedia.review.feature.inbox.history.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.presentation.util.ReviewAttachedImagesClickedListener
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.history.di.DaggerReviewHistoryComponent
import com.tokopedia.review.feature.inbox.history.di.ReviewHistoryComponent
import com.tokopedia.review.feature.inbox.history.presentation.adapter.ReviewHistoryAdapterTypeFactory
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel
import com.tokopedia.review.feature.inbox.history.presentation.util.SearchListener
import com.tokopedia.review.feature.inbox.history.presentation.util.SearchTextWatcher
import com.tokopedia.review.feature.inbox.history.presentation.viewmodel.ReviewHistoryViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_review_history.*
import kotlinx.android.synthetic.main.partial_review_empty.view.*
import javax.inject.Inject

class ReviewHistoryFragment : BaseListFragment<ReviewHistoryUiModel, ReviewHistoryAdapterTypeFactory>(),
        HasComponent<ReviewHistoryComponent>, ReviewAttachedImagesClickedListener, SearchListener {

    companion object {
        fun createNewInstance() : ReviewHistoryFragment {
            return ReviewHistoryFragment()
        }
    }

    @Inject
    lateinit var viewModel: ReviewHistoryViewModel

    override fun getComponent(): ReviewHistoryComponent? {
        return activity?.run {
            DaggerReviewHistoryComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
                    .build()
        }
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return reviewHistoryRecyclerView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchBar()
        observeReviewList()
    }

    override fun getAdapterTypeFactory(): ReviewHistoryAdapterTypeFactory {
        return ReviewHistoryAdapterTypeFactory(this)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(t: ReviewHistoryUiModel?) {
        t?.let {
            goToReviewDetails(it.productrevFeedbackHistory.review.feedbackId, it.productrevFeedbackHistory.reputationId)
        }
    }

    override fun loadData(page: Int) {
        viewModel.updatePage(page)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers(viewModel.reviewList)
    }

    override fun getDefaultInitialPage(): Int {
        return ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return reviewHistorySwipeRefresh
    }

    override fun onAttachedImagesClicked(productName: String, attachedImages: List<String>, position: Int) {
        goToImagePreview(productName, attachedImages, position)
    }

    override fun onSearchTextChanged(text: String) {
        viewModel.updateKeyWord(text)
    }

    private fun initSearchBar() {
        reviewHistorySearchBar.searchBarTextField.apply {
            addTextChangedListener(SearchTextWatcher(searchTextView = this, searchListener = this@ReviewHistoryFragment))
        }
    }

    private fun showErrorToaster(errorMessage: String, ctaText: String, action: () -> Unit) {
        view?.let { Toaster.build(it, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, ctaText, View.OnClickListener { action() }).show() }
    }

    private fun observeReviewList() {
        viewModel.reviewList.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    hidePageLoading()
                    hideError()
                    if(it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE && it.data.list.isEmpty() && it.search.isNotEmpty()) {
                        showEmptySearchResult()
                        return@Observer
                    }
                    if(it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE && it.data.list.isEmpty()) {
                        showNoProductEmpty()
                        return@Observer
                    }
                    renderReviewData(it.data.list.map { history -> ReviewHistoryUiModel(history) }, it.data.hasNext)

                }
                is LoadingView -> {
                    showPageLoading()
                    hideEmptyState()
                    hideError()
                }
                is Fail -> {
                    if(it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE) {
                        hidePageLoading()
                        hideEmptyState()
                        showError()
                    } else {
                        showErrorToaster(getString(R.string.review_toaster_page_error), getString(R.string.review_refresh)) {}
                    }
                }
            }
        })
    }

    private fun goToReviewDetails(feedbackId: Int, reputationId: Int) {

    }

    private fun goToImagePreview(productName: String, attachedImages: List<String>, position: Int) {
        startActivity(context?.let { ImagePreviewSliderActivity.getCallingIntent(it, productName, attachedImages, attachedImages, position) })
    }

    private fun showError() {
        reviewHistoryConnectionError.show()
    }

    private fun hideError() {
        reviewHistoryConnectionError.hide()
    }

    private fun showPageLoading() {
        reviewHistoryLoading.show()
    }

    private fun hidePageLoading() {
        reviewHistoryLoading.hide()
    }

    private fun renderReviewData(reviewData: List<ReviewHistoryUiModel>, hasNextPage: Boolean) {
        reviewHistorySwipeRefresh.show()
        renderList(reviewData, hasNextPage)
    }

    private fun showEmptySearchResult() {
        reviewHistoryEmpty.apply {
            reviewEmptyImage.loadImage(ReviewInboxConstants.REVIEW_INBOX_NO_PRODUCTS_SEARCH_IMAGE)
            reviewEmptyTitle.text = getString(R.string.review_history_no_review_history_title)
            reviewEmptySubtitle.text = getString(R.string.review_history_no_product_search_content)
            show()
        }
        reviewHistorySwipeRefresh.hide()
    }

    private fun showNoProductEmpty() {
        reviewHistoryEmpty.apply {
            reviewEmptyImage.loadImage(ReviewInboxConstants.REVIEW_INBOX_NO_PRODUCTS_BOUGHT_IMAGE)
            reviewEmptyTitle.text = getString(R.string.review_history_no_product_search_result_title)
            reviewEmptySubtitle.text = getString(R.string.review_history_no_product_search_content)
            show()
        }
        reviewHistorySwipeRefresh.hide()
    }

    private fun hideEmptyState() {
        reviewHistoryEmpty.hide()
    }


}