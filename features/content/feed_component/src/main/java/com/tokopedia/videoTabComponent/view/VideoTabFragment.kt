package com.tokopedia.videoTabComponent.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.analytic.list.DefaultPlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.sample.adapter.feed.PlayWidgetSampleFeedAdapter
import com.tokopedia.play.widget.sample.analytic.PlayWidgetFeedSampleAnalytic
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.videoTabComponent.viewmodel.PlayFeedVideoTabViewModel
import javax.inject.Inject

class VideoTabFragment:  PlayWidgetListener, BaseDaggerFragment() {

    private val rvWidget by lazy { view?.findViewById<RecyclerView>(R.id.rv_widget_sample_feed) }

    private lateinit var adapter: PlayWidgetSampleFeedAdapter
    private lateinit var playFeedVideoTabViewModel: PlayFeedVideoTabViewModel


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory



    override fun getScreenName(): String {
        return "VideoTabFragment"
    }

    override fun initInjector() {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            playFeedVideoTabViewModel = viewModelProvider.get(PlayFeedVideoTabViewModel::class.java)
        }
        retainInstance = true
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_widget_sample_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        playFeedVideoTabViewModel.getPlayData()
        setupView(view)
    }
//    override fun initInjector() {
//         DaggerFeedPlusComponent.builder()
//                .baseAppComponent(
//                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
//                )
//                .build()
//                .inject(this)
//    }

    private fun setupView(view: View) {
        adapter = PlayWidgetSampleFeedAdapter(
                coordinator = PlayWidgetCoordinator(this).apply {
                    setListener(this@VideoTabFragment)
                    setAnalyticListener(DefaultPlayWidgetInListAnalyticListener(PlayWidgetFeedSampleAnalytic()))
                }
        )

        rvWidget?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onWidgetCardsScrollChanged(recyclerView)
                }
            }
        })

        rvWidget?.adapter = adapter
        adapter.setItemsAndAnimateChanges(getSampleWidgets())
    }

    private fun getSampleWidgets(): List<PlayFeedUiModel> {
        return listOf(
                PlayWidgetJumboUiModel(
                        PlayWidgetUiMock.getSamplePlayWidget(
                                items = listOf(
                                        PlayWidgetUiMock.getSampleChannelModel(PlayWidgetChannelType.Vod)
                                )
                        )
                ),
                PlayWidgetMediumUiModel(
                        PlayWidgetUiMock.getSamplePlayWidget()
                ),
                PlayWidgetSlotTabUiModel(
                        listOf(
                                Pair("Market Museum", true),
                                Pair("WIB", false),
                                Pair("Cantik Fest", false),
                                Pair("Untukmu", false),
                                Pair("Lagi Live", false),
                                Pair("Akan Datang", false),
                                Pair("Terbaru", false),
                        )
                ),
                PlayWidgetLargeUiModel(
                        PlayWidgetUiMock.getSamplePlayWidget()
                ),
                PlayWidgetSlotTabUiModel(
                        listOf(
                                Pair("Market Museum", true),
                                Pair("WIB", false),
                                Pair("Cantik Fest", false),
                                Pair("Untukmu", false),
                                Pair("Lagi Live", false),
                                Pair("Akan Datang", false),
                                Pair("Terbaru", false),
                        )
                ),
                PlayWidgetMediumUiModel(
                        PlayWidgetUiMock.getSamplePlayWidget(
                                title = "",
                                isActionVisible = false
                        )
                ),
                PlayWidgetLargeUiModel(
                        PlayWidgetUiMock.getSamplePlayWidget()
                ),
        )
    }

    override fun onToggleReminderClicked(
            view: PlayWidgetJumboView,
            channelId: String,
            reminderType: PlayWidgetReminderType,
            position: Int
    ) {
        Toast.makeText(context, "onToggleReminderClicked PlayWidgetJumboView $channelId", Toast.LENGTH_SHORT).show()
    }

    override fun onToggleReminderClicked(
            view: PlayWidgetLargeView,
            channelId: String,
            reminderType: PlayWidgetReminderType,
            position: Int
    ) {
        Toast.makeText(context, "onToggleReminderClicked PlayWidgetLargeView $channelId", Toast.LENGTH_SHORT).show()
    }

    override fun onToggleReminderClicked(
            view: PlayWidgetMediumView,
            channelId: String,
            reminderType: PlayWidgetReminderType,
            position: Int
    ) {
        Toast.makeText(context, "onToggleReminderClicked PlayWidgetMediumView $channelId", Toast.LENGTH_SHORT).show()
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        Toast.makeText(context, "onWidgetOpenAppLink $appLink", Toast.LENGTH_SHORT).show()
    }

    private fun onWidgetCardsScrollChanged(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstCompleteVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastCompleteVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()

        (firstCompleteVisiblePosition..lastCompleteVisiblePosition).map {
            Log.d("FeedPlayWidget", "Visible View: ${layoutManager.findViewByPosition(it)}")
        }
    }
}