package com.tokopedia.play.broadcaster.view.fragment.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.FragmentPlayBroadcastReportBinding
import com.tokopedia.play.broadcaster.setup.product.viewmodel.ViewModelFactoryProvider
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.event.UiString
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.state.ChannelSummaryUiState
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayInteractiveLeaderBoardBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.SummaryInfoViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
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

    private var mListener: Listener? = null

    private lateinit var viewModel: PlayBroadcastSummaryViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private var _binding: FragmentPlayBroadcastReportBinding? = null
    private val binding: FragmentPlayBroadcastReportBinding
        get() = _binding!!

    private val summaryInfoView by viewComponent(isEagerInit = true) {
        SummaryInfoViewComponent(it, binding.layoutPlaySummaryInfo, this)
    }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    override fun getScreenName(): String = "Play Report Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), (parentFragment as ViewModelFactoryProvider).getFactory()).get(PlayBroadcastSummaryViewModel::class.java)
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlayBroadcastReportBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView(view: View) {
        summaryInfoView.entranceAnimation(view as ViewGroup)

        binding.icBroSummaryBack.setOnClickListener {
            viewModel.submitAction(PlayBroadcastSummaryAction.ClickCloseReportPage)
        }

        binding.btnPostVideo.setOnClickListener {
            analytic.clickPostingVideoOnReportPage()
            viewModel.submitAction(PlayBroadcastSummaryAction.ClickPostVideo)
        }
    }

    private fun setupObserver() {
        observeUiState()
        observeEvent()
        observeInteractiveLeaderboardInfo()
    }

    /**
     * Observe
     */
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderChannelHeader(it.prevValue?.channelSummary, it.value.channelSummary)
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
                        toaster.showToaster(
                            message = text,
                            actionLabel = getString(R.string.play_ok),
                        )
                    }
                    PlayBroadcastSummaryEvent.CloseReportPage -> requireActivity().onBackPressed()
                    PlayBroadcastSummaryEvent.OpenLeaderboardBottomSheet -> openInteractiveLeaderboardSheet()
                    PlayBroadcastSummaryEvent.OpenPostVideoPage -> mListener?.onClickPostButton()
                    else -> { }
                }
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

    private fun renderChannelHeader(prev: ChannelSummaryUiState?, value: ChannelSummaryUiState) {
        if(prev == value || value.isEmpty()) return

        summaryInfoView.setChannelHeader(value)
        binding.btnPostVideo.isEnabled = value.isEligiblePostVideo
    }

    private fun renderReport(prev: NetworkResult<List<TrafficMetricUiModel>>?, value: NetworkResult<List<TrafficMetricUiModel>>) {
        if(prev == value) return

        when(value) {
            is NetworkResult.Loading -> {
                binding.layoutPlaySummaryInfo.loaderSummary.visible()
                summaryInfoView.hideError()
            }
            is NetworkResult.Success -> {
                binding.layoutPlaySummaryInfo.loaderSummary.gone()
                summaryInfoView.hideError()
                summaryInfoView.addTrafficMetrics(value.data)
            }
            is NetworkResult.Fail -> {
                binding.layoutPlaySummaryInfo.loaderSummary.gone()
                summaryInfoView.showError { value.onRetry() }
                analytic.viewErrorOnReportPage(
                    channelId = viewModel.channelId,
                    titleChannel = viewModel.channelTitle,
                    errorMessage = value.error.localizedMessage?:getString(R.string.play_broadcaster_default_error)
                )
            }
        }
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

    companion object {
        private const val FIRST_PLACE = 0
    }

    interface Listener {
        fun onClickPostButton()
    }
}