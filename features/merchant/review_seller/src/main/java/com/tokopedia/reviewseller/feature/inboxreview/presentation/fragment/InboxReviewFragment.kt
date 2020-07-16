package com.tokopedia.reviewseller.feature.inboxreview.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerComponentBuilder
import com.tokopedia.reviewseller.common.di.component.DaggerReviewSellerComponent
import com.tokopedia.reviewseller.common.di.component.ReviewSellerComponent
import com.tokopedia.reviewseller.common.di.module.ReviewSellerModule
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.ALL_RATINGS
import com.tokopedia.reviewseller.common.util.toggle
import com.tokopedia.reviewseller.feature.inboxreview.domain.mapper.InboxReviewMapper
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.*
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.*
import com.tokopedia.reviewseller.feature.inboxreview.presentation.viewmodel.InboxReviewViewModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_inbox_review.*
import kotlinx.android.synthetic.main.item_filter_inbox_review.*
import javax.inject.Inject

class InboxReviewFragment : BaseListFragment<Visitable<*>, InboxReviewAdapterTypeFactory>(), HasComponent<ReviewSellerComponent>,
        FeedbackInboxReviewListener, GlobalErrorStateListener {

    companion object {
        fun createInstance(): InboxReviewFragment {
            return InboxReviewFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val inboxReviewViewModel: InboxReviewViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InboxReviewViewModel::class.java)
    }

    private val inboxReviewAdapterTypeFactory by lazy {
        InboxReviewAdapterTypeFactory( this, this)
    }

    private val inboxReviewAdapter: InboxReviewAdapter
        get() = adapter as InboxReviewAdapter

    private val allSelected = 5

    private var isFilter = false

    private var sortFilterItemInboxReviewWrapper = ArrayList<SortFilterInboxItemWrapper>()

    override fun getScreenName(): String {
        return getString(R.string.titlte_inbox_review)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inbox_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeInboxReview()
        observeFeedbackInboxReview()
        initSortFilterInboxReview()
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
            val appComponent = (activity?.application as? BaseMainApplication)?.baseAppComponent
            DaggerReviewSellerComponent
                    .builder()
                    .reviewSellerModule(ReviewSellerModule())
                    .reviewSellerComponent(appComponent)
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

    override fun getRecyclerView(view: View?): RecyclerView {
        return rvInboxReview
    }

    override fun onItemReplyOrEditClicked(data: FeedbackInboxUiModel, isReply: Boolean, adapterPosition: Int) {

    }

    override fun onImageItemClicked(titleProduct: String, imageUrls: List<String>, thumbnailsUrl: List<String>, feedbackId: String, position: Int) {
        context?.run {
            startActivity(ImagePreviewSliderActivity.getCallingIntent(
                    context = this,
                    title = titleProduct,
                    imageUrls = imageUrls,
                    imageThumbnailUrls = thumbnailsUrl,
                    imagePosition = position
            ))
        }
    }

    override fun onActionGlobalErrorStateClicked() {
        loadInitialData()
    }

    private fun observeInboxReview() {
        observe(inboxReviewViewModel.inboxReview) {
            hideLoading()
            when (it) {
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
            when (it) {
                is Success -> {
                    onSuccessGetFeedbackInboxReviewNext(it.data)
                }
                is Fail -> {
                    onErrorGetInboxReviewData()
                }
            }
        }
    }

    private fun onSuccessGetFeedbackInboxReview(data: InboxReviewUiModel) {
        swipeToRefresh.isRefreshing = false
        if (data.page == 1) {
            if (data.feedbackInboxList.isEmpty() && isFilter) {
                sortFilterInboxReview?.show()
                inboxReviewAdapter.addInboxFeedbackEmpty(true)
                isFilter = false
            } else if (data.feedbackInboxList.isEmpty() && !isFilter) {
                sortFilterInboxReview?.hide()
                inboxReviewAdapter.clearAllElements()
                inboxReviewAdapter.addInboxFeedbackEmpty(false)
            }
        } else {
            inboxReviewAdapter.setFeedbackListData(data.feedbackInboxList)
        }
        updateScrollListenerState(data.hasNext)
    }

    private fun onSuccessGetFeedbackInboxReviewNext(data: InboxReviewUiModel) {
        inboxReviewAdapter.setFeedbackListData(data.feedbackInboxList)
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

    private fun initSortFilterInboxReview() {
        inboxReviewViewModel.updateStatusFilterData(InboxReviewMapper.mapToItemSortFilterIsEmpty())
        sortFilterItemInboxReviewWrapper = ArrayList(inboxReviewViewModel.getFilterListUpdated())
        val itemSortFilterList = ArrayList<SortFilterItem>()
        itemSortFilterList.addAll(sortFilterItemInboxReviewWrapper.mapIndexed { index, it ->
            SortFilterItem(
                    title = it.sortFilterItem?.title.toString(),
                    type = if (it.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL
            ).apply {
                if(index == 0) {
                    refChipUnify.setChevronClickListener {}
                }
            }
        })

        itemSortFilterList.forEachIndexed { index, sortFilterItem ->
            sortFilterItem.listener = {
                if (index == 0) {
                    initBottomSheetFilterPeriod()
                } else {
                    updateFilterStatusInboxReview(index)
                }
                sortFilterItem.toggle()
            }
        }
        sortFilterInboxReview?.apply {
            addItem(itemSortFilterList)
            dismissListener = {
                resetAllFilterUnselected()
            }
        }
    }

    private fun resetAllFilterUnselected() {
        sortFilterItemInboxReviewWrapper.map {
            it.sortFilterItem?.type = ChipsUnify.TYPE_NORMAL
            it.isSelected = false
        }
    }

    private fun initBottomSheetFilterPeriod() {
        val ratingListWrapper = InboxReviewMapper.mapToItemRatingFilterBottomSheet(inboxReviewViewModel.getRatingFilterListUpdated())
        inboxReviewViewModel.updateRatingFilterData(ratingListWrapper)
        val bottomSheet = FilterRatingBottomSheet(activity, ::onRatingFilterSelected)
        inboxReviewViewModel.getRatingFilterListUpdated().let { bottomSheet.setRatingFilterList(it) }
        bottomSheet.showDialog()
    }

    private fun onRatingFilterSelected(filterRatingList: List<ListItemRatingWrapper>) {
        isFilter = true
        updatedSortFilterRatingInboxReview(filterRatingList)
        inboxReviewAdapter.removeInboxFeedbackError()
        inboxReviewAdapter.removeInboxFeedbackEmpty()
        inboxReviewAdapter.showLoading()
        endlessRecyclerViewScrollListener?.resetState()
    }

    private fun updateFilterStatusInboxReview(index: Int) {
        isFilter = true
        val updatedState = sortFilterItemInboxReviewWrapper[index].sortFilterItem?.type == ChipsUnify.TYPE_SELECTED
        sortFilterItemInboxReviewWrapper[index].isSelected = updatedState

        inboxReviewViewModel.setFilterAllDataText(sortFilterItemInboxReviewWrapper)
        inboxReviewViewModel.updateStatusFilterData(ArrayList(sortFilterItemInboxReviewWrapper))

        inboxReviewAdapter.removeInboxFeedbackError()
        inboxReviewAdapter.removeInboxFeedbackEmpty()
        inboxReviewAdapter.showLoading()
        endlessRecyclerViewScrollListener?.resetState()
    }

    private fun updatedSortFilterRatingInboxReview(filterRatingList: List<ListItemRatingWrapper>) {
        val countSelected = filterRatingList.filter { it.isSelected }.count()
        val ratingOneSelected = filterRatingList.firstOrNull()?.sortValue.orEmpty()
        val isAllRating = countSelected == allSelected
        val positionSortFilter = 0

        val updatedState = sortFilterItemInboxReviewWrapper[positionSortFilter].sortFilterItem?.type == ChipsUnify.TYPE_SELECTED

        sortFilterItemInboxReviewWrapper[positionSortFilter].isSelected = updatedState

        if (isAllRating) {
            sortFilterItemInboxReviewWrapper[positionSortFilter].apply {
                sortFilterItem?.title = ALL_RATINGS
                sortFilterItem?.refChipUnify?.chip_image_icon?.hide()
            }
        } else {
            if (countSelected == 0) {
                sortFilterItemInboxReviewWrapper[positionSortFilter].apply {
                    sortFilterItem?.title = ALL_RATINGS
                    sortFilterItem?.refChipUnify?.chip_image_icon?.hide()
                }
            } else if (countSelected == 1) {
                sortFilterItemInboxReviewWrapper[positionSortFilter].apply {
                    sortFilterItem?.also {
                        it.title = ratingOneSelected
                        it.refChipUnify.chip_image_icon.show()
                        it.refChipUnify.chip_image_icon.setImage(R.drawable.ic_rating_star_item, 0F)
                    }
                }
            } else {
                sortFilterItemInboxReviewWrapper[positionSortFilter].sortFilterItem?.apply {
                    title = "($countSelected) Filter"
                    refChipUnify.chip_image_icon.hide()
                }
            }
        }

        inboxReviewViewModel.setFilterRatingDataText(filterRatingList)
        inboxReviewViewModel.updateRatingFilterData(ArrayList(filterRatingList))
    }
}