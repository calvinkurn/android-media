package com.tokopedia.reviewseller.feature.inboxreview.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.*
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
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
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

    var itemSortFilterList = ArrayList<SortFilterItem>()

    private var cacheManager: SaveInstanceCacheManager? = null

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

        activity?.finish()
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
        swipeToRefresh?.isRefreshing = false
        sortFilterInboxReview?.show()
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
        itemSortFilterList.addAll(sortFilterItemInboxReviewWrapper.mapIndexed { index, it ->
            SortFilterItem(
                    title = it.sortFilterItem?.title.toString(),
                    type = if (it.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL
            )
        })

        sortFilterInboxReview?.apply {
            addItem(itemSortFilterList)
            dismissListener = {
                resetAllFilterUnselected()
            }
        }

        itemSortFilterList.forEachIndexed { index, sortFilterItem ->
            if (index == 0) {
                sortFilterItem.refChipUnify.setChevronClickListener { }
            }
            sortFilterItem.listener = {
                if (index == 0) {
                    initBottomSheetFilterPeriod()
                } else {
                    updateFilterStatusInboxReview(index)
                }
                sortFilterItem.toggle()
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
        val bottomSheet = FilterRatingBottomSheet(activity, ::onRatingFilterSelected, ::dismissFromRatingFilter)
        inboxReviewViewModel.getRatingFilterListUpdated().let { bottomSheet.setRatingFilterList(it) }
        bottomSheet.showDialog()
    }

    private fun dismissFromRatingFilter() {
        val countSelected = inboxReviewViewModel.getFilterListUpdated().filter { it.isSelected }.count()
        val isZeroSelected = countSelected == 0
        if(isZeroSelected) {
            itemSortFilterList[0].type = ChipsUnify.TYPE_NORMAL
        } else {
            itemSortFilterList[0].type = ChipsUnify.TYPE_SELECTED
        }
    }

    private fun onRatingFilterSelected(filterRatingList: List<ListItemRatingWrapper>) {
        isFilter = true
        inboxReviewAdapter.showLoading()
        inboxReviewAdapter.removeInboxFeedbackError()
        inboxReviewAdapter.removeInboxFeedbackEmpty()
        updatedSortFilterRatingInboxReview(filterRatingList)
        endlessRecyclerViewScrollListener?.resetState()
    }

    private fun updateFilterStatusInboxReview(index: Int) {
        isFilter = true
        val updatedState = itemSortFilterList[index].type == ChipsUnify.TYPE_SELECTED
        sortFilterItemInboxReviewWrapper[index].isSelected = updatedState

        inboxReviewAdapter.removeInboxFeedbackError()
        inboxReviewAdapter.removeInboxFeedbackEmpty()
        inboxReviewAdapter.showLoading()

        inboxReviewViewModel.setFilterAllDataText(sortFilterItemInboxReviewWrapper)
        inboxReviewViewModel.updateStatusFilterData(ArrayList(sortFilterItemInboxReviewWrapper))

        endlessRecyclerViewScrollListener?.resetState()
    }

    private fun updatedSortFilterRatingInboxReview(filterRatingList: List<ListItemRatingWrapper>) {
        val countSelected = filterRatingList.filter { it.isSelected }.count()
        val ratingOneSelected = filterRatingList.firstOrNull()?.sortValue.orEmpty()
        val isAllRating = countSelected == allSelected
        val positionSortFilter = 0

        val updatedState = itemSortFilterList[positionSortFilter].type == ChipsUnify.TYPE_SELECTED

        sortFilterItemInboxReviewWrapper[positionSortFilter].isSelected = updatedState

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
                    refChipUnify.chip_image_icon.setImage(R.drawable.ic_rating_star_item, 0F)
                }
            } else {
                itemSortFilterList[positionSortFilter]?.apply {
                    title = "($countSelected) Filter"
                    refChipUnify.chip_image_icon.hide()
                }
            }
        }

        inboxReviewViewModel.setFilterRatingDataText(filterRatingList)
        inboxReviewViewModel.updateRatingFilterData(ArrayList(filterRatingList))
    }
}