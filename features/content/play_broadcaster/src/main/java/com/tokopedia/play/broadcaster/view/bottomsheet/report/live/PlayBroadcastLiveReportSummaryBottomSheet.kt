package com.tokopedia.play.broadcaster.view.bottomsheet.report.live

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.analytic.report.PlayBroadcastReportAnalytic
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsCardModel
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import com.tokopedia.play.broadcaster.view.compose.report.live.LiveReportSummaryLayout
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 01 March 2024
 */
class PlayBroadcastLiveReportSummaryBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val reportAnalyticFactory: PlayBroadcastReportAnalytic.Factory,
) : BottomSheetUnify() {

    private var mListener: Listener? = null

    private val parentViewModel by activityViewModels<PlayBroadcastViewModel> {
        parentViewModelFactoryCreator.create(requireActivity())
    }

    private val reportAnalytic = reportAnalyticFactory.create(
        getAccount = { parentViewModel.selectedAccount },
        getChannelId = { parentViewModel.channelId },
        getChannelTitle = { parentViewModel.channelTitle }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reportAnalytic.impressLiveReportBottomSheet()
        parentViewModel.submitAction(PlayBroadcastAction.GetLiveReportSummary)
    }

    private fun setupBottomSheet() {
        clearContentPadding = true

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                val uiState by parentViewModel.uiState.collectAsStateWithLifecycle()

                NestTheme(
                    isOverrideStatusBarColor = false,
                ) {
                    Surface {
                        LiveReportSummaryLayout(
                            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                            gridCount = 2,
                            listData = uiState.liveReportSummary.liveStats.map {
                                when (it) {
                                    is LiveStatsUiModel.EstimatedIncome -> {
                                        LiveStatsCardModel.Clickable(
                                            liveStats = it,
                                            clickableIcon = IconUnify.CHEVRON_RIGHT,
                                            clickArea = LiveStatsCardModel.Clickable.ClickArea.Full,
                                            onClick = {
                                                reportAnalytic.clickEstimatedIncomeCardOnLiveReport()
                                                mListener?.onEstimatedIncomeClicked()
                                            }
                                        )
                                    }
                                    else -> {
                                        LiveStatsCardModel.NotClickable(
                                            liveStats = it
                                        )
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }

        setChild(composeView)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    interface Listener {
        fun onEstimatedIncomeClicked()
    }

    companion object {
        private const val TAG = "PlayBroadcastLiveStatsBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayBroadcastLiveReportSummaryBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayBroadcastLiveReportSummaryBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayBroadcastLiveReportSummaryBottomSheet::class.java.name
            ) as PlayBroadcastLiveReportSummaryBottomSheet
        }
    }
}
