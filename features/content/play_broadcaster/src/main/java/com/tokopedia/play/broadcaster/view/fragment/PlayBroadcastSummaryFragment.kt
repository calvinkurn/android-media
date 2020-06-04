package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.SummaryUiModel
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.view.adapter.TrafficMetricReportAdapter
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import kotlinx.android.synthetic.main.fragment_play_broadcaster_summary.*
import javax.inject.Inject


/**
 * @author by jessica on 26/05/20
 */

class PlayBroadcastSummaryFragment @Inject constructor(private val viewModelFactory: ViewModelFactory) : PlayBaseBroadcastFragment() {

    private lateinit var viewModel: PlayBroadcastSummaryViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private val playSummaryInfosAdapter = TrafficMetricReportAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastSummaryViewModel::class.java)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        arguments?.let {
            val channelId = it.getString(KEY_CHANNEL_ID)
            parentViewModel.getChannel(channelId)
            viewModel.getSummaryLiveReport(GraphqlHelper.loadRawString(resources, R.raw.gql_play_query_get_statistics), channelId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_broadcaster_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setUpView() {
        rv_play_summary_live_information.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv_play_summary_live_information.removeItemDecorations()
        rv_play_summary_live_information.adapter = playSummaryInfosAdapter
    }

    override fun getScreenName(): String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeTotalViews()
        observeTotalLikes()
        observeSummary()
        observeLiveTrafficMetrics()
    }

    private fun observeTotalViews() {
        parentViewModel.totalView.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeTotalLikes() {
        parentViewModel.totalLike.observe(viewLifecycleOwner, Observer(::setTotalLike))
    }

    private fun observeSummary() {
        viewModel.observableSummary.observe(viewLifecycleOwner, Observer {
            renderView(it)
        })
    }

    private fun observeLiveTrafficMetrics() {
        viewModel.observableTrafficMetricsUiModel.observe(viewLifecycleOwner, Observer(::renderTrafficMetrics))
    }

    private fun setTotalView(totalView: TotalViewUiModel) {
        if (viewModel.observableTrafficMetricsUiModel.value.isNullOrEmpty()) {
            playSummaryInfosAdapter.addTrafficMetrics(
                    TrafficMetricUiModel(trafficMetricEnum = TrafficMetricsEnum.TOTAL_VIEWS,
                            liveTrafficMetricCount = totalView.totalView))
        }
    }

    private fun setTotalLike(totalLike: TotalLikeUiModel) {
        if (viewModel.observableTrafficMetricsUiModel.value.isNullOrEmpty()) {
            playSummaryInfosAdapter.addTrafficMetrics(
                    TrafficMetricUiModel(
                            trafficMetricEnum = TrafficMetricsEnum.VIDEO_LIKES,
                            liveTrafficMetricCount = totalLike.totalLike))
        }
    }

    private fun setupView() {
        broadcastCoordinator.showActionBar(false)
    }

    private fun renderView(summaryUiModel: SummaryUiModel) {
        tv_play_summary_live_title.text = summaryUiModel.liveTitle

        if (summaryUiModel.tickerContent.showTicker) {
            ticker_live_summary.show()
            ticker_live_summary.tickerTitle = summaryUiModel.tickerContent.tickerTitle
            ticker_live_summary.setHtmlDescription(summaryUiModel.tickerContent.tickerDescription)
        } else ticker_live_summary.hide()

        iv_play_summary_cover.loadImage(summaryUiModel.coverImage)

        tv_play_summary_live_duration.text = summaryUiModel.liveDuration

        btn_play_summary_finish.setOnClickListener {
            //put action here
            RouteManager.route(requireContext(), summaryUiModel.finishRedirectUrl)
        }

        entranceAnimation(view)
    }

    private fun renderTrafficMetrics(trafficMetricUiModels: List<TrafficMetricUiModel>) {
        if (trafficMetricUiModels.isNotEmpty()) playSummaryInfosAdapter.updateTrafficMetrics(trafficMetricUiModels)
    }

    private fun entranceAnimation(v: View?) {
        v?.let {
            it.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    it.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    val animationOffset = resources.getInteger(R.integer.play_summary_layout_animation_offset).toFloat()
                    val animationDuration = resources.getInteger(R.integer.play_summary_layout_animation_duration_ms).toLong()

                    layout_live_summary_info.translationY = animationOffset
                    layout_live_summary_info_overflow.translationY = animationOffset
                    layout_live_summary_meta.translationY = -animationOffset
                    layout_live_summary_info.animate().translationYBy(-animationOffset).setDuration(animationDuration)
                    layout_live_summary_meta.animate().translationYBy(animationOffset).setDuration(animationDuration)
                    layout_live_summary_info_overflow.animate().translationYBy(-animationOffset).setDuration(animationDuration)
                }
            })
        }
    }

    private fun <T : RecyclerView> T.removeItemDecorations() {
        while (itemDecorationCount > 0) {
            removeItemDecorationAt(0)
        }
    }

    companion object {
        const val KEY_CHANNEL_ID = "key_channel_id"
    }
}