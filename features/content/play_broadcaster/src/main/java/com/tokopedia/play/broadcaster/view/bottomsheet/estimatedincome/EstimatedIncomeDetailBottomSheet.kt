package com.tokopedia.play.broadcaster.view.bottomsheet.estimatedincome

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.stats.EstimatedIncomeDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.stats.LiveStatsUiModel
import com.tokopedia.play.broadcaster.ui.model.stats.ProductStatsUiModel
import com.tokopedia.play.broadcaster.view.compose.estimatedincome.EstimatedIncomeDetailLayout
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by Jonathan Darwin on 04 March 2024
 */
class EstimatedIncomeDetailBottomSheet : BottomSheetUnify() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.layoutParams = view.layoutParams.apply {
            height = (getScreenHeight() * 0.8).toInt()
        }
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
                    /** JOE TODO: remove mock data */
                    EstimatedIncomeDetailLayout(
                        estimatedIncomeDetail = EstimatedIncomeDetailUiModel(
                            totalStatsList = listOf(
                                LiveStatsUiModel.EstimatedIncome("Rp5.000.000"),
                                LiveStatsUiModel.Visit("1"),
                                LiveStatsUiModel.AddToCart("2"),
                                LiveStatsUiModel.TotalSold("3"),
                            ),
                            productStatsList = List(5) {
                                ProductStatsUiModel(
                                    id = it.toString(),
                                    name = "Product Name $it",
                                    imageUrl = "",
                                    addToCartFmt = "1",
                                    paymentVerifiedFmt = "2",
                                    visitPdpFmt = "3",
                                    productSoldQtyFmt = "4",
                                    estimatedIncomeFmt = "Rp5.000.000",
                                )
                            }
                        ),
                        onEstimatedIncomeClicked = {
                            showEstimatedIncomeInfoSheet()
                        }
                    )
                }
            }
        }

        setChild(composeView)
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
