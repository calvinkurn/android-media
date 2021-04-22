package com.tokopedia.review.feature.inboxreview.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.common.util.ReviewConstants.ALL_RATINGS
import com.tokopedia.review.common.util.ReviewConstants.ANSWERED_VALUE
import com.tokopedia.review.common.util.ReviewConstants.RESULT_INTENT_REVIEW_REPLY
import com.tokopedia.review.common.util.ReviewConstants.UNANSWERED_VALUE
import com.tokopedia.review.common.util.ReviewConstants.prefixStatus
import com.tokopedia.review.common.util.getStatusFilter
import com.tokopedia.review.common.util.isUnAnswered
import com.tokopedia.review.feature.inbox.common.presentation.activity.InboxReputationActivity
import com.tokopedia.review.feature.inbox.common.presentation.listener.OnTabChangeListener
import com.tokopedia.review.feature.inboxreview.analytics.InboxReviewTracking
import com.tokopedia.review.feature.inboxreview.di.component.DaggerInboxReviewComponent
import com.tokopedia.review.feature.inboxreview.di.component.InboxReviewComponent
import com.tokopedia.review.feature.inboxreview.domain.mapper.InboxReviewMapper
import com.tokopedia.review.feature.inboxreview.presentation.adapter.FeedbackInboxReviewListener
import com.tokopedia.review.feature.inboxreview.presentation.adapter.GlobalErrorStateListener
import com.tokopedia.review.feature.inboxreview.presentation.adapter.InboxReviewAdapter
import com.tokopedia.review.feature.inboxreview.presentation.adapter.InboxReviewAdapterTypeFactory
import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.review.feature.inboxreview.presentation.model.InboxReviewUiModel
import com.tokopedia.review.feature.inboxreview.presentation.model.ListItemRatingWrapper
import com.tokopedia.review.feature.inboxreview.presentation.model.SortFilterInboxItemWrapper
import com.tokopedia.review.feature.inboxreview.presentation.viewmodel.InboxReviewViewModel
import com.tokopedia.review.feature.inboxreview.util.InboxReviewPreference
import com.tokopedia.review.feature.reviewreply.view.activity.SellerReviewReplyActivity
import com.tokopedia.review.feature.reviewreply.view.fragment.SellerReviewReplyFragment
import com.tokopedia.review.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_inbox_review.*
import javax.inject.Inject

