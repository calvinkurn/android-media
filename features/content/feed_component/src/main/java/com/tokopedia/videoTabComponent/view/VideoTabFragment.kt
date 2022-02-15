package com.tokopedia.videoTabComponent.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.videoTabComponent.analytics.PlayWidgetAnalyticsListenerImp
import com.tokopedia.videoTabComponent.analytics.tracker.PlayAnalyticsTracker
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.videoTabComponent.di.DaggerVideoTabComponent
import com.tokopedia.videoTabComponent.domain.mapper.FeedPlayVideoTabMapper
import com.tokopedia.videoTabComponent.domain.model.data.PlayGetContentSlotResponse
import com.tokopedia.videoTabComponent.viewmodel.PlayFeedVideoTabViewModel
import com.tokopedia.videoTabComponent.viewmodel.VideoTabAdapter
import javax.inject.Inject

class VideoTabFragment : PlayWidgetListener, BaseDaggerFragment(), PlayWidgetAnalyticListener {

    private val rvWidget by lazy { view?.findViewById<RecyclerView>(R.id.rv_widget_sample_feed) }

    private lateinit var adapter: VideoTabAdapter
    private val playFeedVideoTabViewModel: PlayFeedVideoTabViewModel by lazy {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModelProvider.get(PlayFeedVideoTabViewModel::class.java)
    }
    private lateinit var playWidgetCoordinator: PlayWidgetCoordinator
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
        playWidgetCoordinator = PlayWidgetCoordinator(this).apply {
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
                    is Success -> onSuccessPlayTabData(
                        it.data.playGetContentSlot,
                        it.data.playGetContentSlot.meta.next_cursor
                    )
                    is Fail -> {
                        //TODO implement error case
//                        fetchFirstPage()
                    }
                }
            })
            getPlayDataRsp.observe(lifecycleOwner, Observer {
//                hideAdapterLoading()
                when (it) {
                    is Success -> onSuccessPlayTabData(
                        it.data.playGetContentSlot,
                        it.data.playGetContentSlot.meta.next_cursor
                    )
                    is Fail -> {
                        //TODO implement error case
//                        fetchFirstPage()
                    }
                }
            })

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analyticListener.visitVideoTabPageOnFeed(screenName)
        playFeedVideoTabViewModel.getInitialPlayData()
        setupView(view)
        playWidgetCoordinator.onResume()
    }

    private fun setupView(view: View) {
        adapter = VideoTabAdapter(
            coordinator = playWidgetCoordinator
        )
        endlessRecyclerViewScrollListener = getEndlessRecyclerViewScrollListener()
        endlessRecyclerViewScrollListener?.let {
            rvWidget?.addOnScrollListener(it)
            it.resetState()
        }

        rvWidget?.adapter = adapter

    }

    private fun onSuccessInitialPlayTabData(
        playDataResponse: PlayGetContentSlotResponse, cursor: String
    ) {
        adapter.setItemsAndAnimateChanges(FeedPlayVideoTabMapper.map(playDataResponse))

    }

    private fun onSuccessPlayTabData(playDataResponse: PlayGetContentSlotResponse, cursor: String) {
        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(playFeedVideoTabViewModel.currentCursor.isNotEmpty())
        adapter.addItemsAndAnimateChanges(FeedPlayVideoTabMapper.map(playDataResponse))

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

    private fun getEndlessRecyclerViewScrollListener(): EndlessRecyclerViewScrollListener? {
        return object : EndlessRecyclerViewScrollListener(rvWidget?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                playFeedVideoTabViewModel.getPlayData()
            }
        }
    }
}