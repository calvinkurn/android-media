package com.tokopedia.videoTabComponent.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.feedcomponent.util.manager.FeedFloatingButtonManager
import com.tokopedia.feedcomponent.view.base.FeedPlusContainerListener
import com.tokopedia.feedcomponent.view.base.FeedPlusTabParentFragment
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.pref.PlayWidgetPreference
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.analytics.PlayWidgetAnalyticsListenerImp
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker
import com.tokopedia.videoTabComponent.callback.PlaySlotTabCallback
import com.tokopedia.videoTabComponent.di.DaggerVideoTabComponent
import com.tokopedia.videoTabComponent.domain.mapper.FeedPlayVideoTabMapper
import com.tokopedia.videoTabComponent.domain.model.data.*
import com.tokopedia.videoTabComponent.util.PlayFeedSharedPrefsUtil.clearTabMenuPosition
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab
import com.tokopedia.videoTabComponent.view.custom.FeedPlayStickyHeaderRecyclerView
import com.tokopedia.videoTabComponent.view.uimodel.SelectedPlayWidgetCard
import com.tokopedia.videoTabComponent.view.viewholder.PlayFeedWidgetViewHolder
import com.tokopedia.videoTabComponent.viewmodel.PlayFeedVideoTabViewModel
import com.tokopedia.videoTabComponent.viewmodel.VideoTabAdapter
import timber.log.Timber
import javax.inject.Inject

