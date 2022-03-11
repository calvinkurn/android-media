package com.tokopedia.play.broadcaster.view.fragment.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.event.UiString
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayInteractiveLeaderBoardBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.SummaryInfoViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


/**
 * @author by jessica on 26/05/20
 */
class PlayBroadcastReportFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val analytic: PlayBroadcastAnalytic,
) : PlayBaseBroadcastFragment(), SummaryInfoViewComponent.Listener {

    companion object {
        private const val FIRST_PLACE = 0
    }

    private var mListener: Listener? = null

    private lateinit var viewModel: PlayBroadcastSummaryViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var icClose: IconUnify
    private lateinit var btnPostVideo: UnifyButton
    private lateinit var loaderView: LoaderUnify

    private val summaryInfoView by viewComponent(isEagerInit = true) { SummaryInfoViewComponent(it, this) }

    override fun getScreenName(): String = "Play Report Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastSummaryViewModel::class.java)
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_broadcast_report, container, false)

        observeUiState()
        observeEvent()
        observeChannelInfo()
        observeInteractiveLeaderboardInfo()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    private fun initView(view: View) {
        with(view) {
            icClose = findViewById(R.id.ic_bro_summary_back)
            btnPostVideo = findViewById(R.id.btn_post_video)
            loaderView = findViewById(R.id.loader_summary)
        }
    }

    private fun setupView(view: View) {
        summaryInfoView.entranceAnimation(view as ViewGroup)

        icClose.setOnClickListener {
            viewModel.submitAction(PlayBroadcastSummaryAction.ClickCloseReportPage)
        }

        btnPostVideo.setOnClickListener {
            analytic.clickPostingVideoOnReportPage()
            viewModel.submitAction(PlayBroadcastSummaryAction.ClickPostVideo)
        }
    }

    /**
     * Observe
     */
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderDuration(it.prevValue?.liveReport?.duration, it.value.liveReport.duration)
                renderReport(it.prevValue?.liveReport?.trafficMetricsResult, it.value.liveReport.trafficMetricsResult)
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when(it) {
                    is PlayBroadcastSummaryEvent.ShowInfo -> {
                        val text = when(val uiString = it.uiString) {
                            is UiString.Text -> uiString.text
                            is UiString.Resource -> getString(uiString.resource)
                        }
                        view?.showToaster(
                            message = text,
                            actionLabel = getString(R.string.play_ok),
                        )
                    }
                    PlayBroadcastSummaryEvent.CloseReportPage -> requireActivity().onBackPressed()
                    PlayBroadcastSummaryEvent.OpenLeaderboardBottomSheet -> openInteractiveLeaderboardSheet()
                    PlayBroadcastSummaryEvent.OpenPostVideoPage -> mListener?.onClickPostButton()
                }
            }
        }
    }

    private fun observeChannelInfo() {
        parentViewModel.observableChannelInfo.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> setChannelInfo(it.data)
                else -> {}
            }
        }
    }

    private fun observeInteractiveLeaderboardInfo() {
        parentViewModel.observableLeaderboardInfo.observe(viewLifecycleOwner) {
            summaryInfoView.addTrafficMetric(
                TrafficMetricUiModel(
                    type = TrafficMetricType.GameParticipants,
                    count = if (it is NetworkResult.Success) it.data.totalParticipant else getString(R.string.play_interactive_leaderboard_default)
                ),
                FIRST_PLACE
            )
        }
    }

    private fun renderDuration(prev: LiveDurationUiModel?, value: LiveDurationUiModel) {
        if(prev == value) return

        summaryInfoView.setLiveDuration(value)
        btnPostVideo.isEnabled = value.isEligiblePostVideo
    }

    private fun renderReport(prev: NetworkResult<List<TrafficMetricUiModel>>?, value: NetworkResult<List<TrafficMetricUiModel>>) {
        if(prev == value) return

        when(value) {
            is NetworkResult.Loading -> {
                loaderView.visible()
                summaryInfoView.hideError()
            }
            is NetworkResult.Success -> {
                loaderView.gone()
                summaryInfoView.hideError()
                summaryInfoView.addTrafficMetrics(value.data)
            }
            is NetworkResult.Fail -> {
                loaderView.gone()
                summaryInfoView.showError { value.onRetry() }
                analytic.viewErrorOnReportPage(
                    channelId = parentViewModel.channelId,
                    titleChannel = parentViewModel.channelTitle,
                    errorMessage = value.error.localizedMessage?:getString(R.string.play_broadcaster_default_error)
                )
            }
        }
    }

    private fun setChannelInfo(channelInfo: ChannelInfoUiModel) {
        summaryInfoView.setChannelTitle(channelInfo.title)
        summaryInfoView.setChannelCover(channelInfo.coverUrl)
    }

    override fun onMetricClicked(view: SummaryInfoViewComponent, metricType: TrafficMetricType) {
         if (metricType.isGameParticipants) viewModel.submitAction(PlayBroadcastSummaryAction.ClickViewLeaderboard)
    }

    private fun openInteractiveLeaderboardSheet() {
        val fragmentFactory = childFragmentManager.fragmentFactory
        val leaderBoardBottomSheet = fragmentFactory.instantiate(
            requireContext().classLoader,
            PlayInteractiveLeaderBoardBottomSheet::class.java.name) as PlayInteractiveLeaderBoardBottomSheet
        leaderBoardBottomSheet.show(childFragmentManager)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onClickPostButton()
    }
}