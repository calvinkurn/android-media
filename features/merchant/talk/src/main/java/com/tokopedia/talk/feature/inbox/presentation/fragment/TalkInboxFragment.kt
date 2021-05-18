package com.tokopedia.talk.feature.inbox.presentation.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.inboxcommon.InboxFragment
import com.tokopedia.inboxcommon.InboxFragmentContainer
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.talk.R
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringContract
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.analytics.TalkInboxTracking
import com.tokopedia.talk.feature.inbox.analytics.TalkInboxTrackingConstants
import com.tokopedia.talk.feature.inbox.data.TalkInboxFilter
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.data.TalkInboxViewState
import com.tokopedia.talk.feature.inbox.di.DaggerTalkInboxComponent
import com.tokopedia.talk.feature.inbox.di.TalkInboxComponent
import com.tokopedia.talk.feature.inbox.presentation.adapter.TalkInboxAdapterTypeFactory
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.BaseTalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxOldUiModel
import com.tokopedia.talk.feature.inbox.presentation.adapter.uimodel.TalkInboxUiModel
import com.tokopedia.talk.feature.inbox.presentation.listener.TalkInboxListener
import com.tokopedia.talk.feature.inbox.presentation.listener.TalkInboxViewHolderListener
import com.tokopedia.talk.feature.inbox.presentation.viewmodel.TalkInboxViewModel
import com.tokopedia.talk.feature.inbox.util.TalkInboxPreference
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_talk_inbox.*
import kotlinx.android.synthetic.main.partial_talk_connection_error.view.*
import kotlinx.android.synthetic.main.partial_talk_inbox_empty.*
import javax.inject.Inject

