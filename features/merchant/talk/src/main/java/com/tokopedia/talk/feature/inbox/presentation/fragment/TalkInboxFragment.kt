package com.tokopedia.talk.feature.inbox.presentation.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.config.GlobalConfig
import com.tokopedia.inboxcommon.InboxFragment
import com.tokopedia.inboxcommon.InboxFragmentContainer
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringContract
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.analytics.TalkInboxTracking
import com.tokopedia.talk.feature.inbox.analytics.TalkInboxTrackingConstants
import com.tokopedia.talk.feature.inbox.data.TalkInboxFilter
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.di.DaggerTalkInboxComponent
import com.tokopedia.talk.feature.inbox.di.TalkInboxComponent
import com.tokopedia.talk.feature.inbox.presentation.adapter.TalkInboxAdapterTypeFactory
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxOldUiModel
import com.tokopedia.talk.feature.inbox.data.TalkInboxViewState
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.BaseTalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.listener.TalkInboxListener
import com.tokopedia.talk.feature.inbox.presentation.listener.TalkInboxViewHolderListener
import com.tokopedia.talk.feature.inbox.presentation.viewmodel.TalkInboxViewModel
import com.tokopedia.talk.R
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_talk_inbox.*
import kotlinx.android.synthetic.main.partial_talk_connection_error.view.*
import kotlinx.android.synthetic.main.partial_talk_inbox_empty.*
import javax.inject.Inject

