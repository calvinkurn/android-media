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
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.view.adapter.TrafficMetricReportAdapter
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
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

    private val trafficMetricReportAdapter = TrafficMetricReportAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastSummaryViewModel::class.java)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        arguments?.let { it ->
            it.getString(KEY_CHANNEL_ID)?.let {channelId ->
                parentViewModel.getChannel(channelId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_broadcaster_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        broadcastCoordinator.showActionBar(false)
        rv_play_summary_live_information.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv_play_summary_live_information.removeItemDecorations()
        rv_play_summary_live_information.adapter = trafficMetricReportAdapter
        renderDuration()
        setUpFinishButton()
    }

    override fun getScreenName(): String = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeChannelInfo()
        observeTotalViews()
        observeTotalLikes()
        observeLiveTrafficMetrics()
    }

    private fun observeChannelInfo() {
        parentViewModel.observableChannelInfo.observe(viewLifecycleOwner, Observer {
            viewModel.getLiveTrafficMetrics(it.channelId)
            renderTitleAndCover(it)
        })
    }

    private fun observeTotalViews() {
        parentViewModel.observableTotalView.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeTotalLikes() {
        parentViewModel.observableTotalLike.observe(viewLifecycleOwner, Observer(::setTotalLike))
    }

    private fun observeLiveTrafficMetrics() {
        viewModel.observableTrafficMetricsUiModel.observe(viewLifecycleOwner, Observer(::renderTrafficMetrics))
    }

    private fun setTotalView(totalView: TotalViewUiModel) {
        if (viewModel.observableTrafficMetricsUiModel.value.isNullOrEmpty()) {
            trafficMetricReportAdapter.addItemAndAnimateChanges(
                    TrafficMetricUiModel(trafficMetricEnum = TrafficMetricsEnum.TOTAL_VIEWS,
                            liveTrafficMetricCount = totalView.totalView))
        }
    }

    private fun setTotalLike(totalLike: TotalLikeUiModel) {
        if (viewModel.observableTrafficMetricsUiModel.value.isNullOrEmpty()) {
            trafficMetricReportAdapter.addItemAndAnimateChanges(
                    TrafficMetricUiModel(
                            trafficMetricEnum = TrafficMetricsEnum.VIDEO_LIKES,
                            liveTrafficMetricCount = totalLike.totalLike))
        }
    }

    private fun renderTitleAndCover(channelInfoUiModel: ChannelInfoUiModel) {
        tv_play_summary_live_title.text = channelInfoUiModel.title
        iv_play_summary_cover.loadImage(channelInfoUiModel.coverUrl)
        entranceAnimation(view)
    }

    private fun renderDuration() {
        arguments?.let { it ->
            it.getString(KEY_LIVE_DURATION)?.let { duration -> tv_play_summary_live_duration.text = duration }
        }
    }

    private fun renderTicker(title: String, description: String) {
        ticker_live_summary.show()
        ticker_live_summary.tickerTitle = title
        ticker_live_summary.setHtmlDescription(description)
    }

    private fun setUpFinishButton() {
        btn_play_summary_finish.setOnClickListener {
            //put action here
            RouteManager.route(requireContext(), "")
            activity?.finish()
        }
    }

    private fun renderTrafficMetrics(trafficMetricUiModels: List<TrafficMetricUiModel>) {
        if (trafficMetricUiModels.isNotEmpty()) {
            trafficMetricReportAdapter.clearAllItemsAndAnimateChanges()
            trafficMetricReportAdapter.setItemsAndAnimateChanges(trafficMetricUiModels)
        }
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
        const val KEY_LIVE_DURATION = "key_live_duration"
    }
}