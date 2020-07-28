package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.ui.model.ChannelInfoUiModel
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.SummaryInfoPartialView
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject


/**
 * @author by jessica on 26/05/20
 */

class PlayBroadcastSummaryFragment @Inject constructor(private val viewModelFactory: ViewModelFactory) : PlayBaseBroadcastFragment() {

    private lateinit var viewModel: PlayBroadcastSummaryViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var summaryInfoView: SummaryInfoPartialView
    private lateinit var btnFinish: UnifyButton

    override fun getScreenName(): String = "Play Summary Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastSummaryViewModel::class.java)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_broadcast_summary, container, false)

        observeChannelInfo()
        observeLiveDuration()
        observeLiveTrafficMetrics()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    private fun initView(view: View) {
        with(view) {
            summaryInfoView = SummaryInfoPartialView(this as ViewGroup)
            btnFinish = findViewById(R.id.btn_finish)
        }
    }

    private fun setupView(view: View) {
        broadcastCoordinator.showActionBar(false)
        btnFinish.setOnClickListener { activity?.finish() }
        summaryInfoView.entranceAnimation(view as ViewGroup)
    }

    private fun setChannelInfo(channelInfo: ChannelInfoUiModel) {
        summaryInfoView.setChannelTitle(channelInfo.title)
        summaryInfoView.setChannelCover(channelInfo.coverUrl)
    }

    private fun setSummaryInfo(dataList: List<TrafficMetricUiModel>) {
        summaryInfoView.setSummaryInfo(dataList)
    }

    private fun setLiveDuration(timeElapsed: String) {
        summaryInfoView.setLiveDuration(timeElapsed)
    }

    private fun observeChannelInfo() {
        parentViewModel.observableChannelInfo.observe(viewLifecycleOwner, Observer(this::setChannelInfo))
    }

    private fun observeLiveTrafficMetrics() {
        viewModel.observableTrafficMetrics.observe(viewLifecycleOwner, Observer(this::setSummaryInfo))
    }

    private fun observeLiveDuration() {
        parentViewModel.observableLiveInfoState.observe(viewLifecycleOwner, Observer {
            if (it is PlayPusherInfoState.Finish) setLiveDuration(it.timeElapsed)
        })
    }
}