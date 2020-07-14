package com.tokopedia.reviewseller.feature.inboxreview.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerComponentBuilder
import com.tokopedia.reviewseller.common.di.component.DaggerReviewSellerComponent
import com.tokopedia.reviewseller.common.di.component.ReviewSellerComponent
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.*
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FilterInboxReviewUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.viewmodel.InboxReviewViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InboxReviewFragment: BaseListFragment<Visitable<*>, InboxReviewAdapterTypeFactory>(), HasComponent<ReviewSellerComponent>,
        FilterInboxReviewListener, FeedbackInboxReviewListener, GlobalErrorStateListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val inboxReviewViewModel: InboxReviewViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InboxReviewViewModel::class.java)
    }

    private val inboxReviewAdapterTypeFactory by lazy {
        InboxReviewAdapterTypeFactory(this, this, this)
    }

    private val inboxReviewAdapter: InboxReviewAdapter
        get() = adapter as InboxReviewAdapter

    private var isFilter = false

    private var bottomSheetFilterRating: BottomSheetUnify? = null
    private var filterRatingListUnify: ListUnify? = null
    private var filterRatingButton: UnifyButton? = null

    override fun getScreenName(): String {
        return getString(R.string.titlte_inbox_review)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inbox_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewBottomSheet()
        observeInboxReview()
        observeFeedbackInboxReview()
    }

    override fun onDestroy() {
        removeObservers(inboxReviewViewModel.inboxReview)
        removeObservers(inboxReviewViewModel.feedbackInboxReview)
        super.onDestroy()
    }

    override fun getAdapterTypeFactory(): InboxReviewAdapterTypeFactory {
        return inboxReviewAdapterTypeFactory
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, InboxReviewAdapterTypeFactory> {
        return InboxReviewAdapter(inboxReviewAdapterTypeFactory)
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadInitialData() {
        isLoadingInitialData = true
        endlessRecyclerViewScrollListener?.resetState()
        inboxReviewViewModel.getInboxReview()
    }

    override fun loadData(page: Int) {
        inboxReviewViewModel.getFeedbackInboxReviewListNext(page)
    }

    override fun getComponent(): ReviewSellerComponent? {
        return activity?.run {
            DaggerReviewSellerComponent
                    .builder()
                    .reviewSellerComponent(ReviewSellerComponentBuilder.getComponent(application))
                    .build()
        }
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onSwipeRefresh() {
        swipeToRefresh.isRefreshing = false
        clearAllData()
        loadInitialData()
    }

    override fun onItemReplyOrEditClicked(data: FeedbackInboxUiModel, isReply: Boolean, adapterPosition: Int) {

    }

    override fun onImageItemClicked(imageUrls: List<String>, thumbnailsUrl: List<String>, feedbackId: String, position: Int) {

    }

    override fun onItemFilterRatingClicked(data: FilterInboxReviewUiModel, adapterPosition: Int) {

    }

    override fun onItemFilterStatusClicked(data: FilterInboxReviewUiModel, adapterPosition: Int) {

    }

    override fun onActionGlobalErrorStateClicked() {
        clearAllData()
        loadInitialData()
    }

    private fun observeInboxReview() {
        observe(inboxReviewViewModel.inboxReview) {
            hideLoading()
            when(it) {
                is Success -> {
                    onSuccessGetFeedbackInboxReview(it.data)
                }
                is Fail -> {
                    onErrorGetInboxReviewData()
                }
            }
        }
    }

    private fun observeFeedbackInboxReview() {
        observe(inboxReviewViewModel.feedbackInboxReview) {
            hideLoading()
            when(it) {
                is Success -> {
                    onSuccessGetFeedbackInboxReview(it.data)
                }
                is Fail -> {
                    onErrorGetInboxReviewData()
                }
            }
        }
    }

    private fun onSuccessGetFeedbackInboxReview(data: InboxReviewUiModel) {
        swipeToRefresh.isRefreshing = false
        if(data.page == 1) {
            if (data.feedbackInboxList.isEmpty() && isFilter) {
                inboxReviewAdapter.addInboxFeedbackEmpty(true)
            } else if (data.feedbackInboxList.isEmpty() && !isFilter) {
                inboxReviewAdapter.clearAllElements()
                inboxReviewAdapter.addInboxFeedbackEmpty(false)
            }
        } else {
            inboxReviewAdapter.setFilterInboxReview(data.filterItemWrapper)
            inboxReviewAdapter.setFeedbackListData(data.feedbackInboxList)
        }
        updateScrollListenerState(data.hasNext)
    }

    private fun onSuccessGetFeedbackInboxReviewNext(data: InboxReviewUiModel) {
        if(data.page == 1) {
            if (data.feedbackInboxList.isEmpty() && isFilter) {
                inboxReviewAdapter.addInboxFeedbackEmpty(true)
            } else if (data.feedbackInboxList.isEmpty() && !isFilter) {
                inboxReviewAdapter.clearAllElements()
                inboxReviewAdapter.addInboxFeedbackEmpty(false)
            }
        } else {
            inboxReviewAdapter.setFeedbackListData(data.feedbackInboxList)
        }
        updateScrollListenerState(data.hasNext)
    }

    private fun onErrorGetInboxReviewData() {
        swipeToRefresh?.isRefreshing = false
        if (inboxReviewAdapter.itemCount.isZero()) {
            inboxReviewAdapter.clearAllElements()
            inboxReviewAdapter.addInboxFeedbackError()
        } else {
            onErrorLoadMoreToaster(getString(R.string.error_message_load_more_review_product), getString(R.string.action_retry_toaster_review_product))
        }
    }

    private fun onErrorLoadMoreToaster(message: String, action: String) {
        view?.let {
            Toaster.make(it, message, actionText = action, type = Toaster.TYPE_ERROR, clickListener = View.OnClickListener {
                loadInitialData()
            })
        }
    }

    private fun initViewBottomSheet() {
        val view = View.inflate(context, R.layout.bottom_sheet_filter_rating_inbox_review, null)
        bottomSheetFilterRating = BottomSheetUnify()
        filterRatingListUnify = view.findViewById(R.id.ratingListInboxReview)
        filterRatingButton = view.findViewById(R.id.btnFilter)
        bottomSheetFilterRating?.setChild(view)
    }

}