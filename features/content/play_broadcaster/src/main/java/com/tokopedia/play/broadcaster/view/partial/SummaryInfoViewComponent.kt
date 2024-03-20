package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.LayoutPlaySummaryInfoBinding
import com.tokopedia.play.broadcaster.ui.itemdecoration.MetricReportItemDecoration
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricType
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsCardModel
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import com.tokopedia.play.broadcaster.ui.state.ChannelSummaryUiState
import com.tokopedia.play.broadcaster.ui.viewholder.TrafficMetricViewHolder
import com.tokopedia.play.broadcaster.view.adapter.TrafficMetricReportAdapter
import com.tokopedia.play.broadcaster.view.compose.report.live.LiveReportSummaryLayout
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


/**
 * Created by mzennis on 18/06/20.
 */
class SummaryInfoViewComponent(
    container: ViewGroup,
    private val binding: LayoutPlaySummaryInfoBinding,
    listener: Listener
) : ViewComponent(container, binding.layoutSummaryContent.id) {

    private val animationOffset = container.resources.getInteger(R.integer.play_summary_layout_animation_offset).toFloat()
    private val animationDuration = container.resources.getInteger(R.integer.play_summary_layout_animation_duration_ms).toLong()

    private val trafficMetricReportAdapter = TrafficMetricReportAdapter(object : TrafficMetricViewHolder.Listener {
        override fun onLabelClicked(metricType: TrafficMetricType) {}
    })

    private val _metricHighlight = MutableStateFlow<List<LiveStatsUiModel>>(emptyList())

    init {
        binding.rvInfo.apply {
            layoutManager = LinearLayoutManager(container.context, RecyclerView.VERTICAL, false)
            addItemDecoration(MetricReportItemDecoration(context))
            adapter = trafficMetricReportAdapter
        }

        binding.viewLiveStatsHighlight.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                val metricHighlight by _metricHighlight.collectAsStateWithLifecycle()

                NestTheme(isOverrideStatusBarColor = false) {
                    LiveReportSummaryLayout(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        listData = metricHighlight.map {
                            when (it) {
                                is LiveStatsUiModel.EstimatedIncome -> {
                                    LiveStatsCardModel.Clickable(
                                        liveStats = it,
                                        clickableIcon = IconUnify.CHEVRON_RIGHT,
                                        clickArea = LiveStatsCardModel.Clickable.ClickArea.Full,
                                        onClick = {
                                            listener.onMetricClicked(this@SummaryInfoViewComponent, it)
                                        }
                                    )
                                }
                                is LiveStatsUiModel.GameParticipant -> {
                                    LiveStatsCardModel.Clickable(
                                        liveStats = it,
                                        clickableIcon = IconUnify.CHEVRON_RIGHT,
                                        clickArea = LiveStatsCardModel.Clickable.ClickArea.Full,
                                        onClick = {
                                            listener.onMetricClicked(this@SummaryInfoViewComponent, it)
                                        }
                                    )
                                }
                                else -> {
                                    LiveStatsCardModel.NotClickable(it)
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    fun entranceAnimation(container: ViewGroup) {
        container.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                container.viewTreeObserver.removeOnGlobalLayoutListener(this)

                binding.flInfo.translationY = animationOffset
                binding.clBroSummaryMeta.translationY = -animationOffset
                binding.flInfo.animate().translationYBy(-animationOffset).duration = animationDuration
                binding.clBroSummaryMeta.animate().translationYBy(animationOffset).duration = animationDuration
            }
        })
    }

    fun setChannelHeader(data: ChannelSummaryUiState) {
        binding.ivBroSummaryCover.setImageUrl(data.coverUrl)
        binding.tvBroSummaryLiveTitle.text = data.title
        binding.tvBroSummaryDuration.text = data.duration
        binding.tvBroSummaryDate.text = data.date
        binding.imageAuthor.setImageUrl(data.author.iconUrl)
        binding.tvAuthor.text = data.author.name
    }

    fun addTrafficMetrics(dataList: List<TrafficMetricUiModel>) {
        trafficMetricReportAdapter.setItemsAndAnimateChanges(dataList)
    }

    fun setTrafficMetricsHighlight(dataList: List<LiveStatsUiModel>) {
        _metricHighlight.update { dataList }
    }

    fun showError(onRetry: () -> Unit) {
        binding.layoutSummaryError.root.show()
        binding.layoutSummaryError.tvErrorTryAgain.setOnClickListener { onRetry() }
    }

    fun hideError() {
        binding.layoutSummaryError.root.hide()
    }

    interface Listener {
        fun onMetricClicked(view: SummaryInfoViewComponent, liveStats: LiveStatsUiModel)
    }
}
