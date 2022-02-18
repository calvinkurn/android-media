package com.tokopedia.videoTabComponent.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.videoTabComponent.analytics.PlayWidgetAnalyticsListenerImp
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker
import com.tokopedia.videoTabComponent.callback.PlaySlotTabCallback
import com.tokopedia.videoTabComponent.di.DaggerVideoTabComponent
import com.tokopedia.videoTabComponent.domain.mapper.FeedPlayVideoTabMapper
import com.tokopedia.videoTabComponent.domain.model.data.PlayGetContentSlotResponse
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlotTabMenuUiModel
import com.tokopedia.videoTabComponent.domain.model.data.VideoPageParams
import com.tokopedia.videoTabComponent.util.PlayFeedSharedPrefsUtil.clearTabMenuPosition
import com.tokopedia.videoTabComponent.util.PlayFeedSharedPrefsUtil.getTabMenuPosition
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab
import com.tokopedia.videoTabComponent.view.custom.FeedPlayStickyHeaderRecyclerView
import com.tokopedia.videoTabComponent.viewmodel.PlayFeedVideoTabViewModel
import com.tokopedia.videoTabComponent.viewmodel.VideoTabAdapter
import javax.inject.Inject

class VideoTabFragment : PlayWidgetListener, BaseDaggerFragment(), PlayWidgetAnalyticListener,
    PlaySlotTabCallback {

    private val rvWidget by lazy { view?.findViewById<FeedPlayStickyHeaderRecyclerView>(R.id.rv_widget_sample_feed) }

    private lateinit var adapter: VideoTabAdapter
    private val playFeedVideoTabViewModel: PlayFeedVideoTabViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModelProvider.get(PlayFeedVideoTabViewModel::class.java)
    }
    private lateinit var playWidgetCoordinator: PlayWidgetCoordinatorVideoTab
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    companion object {

        private const val OPEN_PLAY_CHANNEL = 1858
        private const val EXTRA_PLAY_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        private const val EXTRA_PLAY_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"

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
    lateinit var playWidgetAnalyticsListenerImp: PlayWidgetAnalyticsListenerImp

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
        return inflater.inflate(R.layout.fragment_play_widget_sample_feed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lifecycleOwner: LifecycleOwner = viewLifecycleOwner
        playFeedVideoTabViewModel.run {
            getPlayInitialDataRsp.observe(lifecycleOwner, Observer {
//                hideAdapterLoading()
                when (it) {
                    is Success -> {
                        setAdapter()
                        playWidgetAnalyticsListenerImp.filterCategory =
                            FeedPlayVideoTabMapper.getTabData(it.data.playGetContentSlot)[0].title
                        onSuccessPlayTabData(
                            it.data.playGetContentSlot,
                            it.data.playGetContentSlot.meta.next_cursor
                        )
                    }
                    is Fail -> {
                        //TODO implement error case
//                        fetchFirstPage()
                    }
                }
            })
            getPlayDataForSlotRsp.observe(lifecycleOwner) {
                when (it) {
                    is Success -> {
                        onSuccessPlayTabDataFromChipClick(
                            it.data.playGetContentSlot,
                            it.data.playGetContentSlot.meta.next_cursor
                        )
                    }
                    is Fail -> {

                    }
                }
            }
            getPlayDataRsp.observe(lifecycleOwner, Observer {
                when (it) {
                    is Success -> {
                        onSuccessPlayTabData(
                            it.data.playGetContentSlot,
                            it.data.playGetContentSlot.meta.next_cursor
                        )
                    }
                    is Fail -> {
                        //TODO implement error case

                    }
                }
            })

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analyticListener.visitVideoTabPageOnFeed(2)
        playFeedVideoTabViewModel.getInitialPlayData()
        setupView(view)
        playWidgetCoordinator.onResume()
    }

    private fun setupView(view: View) {
        endlessRecyclerViewScrollListener = getEndlessRecyclerViewScrollListener()
        endlessRecyclerViewScrollListener?.let {
            rvWidget?.addOnScrollListener(it)
            it.resetState()
        }

        setAdapter()
    }

    private fun setAdapter() {
        rvWidget?.apply {
            adapter = VideoTabAdapter(
                playWidgetCoordinator, this@VideoTabFragment, requireActivity()
            ).also {
                setAdapter(it)
            }
        }
    }

    private fun onSuccessPlayTabData(playDataResponse: PlayGetContentSlotResponse, cursor: String) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(playFeedVideoTabViewModel.currentCursor.isNotEmpty())
        adapter.addItemsAndAnimateChanges(
            FeedPlayVideoTabMapper.map(
                playDataResponse.data,
                playDataResponse.meta
            )
        )

    }

    private fun onSuccessPlayTabDataFromChipClick(
        playDataResponse: PlayGetContentSlotResponse,
        cursor: String
    ) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(playFeedVideoTabViewModel.currentCursor.isNotEmpty())
        val mappedData = FeedPlayVideoTabMapper.map(playDataResponse.data, playDataResponse.meta)

        adapter.updateList(mappedData)
        //adapter.setItemsAndAnimateChanges(mappedData)

        /*mappedData.forEachIndexed { index, playFeedUiModel ->
            if(playFeedUiModel is PlaySlotTabMenuUiModel) {
                rvWidget?.scrollLayout(index)
            }
        }*/
    }

    override fun onToggleReminderClicked(
        view: PlayWidgetJumboView,
        channelId: String,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {
        Toast.makeText(
            context,
            "onToggleReminderClicked PlayWidgetJumboView $channelId",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onToggleReminderClicked(
        view: PlayWidgetLargeView,
        channelId: String,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {
        Toast.makeText(
            context,
            "onToggleReminderClicked PlayWidgetLargeView $channelId",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onToggleReminderClicked(
        view: PlayWidgetMediumView,
        channelId: String,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {
        Toast.makeText(
            context,
            "onToggleReminderClicked PlayWidgetMediumView $channelId",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        val intent = RouteManager.getIntent(requireContext(), appLink)
        startActivityForResult(intent, OPEN_PLAY_CHANNEL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) {
            return
        }

        when (requestCode) {

            OPEN_PLAY_CHANNEL -> {
                val channelId = data.getStringExtra(EXTRA_PLAY_CHANNEL_ID)
                val totalView = data.getStringExtra(EXTRA_PLAY_TOTAL_VIEW)
//                updatePlayWidgetTotalView(channelId, totalView)
            }

            else -> {
            }
        }
    }

    private fun playWidgetOnVisibilityChanged(
        isViewResumed: Boolean = if (view == null) false else viewLifecycleOwner.lifecycle.currentState.isAtLeast(
            Lifecycle.State.RESUMED
        ),
        isUserVisibleHint: Boolean = userVisibleHint,
        isParentHidden: Boolean = parentFragment?.isHidden ?: true
    ) {
        if (::playWidgetCoordinator.isInitialized) {
            val isViewVisible = isViewResumed && isUserVisibleHint && !isParentHidden

            if (isViewVisible) playWidgetCoordinator.onResume()
            else playWidgetCoordinator.onPause()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        playWidgetOnVisibilityChanged(
            isUserVisibleHint = true
        )
    }

    override fun onPause() {
//        playWidgetOnVisibilityChanged(isViewResumed = false)
        super.onPause()
    }

    override fun onResume() {
//        playWidgetOnVisibilityChanged(isViewResumed = true)
        super.onResume()
    }

    //click listener for tab menu slot
    override fun clickTabMenu(item: PlaySlotTabMenuUiModel.Item, position: Int) {
        playWidgetAnalyticsListenerImp.filterCategory = item.label
        callAPiOnTabCLick(item)
        analyticListener.clickOnFilterChipsInVideoTab(item.label)
        adapter.slotPosition?.let { rvWidget?.scrollToPosition(it) }
    }

    override fun impressTabMenu(item: PlaySlotTabMenuUiModel.Item) {
        analyticListener.impressOnFilterChipsInVideoTab(item.label)
    }

    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener? {
        return object : EndlessRecyclerViewScrollListener(rvWidget?.getLayoutManager()) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                playFeedVideoTabViewModel.getPlayData(false, null)
            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                rvWidget?.setShouldShowStickyHeaderValue(false)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    rvWidget?.setShouldShowStickyHeaderValue(true)
            }
        }
    }
    private fun callAPiOnTabCLick(item: PlaySlotTabMenuUiModel.Item){
        val videoPageParams = VideoPageParams(cursor = "" , sourceId = item.sourceId, sourceType = item.sourceType, group = item.group)
        playFeedVideoTabViewModel.getPlayData(isClickFromTabMenu = true, videoPageParams = videoPageParams)

    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().clearTabMenuPosition()
    }
}