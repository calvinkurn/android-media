package com.tokopedia.play.widget.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.analytic.list.DefaultPlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.sample.adapter.feed.PlayWidgetSampleFeedAdapter
import com.tokopedia.play.widget.sample.analytic.PlayWidgetFeedSampleAnalytic
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by meyta.taliti on 28/01/22.
 */
class PlayWidgetSampleFeedFragment : TkpdBaseV4Fragment() {

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
            analyticListener = DefaultPlayWidgetInListAnalyticListener(PlayWidgetFeedSampleAnalytic())
        )

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
}