class InboxReviewFragment : BaseListFragment<Visitable<*>, InboxReviewAdapterTypeFactory>(),
        HasComponent<InboxReviewComponent>,
        FeedbackInboxReviewListener, GlobalErrorStateListener, OnTabChangeListener {

    companion object {
        fun createInstance(): InboxReviewFragment {
            return InboxReviewFragment()
        }

        private val TAG_FILTER_RATING = FilterRatingBottomSheet::class.java.simpleName
        private const val positionRating = 0
        private const val positionUnAnswered = 1
        private const val positionAnswered = 2
        private const val allSelected = 5
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var inboxReviewPreference: InboxReviewPreference

    private val inboxReviewViewModel: InboxReviewViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InboxReviewViewModel::class.java)
    }

    private val inboxReviewAdapterTypeFactory by lazy {
        InboxReviewAdapterTypeFactory(this, this)
    }

    private val inboxReviewAdapter: InboxReviewAdapter
        get() = adapter as InboxReviewAdapter

    private val prefKey = this.javaClass.name + ".pref"

    private var isFilter = false

    private var sortFilterItemInboxReviewWrapper = ArrayList<SortFilterInboxItemWrapper>()

    private var itemSortFilterList = ArrayList<SortFilterItem>()

    private var cacheManager: SaveInstanceCacheManager? = null
    private var bottomSheet: FilterRatingBottomSheet? = null

    private var feedbackInboxList: MutableList<FeedbackInboxUiModel>? = null

    private var inboxReviewUiModel: InboxReviewUiModel? = null

    private var statusFilter = UNANSWERED_VALUE

    private var prefs: SharedPreferences? = null

    private var coachmark: CoachMark2? = null
    private var coachmarkItems: ArrayList<CoachMark2Item> = arrayListOf()

    override fun getScreenName(): String {
        return getString(R.string.title_inbox_review)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = context?.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inbox_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeInboxReviewCounter()
        observeInboxReview()
        observeFeedbackInboxReview()
        observeReviewReminderEstimation()
        initTickerInboxReview()
        initSortFilterInboxReview()
        initRatingFilterList()
        setupMarginSortFilter()
        setupViewInteraction()
        initCoachMark()
    }

    override fun onResume() {
        super.onResume()
        inboxReviewViewModel.getInboxReviewCounter()
        inboxReviewViewModel.fetchReminderCounter()
        InboxReviewTracking.openScreenInboxReview(inboxReviewViewModel.userSession.shopId.orEmpty(),
                inboxReviewViewModel.userSession.userId.orEmpty())
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
        statusFilter = UNANSWERED_VALUE
        endlessRecyclerViewScrollListener?.resetState()
        showLoading()
        if (countStatusIsZero()) {
            inboxReviewViewModel.getInitInboxReview(statusFilter = statusFilter)
        } else {
            inboxReviewViewModel.getInboxReview()
        }
    }

    override fun loadData(page: Int) {
        if (countStatusIsZero()) {
            inboxReviewViewModel.getInitFeedbackInboxReviewListNext(page, statusFilter)
        } else {
            inboxReviewViewModel.getFeedbackInboxReviewListNext(page)
        }
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): InboxReviewComponent? {
        return activity?.run {
            DaggerInboxReviewComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(application))
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

    override fun onItemReplyOrEditClicked(data: FeedbackInboxUiModel, isEmptyReply: Boolean, adapterPosition: Int) {
        if (isEmptyReply) {
            InboxReviewTracking.eventClickReviewNotYetReplied(data.feedbackId.toString(),
                    getQuickFilter(),
                    inboxReviewViewModel.userSession.shopId.orEmpty(),
                    data.productID.toString()
            )
        } else {
            InboxReviewTracking.eventClickReviewReplied(data.feedbackId.toString(),
                    getQuickFilter(),
                    inboxReviewViewModel.userSession.shopId.orEmpty(),
                    data.productID.toString()
            )
        }

        val feedbackReplyUiModel = ProductReplyUiModel(data.productID, data.productImageUrl, data.productName, data.variantName)
        val mapFeedbackData = InboxReviewMapper.mapFeedbackInboxToFeedbackUiModel(data)

        cacheManager = context?.let {
            SaveInstanceCacheManager(it, true).apply {
                put(SellerReviewReplyFragment.EXTRA_FEEDBACK_DATA, mapFeedbackData)
                put(SellerReviewReplyFragment.EXTRA_PRODUCT_DATA, feedbackReplyUiModel)
            }
        }

        startActivityForResult(Intent(context, SellerReviewReplyActivity::class.java).apply {
            putExtra(SellerReviewReplyFragment.CACHE_OBJECT_ID, cacheManager?.id)
            putExtra(SellerReviewReplyFragment.EXTRA_SHOP_ID, inboxReviewViewModel.userSession.shopId.orEmpty())
            putExtra(SellerReviewReplyFragment.IS_EMPTY_REPLY_REVIEW, isEmptyReply)
        }, RESULT_INTENT_REVIEW_REPLY)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RESULT_INTENT_REVIEW_REPLY -> {
                if (resultCode == Activity.RESULT_OK) {
                    clearAllData()
                    loadInitialData()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onImageItemClicked(titleProduct: String, imageUrls: List<String>, thumbnailsUrl: List<String>,
                                    feedbackId: String,
                                    productId: String,
                                    position: Int) {

        InboxReviewTracking.eventClickSpecificImageReview(
                feedbackId,
                getQuickFilter(),
                inboxReviewViewModel.userSession.shopId.orEmpty(),
                productId
        )
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


    override fun onInFullReviewClicked(feedbackId: String, productId: String) {
        InboxReviewTracking.eventClickInFullReview(feedbackId, getQuickFilter(),
                inboxReviewViewModel.userSession.shopId.orEmpty(), productId)
    }

    override fun onBackgroundMarginIsReplied(isNotReplied: Boolean) {
        val paramsMargin = sortFilterInboxReview?.layoutParams as? LinearLayout.LayoutParams
        if (isNotReplied) {
            paramsMargin?.bottomMargin = 8.toPx()
        } else {
            paramsMargin?.bottomMargin = 0
        }
    }

    override fun onActionGlobalErrorStateClicked() {
        loadInitialData()
    }

    override fun showCoachMark(view: View?) {
        context?.let {
            if(isFirstTimeSeeKejarUlasan()) {
                if(coachmarkItems.isEmpty()) {
                    if(view != null) {
                        coachmarkItems = createCoachMarkItems(view)
                    }
                }
                if(coachmarkItems.isNotEmpty()) {
                    coachmark?.showCoachMark(coachmarkItems, null, 0)
                    coachmark?.setOnDismissListener {
                        setFirstTimeSeeKejarUlasan()
                    }
                }
            }
        }
    }

    override fun onTabChange(position: Int) {
        dismissCoachMark()
    }

    override fun hideCoachMark() {
        coachmark?.hideCoachMark()
    }

    private fun dismissCoachMark() {
        coachmark?.dismissCoachMark()
    }

    private fun setupMarginSortFilter() {
        if (tickerInboxReview?.isVisible == false) {
            val sortFilterMargin = sortFilterInboxReview?.layoutParams as? LinearLayout.LayoutParams
            sortFilterMargin?.topMargin = 16.toPx()
        }
    }

    private fun setupViewInteraction(){
        buttonReviewReminder?.setOnClickListener {
            RouteManager.route(context, ApplinkConstInternalMarketplace.REVIEW_REMINDER)
        }
    }

    private fun observeInboxReviewCounter() {
        observe(inboxReviewViewModel.inboxReviewCounterText) {
            when (it) {
                is Success -> {
                    setInboxReviewTabCounter(it.data)
                }
                is Fail -> {
                    setInboxReviewTabCounter()
                }
            }
        }
        inboxReviewViewModel.getInboxReviewCounter()
        inboxReviewViewModel.fetchReminderCounter()
    }

    private fun setInboxReviewTabCounter(counter: Int = 0) {
        (activity as? InboxReputationActivity)?.fragmentList?.forEachIndexed { index, fragment ->
            if (fragment::class.java == this::class.java) {
                    if (counter.isMoreThanZero()) {
                        (activity as? InboxReputationActivity)
                                ?.indicator
                                ?.tabLayout
                                ?.getTabAt(index)
                                ?.setCounter(counter)
                                ?.setNotification(hasNotification = true)
                    }
                return
            }
        }
    }

    private fun setButtonReviewReminder(counter: Int = 0) {
        buttonReviewReminder?.text = if (counter > 0) {
            getString(R.string.review_reminder_button_review_reminder_with_counter, counter)
        } else getString(R.string.review_reminder_button_review_reminder)
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

    private fun observeReviewReminderEstimation() {
        observe(inboxReviewViewModel.getEstimation()) {
            setButtonReviewReminder(it.totalBuyer)
        }
    }

    private fun initRatingFilterList() {
        inboxReviewViewModel.updateRatingFilterData(InboxReviewMapper.mapToItemRatingFilterBottomSheet())
    }

    private fun initTickerInboxReview() {
        prefs?.let {
            if (!it.getBoolean(ReviewConstants.HAS_TICKER_INBOX_REVIEW, false)) {
                tickerInboxReview?.apply {
                    setTextDescription(getString(R.string.ticker_inbox_review))
                    show()
                    setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                        override fun onDismiss() {
                            hide()
                            it.edit().putBoolean(ReviewConstants.HAS_TICKER_INBOX_REVIEW, true).apply()
                        }
                    })
                }
            } else {
                tickerInboxReview.hide()
            }
        }
    }

    private fun onSuccessGetFeedbackInboxReview(data: InboxReviewUiModel) {
        inboxReviewUiModel = data
        feedbackInboxList = data.feedbackInboxList.toMutableList()
        swipeToRefresh?.isRefreshing = false
        sortFilterInboxReview?.show()

        if (isUnAnsweredHasNextFalse(data)) {
            statusFilter = ANSWERED_VALUE

            /**
             * parameter: hasNext = false, action case 1 & 3 is the same.
             * case 1 : if adapter is not empty and data is not empty in unanaswered parameter, request lazy load using answered parameter
             * case 2 : if adapter is empty && data is empty in unanaswered parameter, start the new request using answered parameter
             * case 3 : if adapter is empty && data is not empty in answered parameter, request lazy load using answered parameter
             **/
            if (data.feedbackInboxList.isEmpty()) {
                sortFilterInboxReview?.hide()
                isLoadingInitialData = true
                inboxReviewAdapter.clearAllElements()
                hideLoading()
                inboxReviewViewModel.getInitInboxReview(statusFilter = statusFilter)
            } else {
                endlessRecyclerViewScrollListener?.resetState()
                inboxReviewAdapter.setFeedbackListData(data.feedbackInboxList)
                endlessRecyclerViewScrollListener?.loadMoreNextPage()
            }
        } else {
            if (data.feedbackInboxList.isEmpty() && isFilter && data.page == 1) {
                sortFilterInboxReview?.show()
                inboxReviewAdapter.addInboxFeedbackEmpty(true)
            } else if (data.feedbackInboxList.isEmpty() && !isFilter && data.page == 1) {
                sortFilterInboxReview?.show()
                inboxReviewAdapter.clearAllElements()
                inboxReviewAdapter.addInboxFeedbackEmpty(false)
            } else {
                inboxReviewAdapter.setFeedbackListData(data.feedbackInboxList)
            }
            updateScrollListenerState(data.hasNext)
        }
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
            Toaster.build(it, message, actionText = action, type = Toaster.TYPE_ERROR, clickListener = View.OnClickListener {
                loadInitialData()
            })
        }
    }

    private fun initSortFilterInboxReview() {
        inboxReviewViewModel.updateStatusFilterData(InboxReviewMapper.mapToItemSortFilterIsEmpty())
        sortFilterItemInboxReviewWrapper = ArrayList(inboxReviewViewModel.getStatusFilterListUpdated())
        itemSortFilterList.addAll(sortFilterItemInboxReviewWrapper.mapIndexed { _, it ->
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
            if (index == positionRating) {
                sortFilterItem.refChipUnify.setChevronClickListener {
                    initBottomSheetFilterPeriod()
                    sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                }
            }
            sortFilterItem.listener = {
                if (index == positionRating) {
                    initBottomSheetFilterPeriod()
                    sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                } else {
                    updateFilterStatusInboxReview(index)
                }
            }
        }
    }

    private fun resetAllFiltersUnselected() {
        val positionRatingFilter = 0
        sortFilterInboxReview?.hide()
        isLoadingInitialData = true
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
        InboxReviewTracking.eventClickRatingFilter(inboxReviewViewModel.userSession.shopId.orEmpty())
        bottomSheet = FilterRatingBottomSheet(activity, ::onRatingFilterSelected)
        inboxReviewViewModel.getRatingFilterListUpdated().toList().let { bottomSheet?.setRatingFilterList(it) }
        bottomSheet?.setShopId(inboxReviewViewModel.userSession.shopId.orEmpty())
        fragmentManager?.let {
            bottomSheet?.show(it, TAG_FILTER_RATING)
        }
        dismissCoachMark()
    }


    private fun onRatingFilterSelected(filterRatingList: List<ListItemRatingWrapper>) {
        val countSelected = inboxReviewViewModel.getRatingFilterListUpdated().filter { it.isSelected }.count()
        sortFilterInboxReview?.hide()
        endlessRecyclerViewScrollListener?.resetState()
        updatedFilterRatingInboxReview(filterRatingList)
        selectedRatingsFilter(countSelected)
    }

    private fun selectedRatingsFilter(countSelected: Int) {
        val isZeroSelected = countSelected == positionRating
        if (isZeroSelected) {
            itemSortFilterList[positionRating].type = ChipsUnify.TYPE_NORMAL
        } else {
            itemSortFilterList[positionRating].type = ChipsUnify.TYPE_SELECTED
        }
    }

    private fun updateFilterStatusInboxReview(index: Int) {
        if (index == positionUnAnswered) {
            val unAnsweredSelected = itemSortFilterList[index].type == ChipsUnify.TYPE_SELECTED
            sortFilterItemInboxReviewWrapper[positionUnAnswered].isSelected = !unAnsweredSelected
            sortFilterItemInboxReviewWrapper[positionAnswered].isSelected = false

            itemSortFilterList[positionUnAnswered].type = if (unAnsweredSelected) ChipsUnify.TYPE_NORMAL else ChipsUnify.TYPE_SELECTED
            itemSortFilterList[positionAnswered].type = ChipsUnify.TYPE_NORMAL

            val quickFilter = itemSortFilterList[positionUnAnswered].title.toString()
            val isActive = itemSortFilterList[positionUnAnswered].type == ChipsUnify.TYPE_SELECTED
            InboxReviewTracking.eventClickNotRepliedFilter(quickFilter, isActive.toString(), inboxReviewViewModel.userSession.shopId.orEmpty())
        } else {
            val answeredSelected = itemSortFilterList[index].type == ChipsUnify.TYPE_SELECTED
            sortFilterItemInboxReviewWrapper[positionAnswered].isSelected = !answeredSelected
            sortFilterItemInboxReviewWrapper[positionUnAnswered].isSelected = false

            itemSortFilterList[positionAnswered].type = if (answeredSelected) ChipsUnify.TYPE_NORMAL else ChipsUnify.TYPE_SELECTED
            itemSortFilterList[positionUnAnswered].type = ChipsUnify.TYPE_NORMAL

            val quickFilter = itemSortFilterList[positionAnswered].title.toString()
            val isActive = itemSortFilterList[positionAnswered].type == ChipsUnify.TYPE_SELECTED
            InboxReviewTracking.eventClickHasBeenRepliedFilter(quickFilter, isActive.toString(), inboxReviewViewModel.userSession.shopId.orEmpty())
        }

        sortFilterInboxReview?.hide()
        inboxReviewAdapter.clearAllElements()
        inboxReviewAdapter.showLoading()

        inboxReviewViewModel.updateStatusFilterData(sortFilterItemInboxReviewWrapper)
        inboxReviewViewModel.setFilterStatusDataText(ArrayList(sortFilterItemInboxReviewWrapper))

        endlessRecyclerViewScrollListener?.resetState()
        isFilter = hasFiltered()
        dismissCoachMark()
    }

    private fun updatedFilterRatingInboxReview(filterRatingList: List<ListItemRatingWrapper>) {
        val countSelected = filterRatingList.filter { it.isSelected }.count()
        val ratingOneSelected = filterRatingList.firstOrNull { it.isSelected }?.sortValue.orEmpty()
        val isAllRating = countSelected == allSelected

        val updatedState = itemSortFilterList[positionRating].type == ChipsUnify.TYPE_SELECTED
        sortFilterItemInboxReviewWrapper[positionRating].isSelected = !updatedState
        itemSortFilterList[positionRating].type = if (updatedState) ChipsUnify.TYPE_NORMAL else ChipsUnify.TYPE_SELECTED

        if (isAllRating) {
            itemSortFilterList[positionRating].apply {
                title = ALL_RATINGS
                refChipUnify.chip_image_icon.hide()
            }
        } else {
            if (countSelected == 0) {
                itemSortFilterList[positionRating].apply {
                    title = ALL_RATINGS
                    refChipUnify.chip_image_icon.hide()
                }
            } else if (countSelected == 1) {
                itemSortFilterList[positionRating].apply {
                    title = ratingOneSelected
                    refChipUnify.chip_image_icon.show()
                    refChipUnify.chip_image_icon.setImage(R.drawable.ic_filter_rating, 0F)
                }
            } else {
                itemSortFilterList[positionRating].apply {
                    title = "($countSelected) Filter"
                    refChipUnify.chip_image_icon.hide()
                }
            }
        }

        inboxReviewAdapter.clearAllElements()
        inboxReviewAdapter.showLoading()

        inboxReviewViewModel.updateRatingFilterData(ArrayList(filterRatingList))
        inboxReviewViewModel.setFilterRatingDataText(filterRatingList)

        isFilter = hasFiltered()
    }

    private fun isUnAnsweredHasNextFalse(data: InboxReviewUiModel): Boolean {
        val statusFilterViewModel = inboxReviewViewModel.getStatusFilterListUpdated()
        val statusIsEmpty = InboxReviewMapper.mapToStatusFilterList(statusFilterViewModel).filter { it.isSelected }.count().isZero()
        val statusFilter = data.filterBy.getStatusFilter(prefixStatus).isUnAnswered
        return statusIsEmpty && statusFilter && !data.hasNext
    }

    private fun countStatusIsZero(): Boolean {
        return InboxReviewMapper.mapToStatusFilterList(inboxReviewViewModel.getStatusFilterListUpdated()).filter { it.isSelected }.count().isZero()
    }

    private fun getQuickFilter(): String {
        val statusFilterViewModel = inboxReviewViewModel.getStatusFilterListUpdated()
        return InboxReviewMapper.mapToStatusFilterList(statusFilterViewModel).filter { it.isSelected }.joinToString { it.sortFilterItem?.title.toString() }
    }

    private fun hasFiltered(): Boolean {
        val statusFilter = inboxReviewViewModel.getStatusFilterListUpdated().filter { it.isSelected }.count()
        val ratingFilter = inboxReviewViewModel.getRatingFilterListUpdated().filter { it.isSelected }.count()
        return statusFilter > 0 || ratingFilter > 0
    }

    private fun createCoachMarkItems(kejarUlasanLabel: View): ArrayList<CoachMark2Item> {
        return arrayListOf(CoachMark2Item(kejarUlasanLabel, context?.getString(R.string.kejar_ulasan_coach_mark_title) ?: "", context?.getString(R.string.kejar_ulasan_coach_mark_subtitle) ?: "", CoachMark2.POSITION_TOP))
    }

    private fun isFirstTimeSeeKejarUlasan(): Boolean {
        return inboxReviewPreference.isFirstTimeSeeKejarUlasan(inboxReviewViewModel.userSession.userId)

    }

    private fun setFirstTimeSeeKejarUlasan() {
        inboxReviewPreference.setFirstTimeSeeKejarUlasan(inboxReviewViewModel.userSession.userId)
    }

    private fun initCoachMark() {
        coachmark = context?.let { CoachMark2(it) }
    }

}