class TalkInboxFragment : BaseListFragment<BaseTalkInboxUiModel, TalkInboxAdapterTypeFactory>(),
        HasComponent<TalkInboxComponent>, TalkPerformanceMonitoringContract, TalkInboxViewHolderListener, InboxFragment {

    companion object {
        const val TAB_PARAM = "tab_param"
        const val EMPTY_DISCUSSION_IMAGE = "https://ecs7.tokopedia.net/android/talk_inbox_empty.png"
        const val REPLY_REQUEST_CODE = 420
        const val EMPTY_SELLER_READ_DISCUSSION = "https://ecs7.tokopedia.net/android/others/talk_inbox_seller_empty_read.png"
        const val EMPTY_SELLER_DISCUSSION = "https://ecs7.tokopedia.net/android/others/talk_inbox_seller_empty_unread.png"
        const val KEY_SHOW_COACH_MARK = "showCoachMark"
        private const val DISCUSSION_PREF = "discussion.pref"
        const val COACH_MARK_INITIAL_VALUE = true
        const val COACH_MARK_SHOWN = false
        const val COACH_MARK_LAST_INDEX = 2

        fun createNewInstance(tab: TalkInboxTab, talkInboxListener: TalkInboxListener? = null): TalkInboxFragment {
            return TalkInboxFragment().apply {
                this.talkInboxListener = talkInboxListener
                arguments = Bundle().apply {
                    putString(TAB_PARAM, tab.tabParam)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: TalkInboxViewModel

    @Inject
    lateinit var talkInboxTracking: TalkInboxTracking

    @Inject
    lateinit var trackingQueue: TrackingQueue

    private var talkPerformanceMonitoringListener: TalkPerformanceMonitoringListener? = null
    private var talkInboxListener: TalkInboxListener? = null
    private var inboxType = ""
    private var containerListener: InboxFragmentContainer? = null
    private var sharedPrefs: SharedPreferences? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REPLY_REQUEST_CODE) {
            loadInitialData()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun getAdapterTypeFactory(): TalkInboxAdapterTypeFactory {
        return TalkInboxAdapterTypeFactory(this, isOldView())
    }

    override fun getScreenName(): String {
        return TalkInboxTrackingConstants.SCREEN_NAME
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(talkOldUiModel: BaseTalkInboxUiModel?) {
        // No Op
    }

    override fun loadData(page: Int) {
        viewModel.updatePage(page)
    }

    override fun getComponent(): TalkInboxComponent? {
        return activity?.run {
            DaggerTalkInboxComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun stopPreparePerfomancePageMonitoring() {
        talkPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        talkInboxRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                talkPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                talkPerformanceMonitoringListener?.stopPerformanceMonitoring()
                talkInboxRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): TalkPerformanceMonitoringListener? {
        return if (context is TalkPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    override fun onInboxItemImpressed(talkId: String, position: Int, isUnread: Boolean) {
        talkInboxTracking.eventItemImpress(inboxType, talkId, viewModel.getUserId(), position, isUnread, trackingQueue)
    }

    override fun onInboxItemClicked(talkInboxUiModel: TalkInboxUiModel?, talkInboxOldUiModel: TalkInboxOldUiModel?) {
        talkInboxUiModel?.inboxDetail?.let {
            talkInboxTracking.eventClickThread(viewModel.getType(), it.questionID, it.productID,
                    viewModel.getActiveFilter(), !it.isUnread, viewModel.getShopId(), viewModel.getUnreadCount(), viewModel.getUserId())
            goToReply(it.questionID)
        }
        talkInboxOldUiModel?.inboxDetail?.let {
            talkInboxTracking.eventClickThread(viewModel.getType(), it.questionID, it.productID,
                    viewModel.getActiveFilter(), !it.isUnread, viewModel.getShopId(), viewModel.getUnreadCount(), viewModel.getUserId())
            goToReply(it.questionID)
        }
    }

    override fun onRoleChanged(role: Int) {
        when (role) {
            RoleType.BUYER -> inboxType = TalkInboxTab.BUYER_TAB
            RoleType.SELLER -> inboxType = TalkInboxTab.SHOP_TAB
        }
        clearAllData()
        setInboxType()
        initSortFilter()
    }

    override fun onPageClickedAgain() {
        getRecyclerView(view).scrollToPosition(0)
    }

    override fun onPause() {
        super.onPause()
        if (::trackingQueue.isInitialized) {
            trackingQueue.sendAll()
        }
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return talkInboxRecyclerView
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return talkInboxSwipeToRefresh
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        talkPerformanceMonitoringListener = castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromArgument()
        super.onCreate(savedInstanceState)
        setInboxType()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initErrorPage()
        initSortFilter()
        setupTicker()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeInboxList()
    }

    override fun onAttachActivity(context: Context?) {
        if (context is InboxFragmentContainer) {
            containerListener = context
        }
    }

    private fun goToReply(questionId: String) {
        val intent = RouteManager.getIntent(context, Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.TALK_REPLY, questionId))
                .buildUpon()
                .appendQueryParameter(TalkConstants.PARAM_SOURCE, TalkConstants.INBOX_SOURCE)
                .appendQueryParameter(TalkConstants.PARAM_TYPE, inboxType)
                .build().toString()
        )
        startActivityForResult(intent, REPLY_REQUEST_CODE)
    }

    private fun observeInboxList() {
        viewModel.inboxList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is TalkInboxViewState.Success -> {
                    with(it.data) {
                        talkInboxTracking.eventLazyLoad(viewModel.getType(), it.page, inbox.count { inbox -> inbox.isUnread }, inbox.count { inbox -> !inbox.isUnread }, shopID, viewModel.getUserId())
                        hideFullPageError()
                        hideFullPageLoading()
                        hideLoading()
                        if (it.page == TalkConstants.DEFAULT_INITIAL_PAGE) {
                            talkInboxListener?.updateUnreadCounter(it.data.sellerUnread, it.data.buyerUnread)
                            hideLoading()
                            if (it.data.inbox.isEmpty()) {
                                when (it.filter) {
                                    is TalkInboxFilter.TalkInboxNoFilter -> {
                                        showEmptyInbox()
                                    }
                                    is TalkInboxFilter.TalkInboxUnreadFilter -> {
                                        showEmptyUnread()
                                    }
                                    is TalkInboxFilter.TalkInboxReadFilter -> {
                                        showEmptyRead()
                                    }
                                }
                                return@Observer
                            }
                        }
                        setFilterCounter()
                        if (isOldView()) {
                            renderOldData(inbox.map { inbox -> TalkInboxOldUiModel(inbox, isSellerView()) }, it.data.hasNext)
                        } else {
                            renderData(inbox.map { inbox -> TalkInboxUiModel(inbox, isSellerView()) }, it.data.hasNext)
                        }
                    }

                }
                is TalkInboxViewState.Fail -> {
                    hideFullPageLoading()
                    hideLoading()
                    if (it.page == TalkConstants.DEFAULT_INITIAL_PAGE) {
                        showFullPageError()
                    } else {
                        showErrorToaster()
                    }
                }
                is TalkInboxViewState.Loading -> {
                    hideLoading()
                    if (it.page == TalkConstants.DEFAULT_INITIAL_PAGE) {
                        showFullPageLoading()
                    }
                }
            }
        })
    }

    private fun renderOldData(data: List<TalkInboxOldUiModel>, hasNext: Boolean) {
        hideEmpty()
        talkInboxRecyclerView.show()
        renderList(data, hasNext)
    }

    private fun renderData(data: List<TalkInboxUiModel>, hasNext: Boolean) {
        hideEmpty()
        talkInboxRecyclerView.show()
        renderList(data, hasNext)
    }

    private fun initErrorPage() {
        inboxPageError.apply {
            talkConnectionErrorRetryButton.setOnClickListener {
                loadInitialData()
            }
            reading_image_error.loadImageDrawable(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
        }
    }

    private fun showFullPageError() {
        inboxPageError.show()
    }

    private fun hideFullPageError() {
        inboxPageError.hide()
    }

    private fun showErrorToaster() {
        view?.let {
            Toaster.build(talkInboxContainer, getString(R.string.inbox_toaster_connection_error), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.talk_retry), View.OnClickListener { loadData(currentPage) }).show()
        }
    }

    private fun showEmptyInbox() {
        talkInboxEmptyTitle.text = getString(R.string.inbox_all_empty)
        if (isSellerView() && !isOldView()) {
            talkInboxEmptyImage.loadImage(EMPTY_SELLER_DISCUSSION)
            talkInboxEmptySubtitle.text = getString(R.string.inbox_empty_seller_subtitle)
        } else {
            talkInboxEmptyImage.loadImage(EMPTY_DISCUSSION_IMAGE)
            talkInboxEmptySubtitle.text = ""
        }
        talkInboxEmpty.show()
    }

    private fun showEmptyUnread() {
        when {
            isSellerView() && !isOldView() -> {
                talkInboxEmptyImage.loadImage(EMPTY_SELLER_READ_DISCUSSION)
                talkInboxEmptyTitle.text = getString(R.string.inbox_empty_seller_unread_title)
                talkInboxEmptySubtitle.text = getString(R.string.inbox_empty_seller_unread_subtitle)
            }
            else -> {
                talkInboxEmptyImage.loadImage(EMPTY_DISCUSSION_IMAGE)
                talkInboxEmptyTitle.text = getString(R.string.inbox_empty_title)
                talkInboxEmptySubtitle.text = getString(R.string.inbox_empty_unread_discussion)
            }
        }
        talkInboxEmpty.show()
        talkInboxRecyclerView.hide()
    }

    private fun showEmptyRead() {
        when {
            isSellerView() && !isOldView() -> {
                talkInboxEmptyImage.loadImage(EMPTY_SELLER_DISCUSSION)
                talkInboxEmptyTitle.text = getString(R.string.inbox_all_empty)
                talkInboxEmptySubtitle.text = getString(R.string.inbox_empty_seller_subtitle)
            }
            else -> {
                talkInboxEmptyImage.loadImage(EMPTY_DISCUSSION_IMAGE)
                talkInboxEmptyTitle.text = getString(R.string.inbox_empty_title)
                talkInboxEmptySubtitle.text = getString(R.string.inbox_empty_read_discussion)
            }
        }
        talkInboxEmpty.show()
        talkInboxRecyclerView.hide()
    }

    private fun hideEmpty() {
        talkInboxEmpty.hide()
    }

    private fun showFullPageLoading() {
        if (isOldView()) {
            inboxPageLoading.show()
            return
        }
        unifiedInboxPageLoading.show()
    }

    private fun hideFullPageLoading() {
        if (isOldView()) {
            inboxPageLoading.hide()
            return
        }
        unifiedInboxPageLoading.hide()
    }

    private fun getDataFromArgument() {
        arguments?.getString(TAB_PARAM)?.let {
            inboxType = it
        }
    }

    private fun initSortFilter() {
        talkInboxSortFilter?.apply {
            sortFilterItems.removeAllViews()
            sortFilterPrefix.removeAllViews()
            if (isSellerView() && !isOldView()) {
                addItem(getSellerFilterList())
                initCoachmark()
                return
            }
            addItem(getFilterList())
        }
    }

    private fun getFilterList(): ArrayList<SortFilterItem> {
        val readFilter = SortFilterItem(getString(R.string.inbox_read))
        val unreadFilter = SortFilterItem(getString(R.string.inbox_unread))
        readFilter.listener = {
            readFilter.toggle()
            selectFilter(TalkInboxFilter.TalkInboxReadFilter())
            if (readFilter.type == ChipsUnify.TYPE_SELECTED) {
                unreadFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        unreadFilter.listener = {
            unreadFilter.toggle()
            selectFilter(TalkInboxFilter.TalkInboxUnreadFilter())
            if (unreadFilter.type == ChipsUnify.TYPE_SELECTED) {
                readFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        return arrayListOf(unreadFilter, readFilter)
    }

    private fun getSellerFilterList(): ArrayList<SortFilterItem> {
        val unrespondedFilter = if (getUnrespondedCount() != 0) {
            SortFilterItem(getString(R.string.inbox_unresponded) + " (${getUnrespondedCount()})")
        } else {
            SortFilterItem(getString(R.string.inbox_unresponded))
        }
        val problemFilter = if (getUnrespondedCount() != 0) {
            SortFilterItem(getString(R.string.inbox_problem) + " (${getProblemCount()})")
        } else {
            SortFilterItem(getString(R.string.inbox_problem))
        }
        val autoRepliedFilter = SortFilterItem(getString(R.string.inbox_auto_replied))
        val settingsChip = SortFilterItem("Settings")
        unrespondedFilter.listener = {
            unrespondedFilter.toggle()
            selectFilter(TalkInboxFilter.TalkInboxUnrespondedFilter())
            if (unrespondedFilter.type == ChipsUnify.TYPE_SELECTED) {
                problemFilter.type = ChipsUnify.TYPE_NORMAL
                autoRepliedFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        problemFilter.listener = {
            problemFilter.toggle()
            selectFilter(TalkInboxFilter.TalkInboxProblemFilter())
            if (problemFilter.type == ChipsUnify.TYPE_SELECTED) {
                unrespondedFilter.type = ChipsUnify.TYPE_NORMAL
                autoRepliedFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        autoRepliedFilter.listener = {
            autoRepliedFilter.toggle()
//            selectFilter(TalkInboxFilter.TalkInboxProblemFilter())
            if (autoRepliedFilter.type == ChipsUnify.TYPE_SELECTED) {
                unrespondedFilter.type = ChipsUnify.TYPE_NORMAL
                problemFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        settingsChip.listener = {
            gotoSellerSettings()
        }
        return arrayListOf(unrespondedFilter, problemFilter, autoRepliedFilter, settingsChip)
    }

    private fun gotoSellerSettings() {
        RouteManager.route(context, ApplinkConstInternalGlobal.TALK_SELLER_SETTINGS)
    }

    private fun selectFilter(filter: TalkInboxFilter) {
        viewModel.setFilter(filter)
        showFullPageLoading()
        clearAllData()
    }

    private fun SortFilterItem.toggle() {
        type = if (type == ChipsUnify.TYPE_NORMAL) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
    }

    private fun setInboxType() {
        if (isOldView()) {
            viewModel.setInboxType(inboxType)
            return
        }
        if (containerListener?.role == RoleType.BUYER) {
            viewModel.setInboxType(TalkInboxTab.BUYER_TAB)
            return
        }
        viewModel.setInboxType(TalkInboxTab.SHOP_TAB)
    }

    private fun isOldView(): Boolean {
        return false
    }

    private fun isSellerView(): Boolean {
        return viewModel.getType() == TalkInboxTab.SHOP_TAB
    }

    private fun getProblemCount(): Int {
        return (viewModel.inboxList.value as? TalkInboxViewState.Success)?.data?.problemTotal ?: 0
    }

    private fun getUnrespondedCount(): Int {
        return (viewModel.inboxList.value as? TalkInboxViewState.Success)?.data?.unrespondedTotal
                ?: 0
    }

    private fun setFilterCounter() {
        if (getUnrespondedCount() != 0) {
            talkInboxSortFilter?.chipItems?.getOrNull(0)?.title = getString(R.string.inbox_unresponded) + " (${getUnrespondedCount()})"
        }
        if (getProblemCount() != 0) {
            talkInboxSortFilter?.chipItems?.getOrNull(1)?.title = getString(R.string.inbox_problem) + " (${getProblemCount()})"
        }
    }

    private fun updateSharedPrefs(flag: Boolean) {
        context?.let {
            sharedPrefs = it.getSharedPreferences(DISCUSSION_PREF, Context.MODE_PRIVATE)
            sharedPrefs?.run {
                edit().putBoolean(KEY_SHOW_COACH_MARK, flag).apply()
            }
        }
    }

    private fun isShowCoachMark(): Boolean {
        return context?.let {
            sharedPrefs = it.getSharedPreferences(DISCUSSION_PREF, Context.MODE_PRIVATE)
            val result = sharedPrefs?.getBoolean(KEY_SHOW_COACH_MARK, COACH_MARK_INITIAL_VALUE) ?: false
            result
        } ?: false
    }

    private fun initCoachmark() {
        if(isShowCoachMark()) {
            val coachMarkItem = ArrayList<CoachMark2Item>()
            val coachMark = context?.let { CoachMark2(it) }
            if(talkInboxSortFilter?.chipItems !=  null) {
                coachMarkItem.addAll(listOf(
                        getCoachMarkItem(talkInboxSortFilter.chipItems.getOrNull(0)?.refChipUnify, getString(R.string.inbox_coach_mark_filter_title), getString(R.string.inbox_coach_mark_filter_subtitle)),
                        getCoachMarkItem(talkInboxSortFilter.chipItems.getOrNull(1)?.refChipUnify, getString(R.string.inbox_coach_mark_reported_title), getString(R.string.inbox_coach_mark_reported_subtitle)),
                        getCoachMarkItem(talkInboxSortFilter.chipItems.getOrNull(2)?.refChipUnify, getString(R.string.inbox_coach_mark_smart_reply_title), getString(R.string.inbox_coach_mark_smart_reply_subtitle))
                ))
            }
            coachMark?.setStepListener(object : CoachMark2.OnStepListener {
                override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                    if (currentIndex == COACH_MARK_LAST_INDEX) {
                        coachMark.stepNext?.text = getString(R.string.inbox_coach_mark_finish)
                    }
                }
            })
            coachMark?.showCoachMark(coachMarkItem)
            updateSharedPrefs(COACH_MARK_SHOWN)
        }
    }

    private fun getCoachMarkItem(anchorView: View?, title: String, subtitle: String): CoachMark2Item {
        return CoachMark2Item(
                anchorView ?: talkInboxSortFilter,
                title,
                subtitle
        )
    }

    private fun setupTicker() {
        if(GlobalConfig.isSellerApp()) {
            talkInboxTicker.apply {
                tickerTitle = getString(R.string.inbox_ticker_title)
                setTextDescription(getString(R.string.inbox_ticker_description))
                show()
            }
            return
        }
        talkInboxTicker.hide()
    }
}