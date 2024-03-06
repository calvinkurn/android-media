package com.tokopedia.play.broadcaster.view.bottomsheet.estimatedincome

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.model.stats.EstimatedIncomeDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.stats.LiveStatsUiModel
import com.tokopedia.play.broadcaster.ui.model.stats.ProductStatsUiModel
import com.tokopedia.play.broadcaster.view.compose.estimatedincome.EstimatedIncomeDetailLayout
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 04 March 2024
 */
class EstimatedIncomeDetailBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
): BottomSheetUnify() {

    private val parentViewModel by activityViewModels<PlayBroadcastViewModel> {
        parentViewModelFactoryCreator.create(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomSheetHeight(view)

        parentViewModel.submitAction(PlayBroadcastAction.GetEstimatedIncomeDetail)
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

                    EstimatedIncomeDetailLayout(
                        estimatedIncomeDetail = uiState.estimatedIncomeDetail,
                        onEstimatedIncomeClicked = {
                            showEstimatedIncomeInfoSheet()
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
        ): EstimatedIncomeDetailBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? EstimatedIncomeDetailBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                EstimatedIncomeDetailBottomSheet::class.java.name
            ) as EstimatedIncomeDetailBottomSheet
        }
    }
}
