package com.tokopedia.play.broadcaster.view.fragment.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.product.picker.seller.view.viewmodel.ViewModelFactoryProvider
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.FragmentPlayBroadcastReportBinding
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetPage
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetType
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetUiModel
import com.tokopedia.play.broadcaster.ui.model.livetovod.generateHtmlSpanText
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import com.tokopedia.play.broadcaster.ui.state.ChannelSummaryUiState
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroInteractiveBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.report.product.ProductReportSummaryBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.SummaryInfoViewComponent
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.content.common.R as contentcommonR

/**
 * @author by jessica on 26/05/20
 */
class PlayBroadcastReportFragment @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val analytic: PlayBroadcastAnalytic,
    private val router: Router,
) : PlayBaseBroadcastFragment(), SummaryInfoViewComponent.Listener {

    private var mListener: Listener? = null

    private val viewModel: PlayBroadcastSummaryViewModel by activityViewModels {
        (parentFragment as ViewModelFactoryProvider).getFactory()
    }
    private val parentViewModel: PlayBroadcastViewModel by activityViewModels {
        parentViewModelFactoryCreator.create(requireActivity())
    }

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

    private fun generateInAppLink(appLink: String): String {
        return getString(
            contentcommonR.string.up_webview_template,
            ApplinkConst.WEBVIEW,
            appLink,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBroadcastReportBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
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

        binding.btnDone.setOnClickListener {
            viewModel.submitAction(PlayBroadcastSummaryAction.ClickCloseReportPage)
        }
    }

    private fun setupObserver() {
        observeUiState()
        observeEvent()
    }

    /**
     * Observe
     */
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderChannelHeader(it.prevValue?.channelSummary, it.value.channelSummary)
                renderReport(
                    it.prevValue?.liveReport?.trafficMetricsResult,
                    it.value.liveReport.trafficMetricsResult
                )
                renderMetricHighlight(
                    it.prevValue?.liveReport?.trafficMetricHighlight,
                    it.value.liveReport.trafficMetricHighlight,
                )
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            parentViewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderTickerDisableLiveToVod(
                    prevState?.tickerBottomSheetConfig,
                    state.tickerBottomSheetConfig
                )
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when (it) {
                    PlayBroadcastSummaryEvent.CloseReportPage -> requireActivity().onBackPressed()
                    PlayBroadcastSummaryEvent.OpenLeaderboardBottomSheet -> openInteractiveLeaderboardSheet()
                    PlayBroadcastSummaryEvent.OpenPostVideoPage -> mListener?.onClickPostButton()
                    else -> {}
                }
            }
        }
    }

    private fun checkTickerLiveToVodConfig() {
        parentViewModel.submitAction(
            PlayBroadcastAction.GetTickerBottomSheetConfig(
                page = TickerBottomSheetPage.LIVE_REPORT,
            )
        )
    }

    private fun renderChannelHeader(prev: ChannelSummaryUiState?, value: ChannelSummaryUiState) {
        if (prev == value || value.isEmpty()) return

        summaryInfoView.setChannelHeader(value)
    }

    private fun renderReport(
        prev: NetworkResult<List<TrafficMetricUiModel>>?,
        value: NetworkResult<List<TrafficMetricUiModel>>
    ) {
        if (prev == value) return

        when (value) {
            is NetworkResult.Loading -> {
                binding.layoutPlaySummaryInfo.loaderSummary.visible()
                summaryInfoView.hideError()
            }

            is NetworkResult.Success -> {
                binding.layoutPlaySummaryInfo.loaderSummary.gone()
                summaryInfoView.hideError()
                summaryInfoView.addTrafficMetrics(value.data)
                checkTickerLiveToVodConfig()
            }

            is NetworkResult.Fail -> {
                binding.layoutPlaySummaryInfo.loaderSummary.gone()
                summaryInfoView.showError { value.onRetry() }
                analytic.viewErrorOnReportPage(
                    channelId = viewModel.channelId,
                    titleChannel = viewModel.channelTitle,
                    errorMessage = value.error.localizedMessage
                        ?: getString(R.string.play_broadcaster_default_error)
                )
            }

            else -> {
                //no-op
            }
        }
    }

    private fun renderMetricHighlight(
        prev: NetworkResult<List<LiveStatsUiModel>>?,
        curr: NetworkResult<List<LiveStatsUiModel>>,
    ) {
        if (prev == curr) return

        when (curr) {
            is NetworkResult.Success -> {
                summaryInfoView.setTrafficMetricsHighlight(curr.data)
            }
            else -> {
                summaryInfoView.setTrafficMetricsHighlight(emptyList())
            }
        }
    }

    private fun renderTickerDisableLiveToVod(
        prev: TickerBottomSheetUiModel?,
        state: TickerBottomSheetUiModel,
    ) {
        if (prev == state || state.page != TickerBottomSheetPage.LIVE_REPORT) return

        when (state.type) {
            TickerBottomSheetType.TICKER -> showTickerDisableLiveToVod(state)
            else -> return
        }
    }

    private fun showTickerDisableLiveToVod(state: TickerBottomSheetUiModel) {
        binding.tickerReportPage.apply {
            tickerTitle = state.mainText.first().title
            setHtmlDescription(
                generateHtmlSpanText(
                    fullText = state.mainText.first().description,
                    action = state.mainText.first().action,
                )
            )
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    router.route(
                        requireContext(),
                        generateInAppLink(linkUrl.toString()),
                    )
                }

                override fun onDismiss() {
                    parentViewModel.submitAction(
                        PlayBroadcastAction.SetLiveToVodPref(
                            type = TickerBottomSheetType.TICKER,
                            page = TickerBottomSheetPage.LIVE_REPORT,
                        )
                    )
                }

            })
            visible()
        }
    }

    override fun onMetricClicked(view: SummaryInfoViewComponent, liveStats: LiveStatsUiModel) {
        when (liveStats) {
            is LiveStatsUiModel.EstimatedIncome -> {
                openProductReportSummarySheet()
            }
            is LiveStatsUiModel.GameParticipant -> {
                analytic.clickInteractiveParticipantDetail(
                    channelID = viewModel.channelId,
                    channelTitle = viewModel.channelTitle,
                )
                viewModel.submitAction(PlayBroadcastSummaryAction.ClickViewLeaderboard)
            }
            else -> {}
        }
    }

    private fun openInteractiveLeaderboardSheet() {
        val leaderboardReportBottomSheet = PlayBroInteractiveBottomSheet.setupReportLeaderboard(
            childFragmentManager,
            requireContext().classLoader
        )
        leaderboardReportBottomSheet.show(childFragmentManager)
    }

    private fun openProductReportSummarySheet() {
        ProductReportSummaryBottomSheet.getFragment(
            childFragmentManager,
            requireContext().classLoader
        ).show(childFragmentManager)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onClickPostButton()
    }
}
