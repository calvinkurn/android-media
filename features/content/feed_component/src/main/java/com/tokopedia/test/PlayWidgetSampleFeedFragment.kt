package com.tokopedia.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.analytic.list.DefaultPlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.sample.adapter.feed.PlayWidgetSampleFeedAdapter
import com.tokopedia.play.widget.sample.analytic.PlayWidgetFeedSampleAnalytic
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.sample.data.ContentSlotResponse
import com.tokopedia.play.widget.sample.data.PlayGetContentSlotResponse
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.videoTabComponent.domain.mapper.FeedPlayVideoTabMapper

/**
 * Created by meyta.taliti on 28/01/22.
 */
class PlayWidgetSampleFeedFragment : TkpdBaseV4Fragment(), PlayWidgetListener {

    private val rvWidget by lazy { view?.findViewById<RecyclerView>(R.id.rv_widget_sample_feed) }

    private lateinit var adapter: PlayWidgetSampleFeedAdapter

    override fun getScreenName(): String {
        return "PlayWidgetSampleFeedFragment"
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
        setupView(view)
    }

    private fun setupView(view: View) {
        adapter = PlayWidgetSampleFeedAdapter(
            coordinator = PlayWidgetCoordinator(this).apply {
                setListener(this@PlayWidgetSampleFeedFragment)
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

        adapter.setItemsAndAnimateChanges(
            FeedPlayVideoTabMapper.map(
                Gson().fromJson(dummyJson, ContentSlotResponse::class.java).data.playGetContentSlot,
                ""
            )
        )
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