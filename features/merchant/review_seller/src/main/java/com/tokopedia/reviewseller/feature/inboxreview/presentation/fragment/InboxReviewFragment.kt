package com.tokopedia.reviewseller.feature.inboxreview.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerComponentBuilder
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.ALL_RATINGS
import com.tokopedia.reviewseller.common.util.toggle
import com.tokopedia.reviewseller.feature.inboxreview.di.component.DaggerInboxReviewComponent
import com.tokopedia.reviewseller.feature.inboxreview.di.component.InboxReviewComponent
import com.tokopedia.reviewseller.feature.inboxreview.di.module.InboxReviewModule
import com.tokopedia.reviewseller.feature.inboxreview.domain.mapper.InboxReviewMapper
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.FeedbackInboxReviewListener
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.GlobalErrorStateListener
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.InboxReviewAdapter
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.InboxReviewAdapterTypeFactory
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.ListItemRatingWrapper
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.SortFilterInboxItemWrapper
import com.tokopedia.reviewseller.feature.inboxreview.presentation.viewmodel.InboxReviewViewModel
import com.tokopedia.reviewseller.feature.reviewreply.view.activity.SellerReviewReplyActivity
import com.tokopedia.reviewseller.feature.reviewreply.view.fragment.SellerReviewReplyFragment
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_inbox_review.*
import javax.inject.Inject

