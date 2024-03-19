package com.tokopedia.play.broadcaster.view.bottomsheet.report.product

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.report.PlayBroadcastReportAnalytic
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.view.compose.report.product.ProductReportSummaryLayout
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 04 March 2024
 */
class ProductReportSummaryBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val reportAnalyticFactory: PlayBroadcastReportAnalytic.Factory,
): BottomSheetUnify() {

    private val parentViewModel by activityViewModels<PlayBroadcastViewModel> {
        parentViewModelFactoryCreator.create(requireActivity())
    }

    private val reportAnalytic = reportAnalyticFactory.create(
        getAccount = { parentViewModel.selectedAccount },
        getChannelId = { parentViewModel.channelId },
        getChannelTitle = { parentViewModel.channelTitle }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            when (fragment) {
                is EstimatedIncomeInfoBottomSheet -> {
                    fragment.setListener(object : EstimatedIncomeInfoBottomSheet.Listener {
                        override fun onImpress() {
                            reportAnalytic.impressEstimatedIncomeInfoBottomSheet()
                        }
                    })
                }
            }
        }
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomSheetHeight(view)

        reportAnalytic.impressProductReportBottomSheet()
        parentViewModel.submitAction(PlayBroadcastAction.GetProductReportSummary)
    }

    private fun setupBottomSheet() {
        setTitle(getString(R.string.play_broadcaster_live_stats_estimated_income_label))

        clearContentPadding = true

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                NestTheme(
                    isOverrideStatusBarColor = false,
                ) {
                    val uiState by parentViewModel.uiState.collectAsStateWithLifecycle()

                    ProductReportSummaryLayout(
                        productReportSummary = uiState.productReportSummary,
                        onEstimatedIncomeClicked = {
                            reportAnalytic.clickEstimatedIncomeInfoIcon()
                            showEstimatedIncomeInfoSheet()
                        },
                        onImpressErrorState = {
                            reportAnalytic.impressProductReportBottomSheetError()
                        }
                    )
                }
            }
        }

        setChild(composeView)
    }

    private fun setupBottomSheetHeight(view: View) {
        view.layoutParams = view.layoutParams.apply {
            height = (getScreenHeight() * HEIGHT_PERCENTAGE).toInt()
        }
    }

    private fun showEstimatedIncomeInfoSheet() {
        EstimatedIncomeInfoBottomSheet.getFragment(
            childFragmentManager,
            requireContext().classLoader
        ).show(childFragmentManager)
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "EstimatedIncomeDetailBottomSheet"

        private const val HEIGHT_PERCENTAGE = 0.8

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): ProductReportSummaryBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ProductReportSummaryBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ProductReportSummaryBottomSheet::class.java.name
            ) as ProductReportSummaryBottomSheet
        }
    }
}
