package com.tokopedia.review.feature.inbox.history.presentation.fragment

import android.net.Uri
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
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.ReviewInboxInstance
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.util.ReviewAttachedImagesClickListener
import com.tokopedia.review.feature.inbox.history.analytics.ReviewHistoryTracking
import com.tokopedia.review.feature.inbox.history.analytics.ReviewHistoryTrackingConstants
import com.tokopedia.review.feature.inbox.history.di.DaggerReviewHistoryComponent
import com.tokopedia.review.feature.inbox.history.di.ReviewHistoryComponent
import com.tokopedia.review.feature.inbox.history.presentation.adapter.ReviewHistoryAdapterTypeFactory
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel
import com.tokopedia.review.feature.inbox.history.presentation.util.ReviewHistoryItemListener
import com.tokopedia.review.feature.inbox.history.presentation.util.SearchListener
import com.tokopedia.review.feature.inbox.history.presentation.util.SearchTextWatcher
import com.tokopedia.review.feature.inbox.history.presentation.viewmodel.ReviewHistoryViewModel
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.FragmentReviewHistoryBinding
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ReviewHistoryFragment :
    BaseListFragment<ReviewHistoryUiModel, ReviewHistoryAdapterTypeFactory>(),
    HasComponent<ReviewHistoryComponent>, ReviewAttachedImagesClickListener, SearchListener,
    ReviewHistoryItemListener {

    companion object {
        fun createNewInstance(): ReviewHistoryFragment {
            return ReviewHistoryFragment()
        }
    }

    @Inject
    lateinit var viewModel: ReviewHistoryViewModel

    private var binding by autoClearedNullable<FragmentReviewHistoryBinding>()

    override fun getComponent(): ReviewHistoryComponent? {
        return activity?.run {
            DaggerReviewHistoryComponent
                .builder()
                .reviewInboxComponent(ReviewInboxInstance.getComponent(application))
                .build()
        }
    }

    override fun getRecyclerView(view: View?): RecyclerView? {
        return binding?.reviewHistoryRecyclerView?.apply {
            itemAnimator = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchBar()
        observeReviewList()
        setupErrorPage()
    }

    override fun getAdapterTypeFactory(): ReviewHistoryAdapterTypeFactory {
        return ReviewHistoryAdapterTypeFactory(
            imagesClickListener = this,
            reviewHistoryItemListener = this,
            reviewMediaTemplateRecycledViewPool = RecyclerView.RecycledViewPool()
        )
    }

    override fun getScreenName(): String {
        return ReviewHistoryTrackingConstants.HISTORY_SCREEN_NAME
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(t: ReviewHistoryUiModel?) {
        t?.let {
            with(it.productrevFeedbackHistory) {
                ReviewHistoryTracking.eventClickReviewCard(
                    viewModel.getUserId(),
                    product.productId,
                    review.feedbackId
                )
            }
            goToReviewDetails(it.productrevFeedbackHistory.review.feedbackId)
        }
    }

    override fun loadData(page: Int) {
        viewModel.updatePage(page)
    }

    override fun onStart() {
        super.onStart()
        activity?.run {
            ReviewHistoryTracking.sendScreen(screenName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers(viewModel.reviewList)
    }

    override fun getDefaultInitialPage(): Int {
        return ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return binding?.reviewHistorySwipeRefresh
    }

    override fun onAttachedImagesClicked(
        productName: String,
        attachedImages: List<String>,
        position: Int
    ) {
        goToImagePreview(productName, attachedImages, position)
    }

    override fun onSearchTextChanged(text: String) {
        if (text.isNotEmpty()) {
            ReviewHistoryTracking.eventSearch(viewModel.getUserId(), text)
        }
        clearAllData()
        viewModel.updateKeyWord(text)
    }

    override fun trackAttachedImageClicked(productId: String?, feedbackId: String?) {
        if (productId != null && feedbackId != null) {
            ReviewHistoryTracking.eventClickImageGallery(
                viewModel.getUserId(),
                productId,
                feedbackId
            )
        }
    }

    private fun initSearchBar() {
        binding?.reviewHistorySearchBar?.searchBarTextField?.apply {
            addTextChangedListener(
                SearchTextWatcher(
                    searchTextView = this,
                    searchListener = this@ReviewHistoryFragment
                )
            )
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    ReviewHistoryTracking.eventClickSearchBar(viewModel.getUserId())
                }
            }
        }
    }

    private fun showErrorToaster(errorMessage: String, ctaText: String, action: () -> Unit) {
        view?.let {
            Toaster.build(
                it,
                errorMessage,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                ctaText
            ) { action() }.show()
        }
    }

    private fun observeReviewList() {
        viewModel.reviewList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    hideLoading()
                    hidePageLoading()
                    hideError()
                    if (it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE && it.data.list.isEmpty() && it.search.isNotBlank()) {
                        showEmptySearchResult()
                        return@Observer
                    }
                    if (it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE && it.data.list.isEmpty()) {
                        showNoProductEmpty()
                        return@Observer
                    }
                    renderReviewData(
                        it.data.list.map { history ->
                            val mappedMediaThumbnail = ReviewMediaThumbnailUiModel(
                                mediaThumbnails = history.review.attachments.map {
                                    ReviewMediaImageThumbnailUiModel(
                                        uiState = ReviewMediaImageThumbnailUiState.Showing(
                                            uri = it.fullSize, removable = false
                                        )
                                    )
                                }
                            )
                            ReviewHistoryUiModel(history, mappedMediaThumbnail)
                        },
                        it.data.hasNext
                    )
                }
                is LoadingView -> {
                    showPageLoading()
                    hideList()
                    hideEmptyState()
                    hideError()
                }
                is Fail -> {
                    if (it.page == ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE) {
                        hidePageLoading()
                        hideEmptyState()
                        hideList()
                        showError()
                    } else {
                        showErrorToaster(
                            getString(R.string.review_toaster_page_error),
                            getString(R.string.review_refresh)
                        ) {}
                    }
                }
            }
        })
    }

    private fun goToReviewDetails(feedbackId: String) {
        RouteManager.route(
            context,
            Uri.parse(UriUtil.buildUri(ApplinkConstInternalMarketplace.REVIEW_DETAIL, feedbackId))
                .buildUpon().toString()
        )
    }

    private fun goToImagePreview(productName: String, attachedImages: List<String>, position: Int) {
        startActivity(context?.let {
            ImagePreviewSliderActivity.getCallingIntent(
                it,
                productName,
                attachedImages,
                attachedImages,
                position
            )
        })
    }

    private fun showError() {
        binding?.reviewHistoryConnectionError?.root?.show()
    }

    private fun hideError() {
        binding?.reviewHistoryConnectionError?.root?.hide()
    }

    private fun showPageLoading() {
        binding?.reviewHistoryLoading?.root?.show()
    }

    private fun hidePageLoading() {
        binding?.reviewHistoryLoading?.root?.hide()
    }

    private fun showList() {
        binding?.reviewHistorySearchBar?.show()
        binding?.reviewHistorySwipeRefresh?.show()
    }

    private fun hideList() {
        binding?.reviewHistorySearchBar?.hide()
        binding?.reviewHistorySwipeRefresh?.hide()
    }

    private fun renderReviewData(reviewData: List<ReviewHistoryUiModel>, hasNextPage: Boolean) {
        showList()
        renderList(reviewData, hasNextPage)
    }

    private fun showEmptySearchResult() {
        binding?.reviewHistorySearchBar?.show()
        binding?.reviewHistoryEmpty?.apply {
            reviewEmptyImage.loadImage(ReviewInboxConstants.REVIEW_INBOX_NO_PRODUCTS_SEARCH_IMAGE)
            reviewEmptyTitle.text =
                getString(R.string.review_history_no_product_search_result_title)
            reviewEmptySubtitle.text = getString(R.string.review_history_no_product_search_content)
            reviewEmptyButton.hide()
            root.show()
        }
        binding?.reviewHistorySwipeRefresh?.hide()
    }

    private fun showNoProductEmpty() {
        binding?.reviewHistoryEmpty?.apply {
            reviewEmptyImage.loadImage(ReviewInboxConstants.REVIEW_INBOX_NO_PRODUCTS_BOUGHT_IMAGE)
            reviewEmptyTitle.text = getString(R.string.review_history_no_review_history_title)
            reviewEmptySubtitle.text = getString(R.string.review_history_no_review_history_content)
            reviewEmptyButton.hide()
            root.show()
        }
        binding?.reviewHistorySearchBar?.hide()
        binding?.reviewHistorySwipeRefresh?.hide()
    }

    private fun hideEmptyState() {
        binding?.reviewHistoryEmpty?.root?.hide()
    }

    private fun setupErrorPage() {
        binding?.reviewHistoryConnectionError?.reviewConnectionErrorRetryButton?.setOnClickListener {
            loadInitialData()
        }
    }
}