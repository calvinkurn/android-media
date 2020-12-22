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
import com.tokopedia.user.session.UserSessionInterface
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
        const val EMPTY_SELLER_PROBLEM = "https://ecs7.tokopedia.net/android/others/talk_empty_reported_discussion.png"

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

    @Inject
    lateinit var userSession: UserSessionInterface

    private var talkPerformanceMonitoringListener: TalkPerformanceMonitoringListener? = null
    private var talkInboxListener: TalkInboxListener? = null
    private var inboxType = ""
    private var containerListener: InboxFragmentContainer? = null

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
        initToolbar()
        initSortFilter()
        initErrorPage()
        initSortFilter()
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
        unrespondedFilter.listener = {
            unrespondedFilter.toggle()
            selectFilter(TalkInboxFilter.TalkInboxUnrespondedFilter())
            if (unrespondedFilter.type == ChipsUnify.TYPE_SELECTED) {
                problemFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        problemFilter.listener = {
            problemFilter.toggle()
            selectFilter(TalkInboxFilter.TalkInboxProblemFilter())
            if (problemFilter.type == ChipsUnify.TYPE_SELECTED) {
                unrespondedFilter.type = ChipsUnify.TYPE_NORMAL
            }
        }
        return arrayListOf(unrespondedFilter, problemFilter)
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

    private fun initToolbar() {
        if (!userSession.hasShop() && !GlobalConfig.isSellerApp() && isOldView()) {
            setupToolbar()
        } else if (userSession.hasShop() && GlobalConfig.isSellerApp() && isOldView()) {
            setupToolbar()
        } else {
            headerTalkInbox?.hide()
        }
    }

    private fun setupToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                setSupportActionBar(headerTalkInbox)
                headerTalkInbox?.title = getString(R.string.title_talk_discuss)
            }
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
        if(isSellerView()) {
            if (getUnrespondedCount() != 0) {
                talkInboxSortFilter?.chipItems?.getOrNull(0)?.title = getString(R.string.inbox_unresponded) + " (${getUnrespondedCount()})"
            } else {
                talkInboxSortFilter?.chipItems?.getOrNull(0)?.title = getString(R.string.inbox_unresponded)
            }
            if (getProblemCount() != 0) {
                talkInboxSortFilter?.chipItems?.getOrNull(1)?.title = getString(R.string.inbox_problem) + " (${getProblemCount()})"
            } else {
                talkInboxSortFilter?.chipItems?.getOrNull(1)?.title = getString(R.string.inbox_problem)
            }
        }
    }
}