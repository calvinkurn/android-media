package com.tokopedia.talk.feature.inbox.presentation.fragment

import com.tokopedia.imageassets.TokopediaImageUrl

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
import com.tokopedia.kotlin.extensions.view.clearImage
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.talk.R
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringContract
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.databinding.FragmentTalkInboxBinding
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
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

open class TalkInboxFragment :
    BaseListFragment<BaseTalkInboxUiModel, TalkInboxAdapterTypeFactory>(),
    HasComponent<TalkInboxComponent>,
    TalkPerformanceMonitoringContract,
    TalkInboxViewHolderListener,
    InboxFragment {

    companion object {
        const val TAB_PARAM = "tab_param"
        const val FILTER_PARAM = "filter"
        const val FILTER_UNREAD = "unread"
        const val EMPTY_DISCUSSION_IMAGE = TokopediaImageUrl.EMPTY_DISCUSSION_IMAGE
        const val REPLY_REQUEST_CODE = 420
        const val EMPTY_SELLER_READ_DISCUSSION =
            "https://images.tokopedia.net/android/others/talk_inbox_seller_empty_read.png"
        const val EMPTY_SELLER_DISCUSSION =
            "https://images.tokopedia.net/android/others/talk_inbox_seller_empty_unread.png"
        const val EMPTY_SELLER_PROBLEM =
            "https://images.tokopedia.net/android/others/talk_empty_reported_discussion.png"
        const val EMPTY_SELLER_AUTOREPLIED =
            "https://images.tokopedia.net/img/android/talk/talk_inbox_empty_autoreplied.png"
        const val COACH_MARK_SHOWN = false
        const val COACH_MARK_LAST_INDEX = 2
        const val INDEX_UNRESPONDED_FILTER = 0
        const val INDEX_PROBLEM_FILTER = 1
        const val INDEX_AUTOREPLY_FILTER = 2
        const val SETTING_CHIP_POSITION = 3

        fun createNewInstance(
            tab: TalkInboxTab? = null,
            talkInboxListener: TalkInboxListener? = null
        ): TalkInboxFragment {
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

    private var binding by autoClearedNullable<FragmentTalkInboxBinding>()

    private var talkPerformanceMonitoringListener: TalkPerformanceMonitoringListener? = null
    protected var talkInboxListener: TalkInboxListener? = null
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
        return TalkInboxAdapterTypeFactory(this)
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
        binding?.talkInboxRecyclerView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    talkPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                    talkPerformanceMonitoringListener?.stopPerformanceMonitoring()
                    binding?.talkInboxRecyclerView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
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
        talkInboxTracking.eventItemImpress(
            inboxType,
            talkId,
            viewModel.getUserId(),
            position,
            isUnread,
            trackingQueue
        )
    }

    override fun onInboxItemClicked(
        talkInboxUiModel: TalkInboxUiModel?,
        talkInboxOldUiModel: TalkInboxOldUiModel?,
        position: Int
    ) {
        talkInboxUiModel?.inboxDetail?.let {
            talkInboxTracking.eventClickThreadEcommerce(
                viewModel.getType(),
                it.questionID,
                viewModel.getUserId(),
                position,
                it.isUnread
            )
            goToReply(it.questionID)
            if (it.isUnread || it.state.isUnresponded || it.state.hasProblem) {
                containerListener?.decreaseDiscussionUnreadCounter()
            }
        }
        talkInboxOldUiModel?.inboxDetail?.let {
            talkInboxTracking.eventClickThread(
                viewModel.getType(),
                it.questionID,
                it.productID,
                viewModel.getActiveFilter(),
                !it.isUnread,
                viewModel.getShopId(),
                getCounterForTracking(),
                viewModel.getUserId()
            )
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
        getRecyclerView(view)?.scrollToPosition(0)
    }

    override fun onPause() {
        super.onPause()
        if (::trackingQueue.isInitialized) {
            trackingQueue.sendAll()
        }
    }

    override fun getRecyclerView(view: View?): RecyclerView? {
        return binding?.talkInboxRecyclerView
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return binding?.talkInboxSwipeToRefresh
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTalkInboxBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
        super.onViewCreated(view, savedInstanceState)
        initSharedPrefs()
        setupSettingsIcon()
        setupTicker()
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
        observeSmartReplyDecommissionConfig()
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
            if (isSellerApp()) {
                coachMark?.showCoachMark(getCoachMarkItems())
            }
        }
    }

    private fun goToReply(questionId: String) {
        val intent = RouteManager.getIntent(
            context,
            Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.TALK_REPLY, questionId))
                .buildUpon()
                .appendQueryParameter(TalkConstants.PARAM_SOURCE, TalkConstants.INBOX_SOURCE)
                .appendQueryParameter(TalkConstants.PARAM_TYPE, inboxType)
                .build().toString()
        )
        startActivityForResult(intent, REPLY_REQUEST_CODE)
    }

    private fun observeInboxList() {
        viewModel.inboxList.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is TalkInboxViewState.Success -> {
                        stopNetworkRequestPerformanceMonitoring()
                        startRenderPerformanceMonitoring()
                        with(it.data) {
                            talkInboxTracking.eventLazyLoad(
                                viewModel.getType(),
                                it.page,
                                inbox.count { inbox -> inbox.isUnread },
                                inbox.count { inbox -> !inbox.isUnread },
                                shopID,
                                viewModel.getUserId()
                            )
                            hideFullPageError()
                            hideFullPageLoading()
                            hideLoading()
                            if (it.page == TalkConstants.DEFAULT_INITIAL_PAGE) {
                                hitOnRoleChangeTracker()
                                talkInboxListener?.updateUnreadCounter(
                                    it.data.sellerUnread,
                                    it.data.buyerUnread
                                )
                                setFilterCounter()
                                hideLoading()
                                if (it.data.inbox.isEmpty()) {
                                    when (it.filter) {
                                        is TalkInboxFilter.TalkInboxNoFilter -> {
                                            showEmptyInbox()
                                        }
                                        is TalkInboxFilter.TalkInboxUnreadFilter -> {
                                            showEmptyFilter(
                                                getString(R.string.inbox_empty_title),
                                                getString(R.string.inbox_empty_unread_discussion)
                                            )
                                        }
                                        is TalkInboxFilter.TalkInboxReadFilter -> {
                                            showEmptyFilter(
                                                getString(R.string.inbox_empty_title),
                                                getString(R.string.inbox_empty_read_discussion)
                                            )
                                        }
                                        is TalkInboxFilter.TalkInboxUnrespondedFilter -> {
                                            showEmptySeller(
                                                EMPTY_SELLER_READ_DISCUSSION,
                                                getString(R.string.inbox_unresponded_empty_title),
                                                getString(R.string.inbox_unresponded_empty_subtitle)
                                            )
                                        }
                                        is TalkInboxFilter.TalkInboxProblemFilter -> {
                                            showEmptySeller(
                                                EMPTY_SELLER_PROBLEM,
                                                getString(R.string.inbox_problem_empty),
                                                getString(R.string.inbox_problem_empty_subtitle)
                                            )
                                        }
                                        is TalkInboxFilter.TalkInboxAutorepliedFilter -> {
                                            showEmptySeller(
                                                EMPTY_SELLER_AUTOREPLIED,
                                                getString(R.string.inbox_autoreplied_empty_title),
                                                getString(R.string.inbox_autoreplied_empty_subtitle)
                                            )
                                        }
                                    }
                                    return@Observer
                                }
                            }
                            renderOldData(
                                inbox.map { inbox ->
                                    TalkInboxOldUiModel(
                                        inbox,
                                        isSellerView() || isSellerApp()
                                    )
                                },
                                it.data.hasNext
                            )
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
            }
        )
    }

    private fun observeSmartReplyDecommissionConfig() {
        viewModel.smartReplyDecommissionConfig.observe(viewLifecycleOwner) { config ->
            runCatching {
                binding?.tickerTalkInbox?.tickerTitle = config.tickerConfig.title
                binding?.tickerTalkInbox?.setHtmlDescription(config.tickerConfig.text)
                binding?.tickerTalkInbox?.showWithCondition(config.isSmartReviewDisabled)
            }.onFailure {
                binding?.tickerTalkInbox?.gone()
            }
        }
    }

    private fun renderOldData(data: List<TalkInboxOldUiModel>, hasNext: Boolean) {
        hideEmpty()
        binding?.talkInboxRecyclerView?.show()
        renderList(data, hasNext)
    }

    private fun renderData(data: List<TalkInboxUiModel>, hasNext: Boolean) {
        hideEmpty()
        binding?.talkInboxRecyclerView?.show()
        renderList(data, hasNext)
    }

    private fun initErrorPage() {
        binding?.inboxPageError?.apply {
            talkConnectionErrorRetryButton.setOnClickListener {
                loadInitialData()
            }
            readingImageError.loadImage(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
        }
    }

    private fun showFullPageError() {
        binding?.inboxPageError?.root?.show()
    }

    private fun hideFullPageError() {
        binding?.inboxPageError?.root?.hide()
    }

    private fun showErrorToaster() {
        view?.let {
            Toaster.build(
                binding?.talkInboxContainer ?: it,
                getString(R.string.inbox_toaster_connection_error),
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(R.string.talk_retry),
                View.OnClickListener { loadData(currentPage) }
            ).show()
        }
    }

    private fun showEmptyInbox() {
        binding?.talkInboxEmpty?.let {
            it.talkInboxEmptyTitle.text = getString(R.string.inbox_all_empty)
            if (isSellerApp()) {
                it.talkInboxEmptyImage.loadImage(EMPTY_SELLER_DISCUSSION)
                it.talkInboxEmptySubtitle.text = getString(R.string.inbox_empty_seller_subtitle)
            } else {
                it.talkInboxEmptyImage.loadImage(EMPTY_DISCUSSION_IMAGE)
                it.talkInboxEmptySubtitle.text = ""
            }
            it.root.show()
        }
    }

    private fun showEmptyFilter(title: String, subtitle: String) {
        binding?.apply {
            talkInboxEmpty.talkInboxEmptyImage.loadImage(EMPTY_DISCUSSION_IMAGE)
            talkInboxEmpty.talkInboxEmptyTitle.text = title
            talkInboxEmpty.talkInboxEmptySubtitle.text = subtitle
            talkInboxEmpty.root.show()
            talkInboxRecyclerView.hide()
        }
    }

    private fun showEmptySeller(imageUrl: String, title: String, subtitle: String) {
        binding?.apply {
            talkInboxEmpty.talkInboxEmptyImage.loadImage(imageUrl)
            talkInboxEmpty.talkInboxEmptyTitle.text = title
            talkInboxEmpty.talkInboxEmptySubtitle.text = subtitle
            talkInboxEmpty.root.show()
            talkInboxRecyclerView.hide()
        }
    }

    private fun hideEmpty() {
        binding?.talkInboxEmpty?.root?.hide()
    }

    private fun showFullPageLoading() {
        binding?.inboxPageLoading?.root?.show()
    }

    private fun hideFullPageLoading() {
        binding?.inboxPageLoading?.root?.hide()
    }

    private fun getDataFromArgument() {
        arguments?.getString(TAB_PARAM)?.let {
            inboxType = it
        }
    }

    private fun initSortFilter() {
        binding?.talkInboxSortFilter?.apply {
            sortFilterItems.removeAllViews()
            sortFilterPrefix.removeAllViews()
            if (isSellerApp()) {
                addItem(getSellerFilterList())
                setSettingsChipMargins()
                initCoachmark()
                return
            }
            addItem(getFilterList())
        }
    }

    private fun selectUnreadFilterFromCardSA() {
        if (GlobalConfig.isSellerApp()) {
            val filter = activity?.intent?.data?.getQueryParameter(FILTER_PARAM)
            if (filter == FILTER_UNREAD) {
                val unrespondedFilter =
                    binding?.talkInboxSortFilter?.chipItems?.getOrNull(INDEX_UNRESPONDED_FILTER)
                val problemFilter =
                    binding?.talkInboxSortFilter?.chipItems?.getOrNull(INDEX_PROBLEM_FILTER)
                val autoRepliedFilterChip =
                    binding?.talkInboxSortFilter?.chipItems?.getOrNull(INDEX_AUTOREPLY_FILTER)
                unrespondedFilter?.toggle()
                selectFilter(TalkInboxFilter.TalkInboxUnrespondedFilter(), shouldTrack = false)
                if (unrespondedFilter?.type == ChipsUnify.TYPE_SELECTED) {
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
        binding?.talkInboxSettingsIcon?.apply {
            setOnClickListener {
                goToSellerSettings()
            }
            updateSettingsIconVisibility()
        }
    }

    private fun setupTicker() {
        binding?.tickerTalkInbox?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                RouteManager.route(context, linkUrl.toString())
            }

            override fun onDismiss() {}
        })
    }

    private fun updateSettingsIconVisibility() {
        binding?.talkInboxSettingsIcon?.apply {
            if (isSellerApp()) {
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
        viewModel.setFilter(filter, isSellerApp() || isSellerView(), shouldTrack)
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
        if (isSellerApp()) {
            inboxType = if (containerListener?.role == RoleType.BUYER) {
                TalkInboxTab.BUYER_TAB
            } else {
                TalkInboxTab.SHOP_TAB
            }
        }
        viewModel.setInboxType(inboxType)
    }

    private fun isSellerView(): Boolean {
        return viewModel.getType() == TalkInboxTab.SHOP_OLD || viewModel.getType() == TalkInboxTab.SHOP_TAB
    }

    private fun isSellerApp(): Boolean {
        return GlobalConfig.isSellerApp()
    }

    private fun getProblemCount(): Long {
        return (viewModel.inboxList.value as? TalkInboxViewState.Success)?.data?.problemTotal ?: 0L
    }

    private fun getUnrespondedCount(): Long {
        return (viewModel.inboxList.value as? TalkInboxViewState.Success)?.data?.unrespondedTotal
            ?: 0L
    }

    private fun setFilterCounter() {
        if (isSellerApp()) {
            binding?.apply {
                if (getUnrespondedCount() != 0L) {
                    talkInboxSortFilter.chipItems?.getOrNull(0)?.title =
                        getString(R.string.inbox_unresponded) + " (${getUnrespondedCount()})"
                } else {
                    talkInboxSortFilter.chipItems?.getOrNull(0)?.title =
                        getString(R.string.inbox_unresponded)
                }
                if (getProblemCount() != 0L) {
                    talkInboxSortFilter.chipItems?.getOrNull(1)?.title =
                        getString(R.string.inbox_problem) + " (${getProblemCount()})"
                } else {
                    talkInboxSortFilter.chipItems?.getOrNull(1)?.title =
                        getString(R.string.inbox_problem)
                }
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
            if (this.isVisible) {
                coachMark?.showCoachMark(getCoachMarkItems())
            }
            updateSharedPrefs()
        }
    }

    private fun getCoachMarkItems(): ArrayList<CoachMark2Item> {
        val coachMarkItem = ArrayList<CoachMark2Item>()
        binding?.apply {
            if (talkInboxSortFilter.chipItems != null) {
                coachMarkItem.addAll(
                    listOf(
                        getCoachMarkItem(
                            talkInboxSortFilter.chipItems?.getOrNull(0)?.refChipUnify,
                            getString(R.string.inbox_coach_mark_filter_title),
                            getString(R.string.inbox_coach_mark_filter_subtitle),
                            this
                        ),
                        getCoachMarkItem(
                            talkInboxSortFilter.chipItems?.getOrNull(1)?.refChipUnify,
                            getString(R.string.inbox_coach_mark_reported_title),
                            getString(R.string.inbox_coach_mark_reported_subtitle),
                            this
                        ),
                        getCoachMarkItem(
                            talkInboxSortFilter.chipItems?.getOrNull(2)?.refChipUnify,
                            getString(R.string.inbox_coach_mark_smart_reply_title),
                            getString(R.string.inbox_coach_mark_smart_reply_subtitle),
                            this
                        )
                    )
                )
            }
        }
        return coachMarkItem
    }

    private fun setSettingsChipMargins() {
        binding?.talkInboxSortFilter?.chipItems?.getOrNull(SETTING_CHIP_POSITION)?.refChipUnify?.chip_text?.hide()
    }

    private fun getCoachMarkItem(
        anchorView: View?,
        title: String,
        subtitle: String,
        binding: FragmentTalkInboxBinding
    ): CoachMark2Item {
        return CoachMark2Item(
            anchorView ?: binding.talkInboxSortFilter,
            title,
            subtitle
        )
    }

    private fun hideToolbar() {
        if (GlobalConfig.isSellerApp()) {
            (activity as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                setSupportActionBar(binding?.headerTalkInbox)
            }
        }
    }

    private fun setupToolbar() {
        binding?.headerTalkInbox?.apply {
            setTitle(R.string.title_talk_discuss)
            if (GlobalConfig.isSellerApp()) {
                addRightIcon(0).apply {
                    clearImage()
                    setImageDrawable(
                        com.tokopedia.iconunify.getIconUnifyDrawable(
                            context,
                            IconUnify.SETTING,
                            ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_N700
                            )
                        )
                    )
                    setOnClickListener {
                        goToSellerSettings()
                    }
                    contentDescription = getString(R.string.menu_talk_inbox_setting_content_description)
                }
                show()
                binding?.talkInboxSettingsIcon?.hide()
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
        if (isSellerApp()) {
            return viewModel.getUnrespondedCount()
        }
        return viewModel.getUnreadCount()
    }

    private fun hitOnRoleChangeTracker() {
        if (shouldHitRoleChangedTracker) {
            talkInboxTracking.eventClickTab(
                inboxType,
                viewModel.getUserId(),
                viewModel.getShopId(),
                getCounterForTracking()
            )
            shouldHitRoleChangedTracker = false
        }
    }

    private fun initSharedPrefs() {
        talkInboxPreference = TalkInboxPreference(context)
    }
}