class InboxReviewFragment : BaseListFragment<Visitable<*>, InboxReviewAdapterTypeFactory>(), HasComponent<InboxReviewComponent>,
        FeedbackInboxReviewListener, GlobalErrorStateListener {

    companion object {
        fun createInstance(): InboxReviewFragment {
            return InboxReviewFragment()
        }

        private val TAG_FILTER_RATING = FilterRatingBottomSheet::class.java.simpleName
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val inboxReviewViewModel: InboxReviewViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InboxReviewViewModel::class.java)
    }

    private val inboxReviewAdapterTypeFactory by lazy {
        InboxReviewAdapterTypeFactory(this, this)
    }

    private val inboxReviewAdapter: InboxReviewAdapter
        get() = adapter as InboxReviewAdapter

    private val allSelected = 5

    private var isFilter = false

    private var sortFilterItemInboxReviewWrapper = ArrayList<SortFilterInboxItemWrapper>()

    private var itemSortFilterList = ArrayList<SortFilterItem>()

    private var cacheManager: SaveInstanceCacheManager? = null
    private var bottomSheet: FilterRatingBottomSheet? = null

    private var feedbackInboxList: MutableList<FeedbackInboxUiModel>? = null

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
        initRatingFilterList()
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
        sortFilterInboxReview?.hide()
        isLoadingInitialData = true
        endlessRecyclerViewScrollListener?.resetState()
        showLoading()
        inboxReviewViewModel.getInboxReview()
    }

    override fun loadData(page: Int) {
        inboxReviewViewModel.getFeedbackInboxReviewListNext(page)
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): InboxReviewComponent? {
        return activity?.run {
            DaggerInboxReviewComponent
                    .builder()
                    .reviewSellerComponent(ReviewSellerComponentBuilder.getComponent(application))
                    .inboxReviewModule(InboxReviewModule())
                    .build()
        }
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return inboxReviewSwipeToRefresh
    }

    override fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = false
        clearAllData()
        loadInitialData()
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return rvInboxReview
    }

    override fun onItemReplyOrEditClicked(data: FeedbackInboxUiModel, isReply: Boolean, adapterPosition: Int) {
        val feedbackReplyUiModel = ProductReplyUiModel(data.productID, data.productImageUrl, data.productName, data.variantName)
        val mapFeedbackData = InboxReviewMapper.mapFeedbackInboxToFeedbackUiModel(data)

        cacheManager = context?.let {
            SaveInstanceCacheManager(it, true).apply {
                put(SellerReviewReplyFragment.EXTRA_FEEDBACK_DATA, mapFeedbackData)
                put(SellerReviewReplyFragment.EXTRA_PRODUCT_DATA, feedbackReplyUiModel)
            }
        }

        startActivity(Intent(context, SellerReviewReplyActivity::class.java).apply {
            putExtra(SellerReviewReplyFragment.CACHE_OBJECT_ID, cacheManager?.id)
            putExtra(SellerReviewReplyFragment.EXTRA_SHOP_ID, userSession.shopId.orEmpty())
            putExtra(SellerReviewReplyFragment.IS_EMPTY_REPLY_REVIEW, isReply)
        })

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

    override fun onBackgroundMarginIsReplied(isNotReplied: Boolean) {
        val paramsMargin = sortFilterInboxReview.layoutParams as? LinearLayout.LayoutParams
        if(isNotReplied) {
            paramsMargin?.bottomMargin = 16.toPx()
        } else {
            paramsMargin?.bottomMargin = 0
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

    private fun initRatingFilterList() {
        inboxReviewViewModel.updateRatingFilterData(InboxReviewMapper.mapToItemRatingFilterBottomSheet())
    }

    private fun onSuccessGetFeedbackInboxReview(data: InboxReviewUiModel) {
        feedbackInboxList = data.feedbackInboxList.toMutableList()
        swipeToRefresh?.isRefreshing = false
        sortFilterInboxReview?.show()
        if (data.feedbackInboxList.isEmpty() && isFilter && data.page == 1) {
            sortFilterInboxReview?.show()
            inboxReviewAdapter.addInboxFeedbackEmpty(true)
            isFilter = false
        } else if (data.feedbackInboxList.isEmpty() && !isFilter && data.page == 1) {
            sortFilterInboxReview?.hide()
            inboxReviewAdapter.clearAllElements()
            inboxReviewAdapter.addInboxFeedbackEmpty(false)
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
        feedbackInboxList?.clear()
        swipeToRefresh?.isRefreshing = false
        if (feedbackInboxList?.isEmpty() == true) {
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
        sortFilterItemInboxReviewWrapper = ArrayList(inboxReviewViewModel.getStatusFilterListUpdated())
        itemSortFilterList.addAll(sortFilterItemInboxReviewWrapper.mapIndexed { index, it ->
            SortFilterItem(
                    title = it.sortFilterItem?.title.toString(),
                    type = if (it.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL
            )
        })

        sortFilterInboxReview?.apply {
            addItem(itemSortFilterList)
            filterType = SortFilter.TYPE_QUICK
            dismissListener = {
                resetAllFiltersUnselected()
            }
        }

        itemSortFilterList.forEachIndexed { index, sortFilterItem ->
            if (index == 0) {
                sortFilterItem.refChipUnify.setChevronClickListener {
                    initBottomSheetFilterPeriod()
                    sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                }
            }
            sortFilterItem.listener = {
                if (index == 0) {
                    initBottomSheetFilterPeriod()
                    sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                } else {
                    updateFilterStatusInboxReview(index)
                    sortFilterItem.toggle()
                }
            }
        }
    }

    private fun resetAllFiltersUnselected() {
        val positionRatingFilter = 0
        sortFilterInboxReview?.hide()
        inboxReviewAdapter.clearAllElements()
        inboxReviewAdapter.showLoading()
        itemSortFilterList[positionRatingFilter].apply {
            title = ALL_RATINGS
            refChipUnify.chip_image_icon.hide()
        }
        inboxReviewViewModel.resetAllFilter()
        endlessRecyclerViewScrollListener?.resetState()
    }

    private fun initBottomSheetFilterPeriod() {
        bottomSheet = FilterRatingBottomSheet(activity, ::onRatingFilterSelected)
        inboxReviewViewModel.getRatingFilterListUpdated().toList().let { bottomSheet?.setRatingFilterList(it) }
        fragmentManager?.let {
            bottomSheet?.show(it, TAG_FILTER_RATING)
        }
    }


    private fun onRatingFilterSelected(filterRatingList: List<ListItemRatingWrapper>) {
        isFilter = true
        val countSelected = inboxReviewViewModel.getRatingFilterListUpdated().filter { it.isSelected }.count()
        sortFilterInboxReview?.hide()
        inboxReviewAdapter.clearAllElements()
        inboxReviewAdapter.showLoading()
        updatedFilterRatingInboxReview(filterRatingList)
        selectedRatingsFilter(countSelected)
        endlessRecyclerViewScrollListener?.resetState()
        bottomSheet?.dismiss()
    }

    private fun selectedRatingsFilter(countSelected: Int) {
        val positionSortFilter = 0
        val isZeroSelected = countSelected == positionSortFilter
        if (isZeroSelected) {
            itemSortFilterList[positionSortFilter].type = ChipsUnify.TYPE_NORMAL
        } else {
            itemSortFilterList[positionSortFilter].type = ChipsUnify.TYPE_SELECTED
        }
    }

    private fun updateFilterStatusInboxReview(index: Int) {
        isFilter = true
        val updatedState = itemSortFilterList[index].type == ChipsUnify.TYPE_SELECTED
        sortFilterItemInboxReviewWrapper[index].isSelected = !updatedState

        sortFilterInboxReview?.hide()
        inboxReviewAdapter.clearAllElements()
        inboxReviewAdapter.showLoading()

        inboxReviewViewModel.updateStatusFilterData(sortFilterItemInboxReviewWrapper)
        inboxReviewViewModel.setFilterStatusDataText(ArrayList(sortFilterItemInboxReviewWrapper))

        endlessRecyclerViewScrollListener?.resetState()
    }

    private fun updatedFilterRatingInboxReview(filterRatingList: List<ListItemRatingWrapper>) {
        val countSelected = filterRatingList.filter { it.isSelected }.count()
        val ratingOneSelected = filterRatingList.firstOrNull { it.isSelected }?.sortValue.orEmpty()
        val isAllRating = countSelected == allSelected
        val positionSortFilter = 0

        val updatedState = itemSortFilterList[positionSortFilter].type == ChipsUnify.TYPE_SELECTED
        sortFilterItemInboxReviewWrapper[positionSortFilter].isSelected = !updatedState
        itemSortFilterList[positionSortFilter].type = if(updatedState) ChipsUnify.TYPE_NORMAL else ChipsUnify.TYPE_SELECTED

        if (isAllRating) {
            itemSortFilterList[positionSortFilter].apply {
                title = ALL_RATINGS
                refChipUnify.chip_image_icon.hide()
            }
        } else {
            if (countSelected == 0) {
                itemSortFilterList[positionSortFilter].apply {
                    title = ALL_RATINGS
                    refChipUnify.chip_image_icon.hide()
                }
            } else if (countSelected == 1) {
                itemSortFilterList[positionSortFilter].apply {
                    title = ratingOneSelected
                    refChipUnify.chip_image_icon.show()
                    refChipUnify.chip_image_icon.setImage(R.drawable.ic_filter_rating, 0F)
                }
            } else {
                itemSortFilterList[positionSortFilter].apply {
                    title = "($countSelected) Filter"
                    refChipUnify.chip_image_icon.hide()
                }
            }
        }

        inboxReviewViewModel.updateRatingFilterData(ArrayList(filterRatingList))
        inboxReviewViewModel.setFilterRatingDataText(filterRatingList)
    }
}