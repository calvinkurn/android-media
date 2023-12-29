package com.tokopedia.play.widget.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.sample.adapter.common.PlayWidgetSampleCommonAdapter
import com.tokopedia.play.widget.sample.analytic.PlayWidgetSampleAnalytic
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMock
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetType
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by meyta.taliti on 27/01/22.
 */
class PlayWidgetSampleCommonFragment : TkpdBaseV4Fragment() {

    private val rvWidgetSample by lazy { view?.findViewById<RecyclerView>(R.id.rv_play_widget_sample) }

    private lateinit var adapter: PlayWidgetSampleCommonAdapter

    override fun getScreenName(): String {
        return "PlayWidgetSampleCommonFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_widget_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun setupView(view: View) {
        val sampleData = getSampleData()
        val coordinatorMap = List(sampleData.size) {
            PlayWidgetCoordinator(this, autoHandleLifecycleMethod = false).apply {
                setListener(object : PlayWidgetListener {
                    override fun onReminderClicked(
                        view: PlayWidgetCarouselView,
                        channelId: String,
                        reminderType: PlayWidgetReminderType,
                        position: Int
                    ) {
                        val oldItems = adapter.getItems()
                        val newItems = oldItems.map { state ->
                            state.copy(
                                model = state.model.copy(
                                    items = state.model.items.map { item ->
                                        if (item is PlayWidgetChannelUiModel && item.channelId == channelId) {
                                            item.copy(reminderType = reminderType)
                                        } else {
                                            item
                                        }
                                    }
                                )
                            )
                        }
                        adapter.setItemsAndAnimateChanges(newItems)
                    }
                })
                setAnalyticListener(PlayWidgetSampleAnalytic(requireContext()))
            }
        }.associateWith { null }

        adapter = PlayWidgetSampleCommonAdapter(coordinatorMap)

        rvWidgetSample?.itemAnimator = null
        rvWidgetSample?.adapter = adapter
        adapter.setItemsAndAnimateChanges(getSampleData())
    }

    private fun getSampleData(): List<PlayWidgetState> {
        return listOf(
//            PlayWidgetState(
//                model = PlayWidgetUiMock.getSamplePlayWidget(
//                    items = listOf(
//                        PlayWidgetUiMock.getSampleChannelModel(PlayWidgetChannelType.Vod)
//                    )
//                ),
//                widgetType = PlayWidgetType.Jumbo,
//                isLoading = false,
//            ),
//            PlayWidgetState(
//                model = PlayWidgetUiMock.getSamplePlayWidget(),
//                widgetType = PlayWidgetType.Small,
//                isLoading = false,
//            ),
//            PlayWidgetState(
//                model = PlayWidgetUiMock.getSamplePlayWidget(),
//                widgetType = PlayWidgetType.Medium,
//                isLoading = false
//            ),
            PlayWidgetState(
                model = PlayWidgetUiMock.getSamplePlayWidget(
                    items = PlayWidgetUiMock.getSampleItemData().mapIndexed { index, data ->
                        if (data is PlayWidgetChannelUiModel) data.copy(
                            channelId = index.toString(),
                            title = "Channel $index"
                        )
                        else data
                    }
                ),
                widgetType = PlayWidgetType.Carousel,
                isLoading = false
            )
        )
    }
}