class VideoTabFragment :
    PlayWidgetListener,
    BaseDaggerFragment(),
    PlayWidgetAnalyticListener,
    PlaySlotTabCallback,
    SwipeRefreshLayout.OnRefreshListener,
    FeedPlusTabParentFragment {

    private var rvWidget: FeedPlayStickyHeaderRecyclerView? = null
    private var swipeToRefresh: SwipeToRefresh? = null

    private lateinit var adapter: VideoTabAdapter
    private val playFeedVideoTabViewModel: PlayFeedVideoTabViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModelProvider.get(PlayFeedVideoTabViewModel::class.java)
    }
    private lateinit var playWidgetCoordinator: PlayWidgetCoordinatorVideoTab
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var isScrollingUp = false

    companion object {
        const val TIME_DELAY_TO_SHOW_STICKY_HEADER_TAB_VIEW = 3000L
        private const val REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME = 257
        private const val ARGS_FEED_VIDEO_TAB_SELECT_CHIP = "tab"
        private const val REQUEST_CODE_PLAY_ROOM = 123
        private const val EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"
        private const val EXTRA_IS_REMINDER = "EXTRA_IS_REMINDER"
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"

        @JvmStatic
        fun newInstance(bundle: Bundle?): VideoTabFragment {
            val fragment = VideoTabFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analyticListener: PlayAnalyticsTracker

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var playWidgetAnalyticsListenerImp: PlayWidgetAnalyticsListenerImp

    @Inject
    lateinit var playWidgetPreference: PlayWidgetPreference

    @Inject
    lateinit var feedFloatingButtonManager: FeedFloatingButtonManager

    private var mContainerListener: FeedPlusContainerListener? = null

    override fun getScreenName(): String {
        return "VideoTabFragment"
    }

    override fun initInjector() {
        DaggerVideoTabComponent.builder()
            .baseAppComponent(
                (requireContext().applicationContext as BaseMainApplication).baseAppComponent
            )
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVar()
        retainInstance = true
    }

    private fun initVar() {
        requireActivity().clearTabMenuPosition()

        playWidgetAnalyticsListenerImp.setOnClickChannelCard { channelId, position ->
            playFeedVideoTabViewModel.selectedPlayWidgetCard = SelectedPlayWidgetCard(channelId, position)
        }

        playWidgetCoordinator = PlayWidgetCoordinatorVideoTab(this).apply {
            setListener(this@VideoTabFragment)
            setAnalyticListener(playWidgetAnalyticsListenerImp)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_play, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lifecycleOwner: LifecycleOwner = viewLifecycleOwner
        playFeedVideoTabViewModel.run {
            reminderObservable.observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> onSuccessReminderSet(it.data)
                        is Fail -> {
                            val errorMsg = if (it.throwable is CustomUiMessageThrowable) {
                                getString(
                                    (it.throwable as? CustomUiMessageThrowable)?.errorMessageId
                                        ?: com.tokopedia.feedcomponent.R.string.feed_video_tab_error_reminder
                                )
                            } else {
                                it.throwable.message
                                    ?: getString(com.tokopedia.feedcomponent.R.string.feed_video_tab_error_reminder)
                            }
                            showToast(errorMsg, Toaster.TYPE_ERROR)
                        }
                    }
                }
            )

            playWidgetReminderEvent.observe(viewLifecycleOwner) {
                startActivityForResult(
                    RouteManager.getIntent(context, ApplinkConst.LOGIN),
                    REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME
                )
            }
            getPlayInitialDataRsp.observe(lifecycleOwner) {
                when (it) {
                    is Success -> {
                        setAdapter()
                        playWidgetAnalyticsListenerImp.filterCategory =
                            FeedPlayVideoTabMapper.getTabData(it.data.playGetContentSlot)
                                .firstOrNull()?.items?.firstOrNull()?.label ?: ""
                        onSuccessInitialPlayTabData(
                            it.data.playGetContentSlot
                        )
                    }
                    is Fail -> {
                        hideLoading()
                        Timber.e(it.throwable)
                    }
                }
            }
            getPlayDataForSlotRsp.observe(lifecycleOwner) {
                hideLoading()
                when (it) {
                    is Success -> {
                        onSuccessPlayTabDataFromChipClick(
                            it.data.playGetContentSlot
                        )
                    }
                    is Fail -> {
                        Timber.e(it.throwable)
                    }
                }
            }
            getPlayDataRsp.observe(lifecycleOwner) {
                hideLoading()
                when (it) {
                    is Success -> {
                        onSuccessPlayTabData(
                            it.data.playGetContentSlot
                        )
                    }
                    is Fail -> {
                        Timber.e(it.throwable)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvWidget = view.findViewById(R.id.rv_feed_play)
        swipeToRefresh = rvWidget?.findViewById(R.id.video_tab_swipe_refresh_layout)

        swipeToRefresh?.isRefreshing = true
        swipeToRefresh?.isEnabled = false
        playFeedVideoTabViewModel.getInitialPlayData(
            arguments?.getString(
                ARGS_FEED_VIDEO_TAB_SELECT_CHIP
            ) ?: ""
        )
        feedFloatingButtonManager.setInitialData(parentFragment)
        setupView(view)
        playWidgetCoordinator.onResume()
    }

    private fun setupView(view: View) {
        endlessRecyclerViewScrollListener = getEndlessRecyclerViewScrollListener()
        endlessRecyclerViewScrollListener?.let {
            rvWidget?.addOnScrollListener(it)
            it.resetState()
        }
        rvWidget?.addOnScrollListener(feedFloatingButtonManager.scrollListener)
        swipeToRefresh?.setOnRefreshListener(this)

        setAdapter()
        rvWidget?.let { feedFloatingButtonManager.setDelayForExpandFab(it.recyclerView) }
    }

    private fun setAdapter() {
        rvWidget?.apply {
            adapter = VideoTabAdapter(
                playWidgetCoordinator,
                this@VideoTabFragment,
                requireActivity()
            ).also {
                setAdapter(it)
            }
        }
    }
    private fun onSuccessInitialPlayTabData(playDataResponse: PlayGetContentSlotResponse) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(playFeedVideoTabViewModel.currentCursor.isNotEmpty())
        adapter.setItemsAndAnimateChanges(
            FeedPlayVideoTabMapper.map(
                playDataResponse.data,
                playDataResponse.meta,
                position = playFeedVideoTabViewModel.selectedTabDefaultPosition,
                shopId = userSession.shopId,
                playWidgetPreference = playWidgetPreference
            )
        )
    }
    private fun onSuccessPlayTabData(playDataResponse: PlayGetContentSlotResponse) {
        swipeToRefresh?.isEnabled = true
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(playFeedVideoTabViewModel.currentCursor.isNotEmpty())
        adapter.addItemsAndAnimateChanges(
            FeedPlayVideoTabMapper.map(
                playDataResponse.data,
                playDataResponse.meta,
                shopId = userSession.shopId,
                playWidgetPreference = playWidgetPreference
            )
        )
    }

    private fun onSuccessPlayTabDataFromChipClick(
        playDataResponse: PlayGetContentSlotResponse
    ) {
        if (adapter.slotPosition == null) {
            adapter.updateSlotPosition()
        }
        adapter.slotPosition?.let { rvWidget?.scrollToPosition(it) }
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(playFeedVideoTabViewModel.currentCursor.isNotEmpty())
        val mappedData = FeedPlayVideoTabMapper.map(
            playDataResponse.data,
            playDataResponse.meta,
            shopId = userSession.shopId,
            playWidgetPreference = playWidgetPreference
        )

        adapter.updateList(mappedData, playFeedVideoTabViewModel.currentSourceId, playFeedVideoTabViewModel.currentSourceType, playWidgetAnalyticsListenerImp.filterCategory)
    }

    override fun onToggleReminderClicked(
        view: PlayWidgetJumboView,
        channelId: String,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {
        playFeedVideoTabViewModel.updatePlayWidgetToggleReminder(channelId, reminderType, position)
    }

    override fun onToggleReminderClicked(
        view: PlayWidgetMediumView,
        channelId: String,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {
        playFeedVideoTabViewModel.updatePlayWidgetToggleReminder(channelId, reminderType, position)
    }

    override fun onToggleReminderClicked(view: PlayWidgetLargeView, channelId: String, reminderType: PlayWidgetReminderType, position: Int) {
        super.onToggleReminderClicked(view, channelId, reminderType, position)
        playFeedVideoTabViewModel.updatePlayWidgetToggleReminder(channelId, reminderType, position)
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        val intent = RouteManager.getIntent(requireContext(), appLink)
        startActivityForResult(intent, REQUEST_CODE_PLAY_ROOM)
    }

    private fun playWidgetOnVisibilityChanged(
        isViewResumed: Boolean = if (view == null) {
            false
        } else {
            viewLifecycleOwner.lifecycle.currentState.isAtLeast(
                Lifecycle.State.RESUMED
            )
        },
        isUserVisibleHint: Boolean = userVisibleHint,
        isParentHidden: Boolean = parentFragment?.isHidden ?: true
    ) {
        if (::playWidgetCoordinator.isInitialized) {
            val isViewVisible = isViewResumed && isUserVisibleHint && !isParentHidden

            if (isViewVisible) {
                playWidgetCoordinator.onResume()
                autoplayJumboWidget()
            } else {
                playWidgetCoordinator.onPause()
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        playWidgetOnVisibilityChanged(
            isUserVisibleHint = true
        )
    }

    // click listener for tab menu slot
    override fun clickTabMenu(item: PlaySlotTabMenuUiModel.Item, position: Int) {
        playWidgetAnalyticsListenerImp.filterCategory = item.label
        if (rvWidget?.isStickyRecyclerViewIsEnabled() == true) {
            adapter.updateSlotTabViewHolderState()
        }
        analyticListener.clickOnFilterChipsInVideoTab(item.label)
        callAPiOnTabCLick(item)
    }

    override fun impressTabMenu(item: PlaySlotTabMenuUiModel.Item) {
        analyticListener.impressOnFilterChipsInVideoTab(item.label)
    }

    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener? {
        return object : EndlessRecyclerViewScrollListener(rvWidget?.getLayoutManager()) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                playFeedVideoTabViewModel.getPlayData(false, null)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val f = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    val l = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (rvWidget?.getViewHolderAtPosition(l) != null && rvWidget?.getViewHolderAtPosition(l) is PlayFeedWidgetViewHolder.Large) {
                        val vh = rvWidget?.getViewHolderAtPosition(l) as PlayFeedWidgetViewHolder.Large
                        val recyclerViewLargeWidget = vh.itemView.findViewById<RecyclerView>(com.tokopedia.play.widget.R.id.play_widget_recycler_view)
                        recyclerViewLargeWidget?.let { playWidgetCoordinator.configureAutoplayForLargeAndJumboWidget(it) }
                    }
                    if (rvWidget?.getViewHolderAtPosition(f) != null && rvWidget?.getViewHolderAtPosition(f) is PlayFeedWidgetViewHolder.Jumbo) {
                        val vh = rvWidget?.getViewHolderAtPosition(f) as PlayFeedWidgetViewHolder.Jumbo
                        val recyclerViewJumboWidget = vh.itemView.findViewById<RecyclerView>(com.tokopedia.play.widget.R.id.play_widget_recycler_view)
                        recyclerViewJumboWidget?.let { playWidgetCoordinator.configureAutoplayForLargeAndJumboWidget(it) }
                    }
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isScrollingUp) {
                    rvWidget?.setShouldShowStickyHeaderValue(true, TIME_DELAY_TO_SHOW_STICKY_HEADER_TAB_VIEW)
                }
            }
        }
    }
    fun autoplayJumboWidget() {
        rvWidget?.let {
            val f = (it.getLayoutManager() as LinearLayoutManager).findFirstVisibleItemPosition()

            if (it.getViewHolderAtPosition(f) != null && it.getViewHolderAtPosition(f) is PlayFeedWidgetViewHolder.Jumbo) {
                val vh = it.getViewHolderAtPosition(f) as PlayFeedWidgetViewHolder.Jumbo
                val recyclerView = vh.itemView.findViewById<RecyclerView>(com.tokopedia.play.widget.R.id.play_widget_recycler_view)
                recyclerView?.let { playWidgetCoordinator.configureAutoplayForLargeAndJumboWidget(recyclerView) }
            }
        }
    }

    private fun callAPiOnTabCLick(item: PlaySlotTabMenuUiModel.Item) {
        val videoPageParams = VideoPageParams(cursor = "", sourceId = item.sourceId, sourceType = item.sourceType, group = item.group)
        playFeedVideoTabViewModel.getPlayData(isClickFromTabMenu = true, videoPageParams = videoPageParams)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvWidget?.removeOnScrollListener(feedFloatingButtonManager.scrollListener)
        feedFloatingButtonManager.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::playWidgetCoordinator.isInitialized) {
            playWidgetCoordinator.onDestroy()
        }
        requireActivity().clearTabMenuPosition()
    }
    private fun showToast(message: String, type: Int, actionText: String? = null) {
        if (actionText?.isEmpty() == false) {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type, actionText).show()
        } else {
            Toaster.build(requireView(), message, Toaster.LENGTH_LONG, type).show()
        }
    }
    private fun onSuccessReminderSet(playWidgetFeedReminderInfoData: PlayWidgetFeedReminderInfoData) {
        showToast(
            if (playWidgetFeedReminderInfoData.reminderType.reminded) {
                getString(com.tokopedia.feedcomponent.R.string.feed_video_tab_success_add_reminder)
            } else {
                getString(com.tokopedia.feedcomponent.R.string.feed_video_tab_success_remove_reminder)
            },
            Toaster.TYPE_NORMAL
        )

        val adapterPositionForItem = adapter.getPositionInList(playWidgetFeedReminderInfoData.channelId, playWidgetFeedReminderInfoData.itemPosition)
        adapter.updatePlayWidgetInfo(
            adapterPositionForItem,
            playWidgetFeedReminderInfoData.channelId,
            null,
            playWidgetFeedReminderInfoData.reminderType == PlayWidgetReminderType.Reminded
        )
    }

    override fun onResume() {
        super.onResume()
        playWidgetOnVisibilityChanged(true)
    }

    override fun onPause() {
        super.onPause()
        playWidgetOnVisibilityChanged(true)
    }

    override fun onRefresh() {
        playFeedVideoTabViewModel.setDefaultValuesOnRefresh()
        requireActivity().clearTabMenuPosition()
        swipeToRefresh?.isRefreshing = true
        swipeToRefresh?.isEnabled = false
        playFeedVideoTabViewModel.getInitialPlayData()

        mContainerListener?.onChildRefresh()
    }

    private fun hideLoading() {
        swipeToRefresh?.isRefreshing = false
        swipeToRefresh?.isEnabled = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME -> if (resultCode == Activity.RESULT_OK) {
                val playWidgetFeedReminderInfoData = playFeedVideoTabViewModel.playWidgetReminderEvent.value
                if (playWidgetFeedReminderInfoData != null) playFeedVideoTabViewModel.updatePlayWidgetToggleReminder(playWidgetFeedReminderInfoData.channelId, playWidgetFeedReminderInfoData.reminderType, playWidgetFeedReminderInfoData.itemPosition)
            }
            REQUEST_CODE_PLAY_ROOM -> {
                if (resultCode == Activity.RESULT_OK) {
                    val selectedCard = playFeedVideoTabViewModel.selectedPlayWidgetCard

                    val channelId = data?.extras?.getString(EXTRA_CHANNEL_ID) ?: selectedCard.channelId
                    val totalView = data?.extras?.getString(EXTRA_TOTAL_VIEW)
                    val isReminderSet = data?.extras?.getBoolean(EXTRA_IS_REMINDER, false)

                    val position = adapter.getPositionInList(channelId, selectedCard.position)

                    adapter.updatePlayWidgetInfo(position, channelId, totalView, isReminderSet)

                    playFeedVideoTabViewModel.selectedPlayWidgetCard = SelectedPlayWidgetCard.Empty
                }
            }
        }
    }

    override fun setContainerListener(listener: FeedPlusContainerListener) {
        this.mContainerListener = listener
    }
}