class TalkInboxFragment : BaseListFragment<BaseTalkInboxUiModel, TalkInboxAdapterTypeFactory>(),
        HasComponent<TalkInboxComponent>, TalkPerformanceMonitoringContract, TalkInboxViewHolderListener, InboxFragment {

    companion object {
        const val TAB_PARAM = "tab_param"
        const val FILTER_PARAM = "filter"
        const val FILTER_UNREAD = "unread"
        const val EMPTY_DISCUSSION_IMAGE = "https://ecs7.tokopedia.net/android/talk_inbox_empty.png"
        const val REPLY_REQUEST_CODE = 420
        const val EMPTY_SELLER_READ_DISCUSSION = "https://ecs7.tokopedia.net/android/others/talk_inbox_seller_empty_read.png"
        const val EMPTY_SELLER_DISCUSSION = "https://ecs7.tokopedia.net/android/others/talk_inbox_seller_empty_unread.png"
        const val EMPTY_SELLER_PROBLEM = "https://ecs7.tokopedia.net/android/others/talk_empty_reported_discussion.png"
        const val EMPTY_SELLER_AUTOREPLIED = "https://images.tokopedia.net/img/android/talk/talk_inbox_empty_autoreplied.png"
        const val COACH_MARK_SHOWN = false
        const val COACH_MARK_LAST_INDEX = 2
        const val INDEX_UNRESPONDED_FILTER = 0
        const val INDEX_PROBLEM_FILTER = 1
        const val INDEX_AUTOREPLY_FILTER = 2

        fun createNewInstance(tab: TalkInboxTab? = null, talkInboxListener: TalkInboxListener? = null): TalkInboxFragment {
            return TalkInboxFragment().apply {
                this.talkInboxListener = talkInboxListener
                arguments = Bundle().apply {
                    tab?.let {
                        putString(TAB_PARAM, it.tabParam)
                    }
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

    @Inject
    lateinit var userSession: UserSessionInterface

    private var talkPerformanceMonitoringListener: TalkPerformanceMonitoringListener? = null
    private var talkInboxListener: TalkInboxListener? = null
    private var inboxType = ""
    private var containerListener: InboxFragmentContainer? = null
    private lateinit var remoteConfigInstance: RemoteConfigInstance
    private var shouldHitRoleChangedTracker = false
    private var talkInboxPreference: TalkInboxPreference? = null
    private var coachMark: CoachMark2? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REPLY_REQUEST_CODE) {
            loadInitialData()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun getAdapterTypeFactory(): TalkInboxAdapterTypeFactory {
        return TalkInboxAdapterTypeFactory(this, isNewInbox())
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

    override fun onInboxItemClicked(talkInboxUiModel: TalkInboxUiModel?, talkInboxOldUiModel: TalkInboxOldUiModel?, position: Int) {
        talkInboxUiModel?.inboxDetail?.let {
            talkInboxTracking.eventClickThreadEcommerce(viewModel.getType(), it.questionID,  viewModel.getUserId(), position, it.isUnread)
            goToReply(it.questionID)
            if(it.isUnread || it.state.isUnresponded || it.state.hasProblem) {
                containerListener?.decreaseDiscussionUnreadCounter()
            }
        }
        talkInboxOldUiModel?.inboxDetail?.let {
            talkInboxTracking.eventClickThread(viewModel.getType(), it.questionID, it.productID,
                    viewModel.getActiveFilter(), !it.isUnread, viewModel.getShopId(), getCounterForTracking(), viewModel.getUserId())
            goToReply(it.questionID)
        }
    }

    override fun onSwipeRefresh() {
        containerListener?.refreshNotificationCounter()
        super.onSwipeRefresh()
    }

    override fun onRoleChanged(role: Int) {
        if (!::viewModel.isInitialized) return
        when (role) {
            RoleType.BUYER -> inboxType = TalkInboxTab.BUYER_TAB
            RoleType.SELLER -> inboxType = TalkInboxTab.SHOP_TAB
        }
        clearAllData()
        setInboxType()
        initSortFilter()
        updateSettingsIconVisibility()
        shouldHitRoleChangedTracker = true
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
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
        super.onViewCreated(view, savedInstanceState)
        initSharedPrefs()
        setupSettingsIcon()
        initSortFilter()
        initErrorPage()
        hideToolbar()
        setupToolbar()
        showFullPageLoading()
        selectUnreadFilterFromCardSA()
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

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            coachMark?.dismissCoachMark()
        } else {
            if(isShowCoachMark() && isSellerView() && isNewInbox()) {
                coachMark?.showCoachMark(getCoachMarkItems())
            }
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
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    with(it.data) {
                        if(!isNewInbox()) {
                            talkInboxTracking.eventLazyLoad(viewModel.getType(), it.page, inbox.count { inbox -> inbox.isUnread }, inbox.count { inbox -> !inbox.isUnread }, shopID, viewModel.getUserId())
                        }
                        hideFullPageError()
                        hideFullPageLoading()
                        hideLoading()
                        if (it.page == TalkConstants.DEFAULT_INITIAL_PAGE) {
                            hitOnRoleChangeTracker()
                            talkInboxListener?.updateUnreadCounter(it.data.sellerUnread, it.data.buyerUnread)
                            setFilterCounter()
                            hideLoading()
                            if (it.data.inbox.isEmpty()) {
                                when (it.filter) {
                                    is TalkInboxFilter.TalkInboxNoFilter -> {
                                        showEmptyInbox()
                                    }
                                    is TalkInboxFilter.TalkInboxUnreadFilter -> {
                                        showEmptyFilter(getString(R.string.inbox_empty_title), getString(R.string.inbox_empty_unread_discussion))
                                    }
                                    is TalkInboxFilter.TalkInboxReadFilter -> {
                                        showEmptyFilter(getString(R.string.inbox_empty_title), getString(R.string.inbox_empty_read_discussion))
                                    }
                                    is TalkInboxFilter.TalkInboxUnrespondedFilter -> {
                                        showEmptySeller(EMPTY_SELLER_READ_DISCUSSION, getString(R.string.inbox_unresponded_empty_title), getString(R.string.inbox_unresponded_empty_subtitle))
                                    }
                                    is TalkInboxFilter.TalkInboxProblemFilter -> {
                                        showEmptySeller(EMPTY_SELLER_PROBLEM, getString(R.string.inbox_problem_empty), getString(R.string.inbox_problem_empty_subtitle))
                                    }
                                    is TalkInboxFilter.TalkInboxAutorepliedFilter -> {
                                        showEmptySeller(EMPTY_SELLER_AUTOREPLIED, getString(R.string.inbox_autoreplied_empty_title), getString(R.string.inbox_autoreplied_empty_subtitle))
                                    }
                                }
                                return@Observer
                            }
                        }
                        if (isNewInbox()) {
                            renderData(inbox.map { inbox -> TalkInboxUiModel(inbox, isSellerView()) }, it.data.hasNext)
                        } else {
                            renderOldData(inbox.map { inbox -> TalkInboxOldUiModel(inbox, isSellerView()) }, it.data.hasNext)
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
        if (isSellerView() && isNewInbox()) {
            talkInboxEmptyImage.loadImage(EMPTY_SELLER_DISCUSSION)
            talkInboxEmptySubtitle.text = getString(R.string.inbox_empty_seller_subtitle)
        } else {
            talkInboxEmptyImage.loadImage(EMPTY_DISCUSSION_IMAGE)
            talkInboxEmptySubtitle.text = ""
        }
        talkInboxEmpty.show()
    }

    private fun showEmptyFilter(title: String, subtitle: String) {
        talkInboxEmptyImage.loadImage(EMPTY_DISCUSSION_IMAGE)
        talkInboxEmptyTitle.text = title
        talkInboxEmptySubtitle.text = subtitle
        talkInboxEmpty.show()
        talkInboxRecyclerView.hide()
    }

    private fun showEmptySeller(imageUrl: String, title: String, subtitle: String) {
        talkInboxEmptyImage.loadImage(imageUrl)
        talkInboxEmptyTitle.text = title
        talkInboxEmptySubtitle.text = subtitle
        talkInboxEmpty.show()
        talkInboxRecyclerView.hide()
    }

    private fun hideEmpty() {
        talkInboxEmpty.hide()
    }

    private fun showFullPageLoading() {
        if (isNewInbox()) {
            unifiedInboxPageLoading.show()
            return
        }
        inboxPageLoading.show()
    }

    private fun hideFullPageLoading() {
        if (isNewInbox()) {
            unifiedInboxPageLoading.hide()
            return
        }
        inboxPageLoading.hide()
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
            if (isSellerView() && isNewInbox()) {
                addItem(getSellerFilterList())
                setSettingsChipMargins()
                initCoachmark()
                return
            }
            addItem(getFilterList())
        }
    }

    private fun selectUnreadFilterFromCardSA() {
        if(GlobalConfig.isSellerApp()) {
            val filter = activity?.intent?.data?.getQueryParameter(FILTER_PARAM)
            if (filter == FILTER_UNREAD) {
                val unrespondedFilter = talkInboxSortFilter.chipItems?.getOrNull(INDEX_UNRESPONDED_FILTER)
                val problemFilter = talkInboxSortFilter.chipItems?.getOrNull(INDEX_PROBLEM_FILTER)
                val autoRepliedFilterChip = talkInboxSortFilter.chipItems?.getOrNull(INDEX_AUTOREPLY_FILTER)
                unrespondedFilter?.toggle()
                selectFilter(TalkInboxFilter.TalkInboxUnrespondedFilter(), shouldTrack = false)
                if(unrespondedFilter?.type == ChipsUnify.TYPE_SELECTED) {
                    problemFilter?.type = ChipsUnify.TYPE_NORMAL
                    autoRepliedFilterChip?.type = ChipsUnify.TYPE_NORMAL
                }
            }
        }
    }

    private fun getFilterList(): ArrayList<SortFilterItem> {
        val readFilter = SortFilterItem(getString(R.string.inbox_read))
        val unreadFilter = SortFilterItem(getString(R.string.inbox_unread))
        readFilter.listener = {
            readFilter.toggle()
            selectFilter(TalkInboxFilter.TalkInboxReadFilter())
            if(readFilter.type == ChipsUnify.TYPE_SELECTED) {
                unreadFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        unreadFilter.listener = {
            unreadFilter.toggle()
            selectFilter(TalkInboxFilter.TalkInboxUnreadFilter())
            if(unreadFilter.type == ChipsUnify.TYPE_SELECTED) {
                readFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        return arrayListOf(unreadFilter, readFilter)
    }

    private fun getSellerFilterList(): ArrayList<SortFilterItem> {
        val unrespondedFilter = if (getUnrespondedCount() != 0L) {
            SortFilterItem(getString(R.string.inbox_unresponded) + " (${getUnrespondedCount()})")
        } else {
            SortFilterItem(getString(R.string.inbox_unresponded))
        }
        val problemFilter = if (getUnrespondedCount() != 0L) {
            SortFilterItem(getString(R.string.inbox_problem) + " (${getProblemCount()})")
        } else {
            SortFilterItem(getString(R.string.inbox_problem))
        }
        val autoRepliedFilter = SortFilterItem(getString(R.string.inbox_auto_replied))
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
            selectFilter(TalkInboxFilter.TalkInboxAutorepliedFilter())
            if (autoRepliedFilter.type == ChipsUnify.TYPE_SELECTED) {
                unrespondedFilter.type = ChipsUnify.TYPE_NORMAL
                problemFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        if (GlobalConfig.isSellerApp()) {
            return arrayListOf(unrespondedFilter, problemFilter, autoRepliedFilter)
        }
        return arrayListOf(unrespondedFilter, problemFilter, autoRepliedFilter)
    }

    private fun setupSettingsIcon() {
        talkInboxSettingsIcon?.apply {
            setOnClickListener {
                goToSellerSettings()
            }
            updateSettingsIconVisibility()
        }
    }

    private fun updateSettingsIconVisibility() {
        talkInboxSettingsIcon?.apply {
            if(isSellerView() && isNewInbox()) {
                show()
                return
            }
            hide()
        }
    }

    private fun goToSellerSettings() {
        talkInboxTracking.eventClickSettings(viewModel.getShopId(), viewModel.getUserId())
        RouteManager.route(context, ApplinkConstInternalGlobal.TALK_SELLER_SETTINGS)
    }

    private fun selectFilter(filter: TalkInboxFilter, shouldTrack: Boolean = true) {
        viewModel.setFilter(filter, isSellerView(), shouldTrack)
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
        if(isNewInbox()) {
            inboxType = if (containerListener?.role == RoleType.BUYER) {
                TalkInboxTab.BUYER_TAB
            } else {
                TalkInboxTab.SHOP_TAB
            }
        }
        viewModel.setInboxType(inboxType)
    }

    private fun isNewView(): Boolean {
        return getAbTestPlatform()?.getString(
                AbTestPlatform.KEY_AB_INBOX_REVAMP, AbTestPlatform.VARIANT_OLD_INBOX
        ) == AbTestPlatform.VARIANT_NEW_INBOX
    }

    private fun isNewNav(): Boolean {
        return getAbTestPlatform()?.getString(
                AbTestPlatform.NAVIGATION_EXP_TOP_NAV, AbTestPlatform.NAVIGATION_VARIANT_OLD
        ) == AbTestPlatform.NAVIGATION_VARIANT_REVAMP
    }

    private fun isSellerView(): Boolean {
        return viewModel.getType() == TalkInboxTab.SHOP_OLD || GlobalConfig.isSellerApp() || viewModel.getType() == TalkInboxTab.SHOP_TAB
    }

    private fun getProblemCount(): Long {
        return (viewModel.inboxList.value as? TalkInboxViewState.Success)?.data?.problemTotal ?: 0L
    }

    private fun getUnrespondedCount(): Long {
        return (viewModel.inboxList.value as? TalkInboxViewState.Success)?.data?.unrespondedTotal ?: 0L
    }

    private fun setFilterCounter() {
        if(isSellerView() && isNewInbox()) {
            if (getUnrespondedCount() != 0L) {
                talkInboxSortFilter?.chipItems?.getOrNull(0)?.title = getString(R.string.inbox_unresponded) + " (${getUnrespondedCount()})"
            } else {
                talkInboxSortFilter?.chipItems?.getOrNull(0)?.title = getString(R.string.inbox_unresponded)
            }
            if (getProblemCount() != 0L) {
                talkInboxSortFilter?.chipItems?.getOrNull(1)?.title = getString(R.string.inbox_problem) + " (${getProblemCount()})"
            } else {
                talkInboxSortFilter?.chipItems?.getOrNull(1)?.title = getString(R.string.inbox_problem)
            }
        }
    }

    private fun updateSharedPrefs() {
        talkInboxPreference?.updateSharedPrefs(COACH_MARK_SHOWN)
    }

    private fun isShowCoachMark(): Boolean {
        return talkInboxPreference?.isShowCoachMark() ?: false
    }

    private fun initCoachmark() {
        if (isShowCoachMark()) {
            coachMark = context?.let { CoachMark2(it) }
            coachMark?.setStepListener(object : CoachMark2.OnStepListener {
                override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                    if (currentIndex == COACH_MARK_LAST_INDEX) {
                        coachMark?.stepNext?.text = getString(R.string.inbox_coach_mark_finish)
                    }
                }
            })
            if(this.isVisible) {
                coachMark?.showCoachMark(getCoachMarkItems())
            }
            updateSharedPrefs()
        }
    }

    private fun getCoachMarkItems(): ArrayList<CoachMark2Item> {
        val coachMarkItem = ArrayList<CoachMark2Item>()
        if (talkInboxSortFilter?.chipItems != null) {
            coachMarkItem.addAll(listOf(
                    getCoachMarkItem(talkInboxSortFilter.chipItems?.getOrNull(0)?.refChipUnify, getString(R.string.inbox_coach_mark_filter_title), getString(R.string.inbox_coach_mark_filter_subtitle)),
                    getCoachMarkItem(talkInboxSortFilter.chipItems?.getOrNull(1)?.refChipUnify, getString(R.string.inbox_coach_mark_reported_title), getString(R.string.inbox_coach_mark_reported_subtitle)),
                    getCoachMarkItem(talkInboxSortFilter.chipItems?.getOrNull(2)?.refChipUnify, getString(R.string.inbox_coach_mark_smart_reply_title), getString(R.string.inbox_coach_mark_smart_reply_subtitle))
            ))
        }
        return coachMarkItem
    }

    private fun setSettingsChipMargins() {
        talkInboxSortFilter?.chipItems?.getOrNull(3)?.refChipUnify?.chip_text?.hide()
    }

    private fun getCoachMarkItem(anchorView: View?, title: String, subtitle: String): CoachMark2Item {
        return CoachMark2Item(
                anchorView ?: talkInboxSortFilter,
                title,
                subtitle
        )
    }

    private fun hideToolbar() {
        if (isNewInbox() || GlobalConfig.isSellerApp()) {
            (activity as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                setSupportActionBar(headerTalkInbox)
            }
        }
    }

    private fun setupToolbar() {
        headerTalkInbox.apply {
            setTitle(R.string.title_talk_discuss)
            if (GlobalConfig.isSellerApp()) {
                if(isNewInbox()) {
                    addRightIcon(0).apply {
                        clearImage()
                        setImageDrawable(com.tokopedia.iconunify.getIconUnifyDrawable(context, IconUnify.SETTING, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)))
                        setOnClickListener {
                            goToSellerSettings()
                        }
                    }
                }
                show()
                talkInboxSettingsIcon?.hide()
            }
        }
    }

    private fun getAbTestPlatform(): AbTestPlatform? {
        return try {
            if (!::remoteConfigInstance.isInitialized) {
                remoteConfigInstance = RemoteConfigInstance(activity?.application)
            }
            remoteConfigInstance.abTestPlatform
        } catch (e: IllegalStateException) {
            null
        }
    }

    private fun getCounterForTracking(): Long {
        if(isSellerView() && isNewInbox()) {
            return viewModel.getUnrespondedCount()
        }
        return viewModel.getUnreadCount()
    }

    private fun hitOnRoleChangeTracker() {
        if(shouldHitRoleChangedTracker)  {
            talkInboxTracking.eventClickTab(inboxType, viewModel.getUserId(), viewModel.getShopId(), getCounterForTracking())
            shouldHitRoleChangedTracker = false
        }
    }

    private fun initSharedPrefs() {
        talkInboxPreference = TalkInboxPreference(context)
    }

    private fun isNewInbox(): Boolean {
        return isNewNav() && isNewView()
    }
}