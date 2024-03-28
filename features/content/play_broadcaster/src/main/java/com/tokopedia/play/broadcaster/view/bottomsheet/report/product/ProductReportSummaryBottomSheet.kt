package com.tokopedia.play.broadcaster.view.bottomsheet.report.product

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import com.tokopedia.play.broadcaster.ui.model.report.product.ProductReportSummaryUiModel
import com.tokopedia.play.broadcaster.ui.model.report.product.ProductStatsUiModel
import com.tokopedia.play.broadcaster.view.compose.report.product.ProductReportSummaryLayout
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 04 March 2024
 */
class ProductReportSummaryBottomSheet @Inject constructor(

): BottomSheetUnify() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.layoutParams = view.layoutParams.apply {
            height = (getScreenHeight() * HEIGHT_PERCENTAGE).toInt()
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
                    val state by remember {
//                        mutableStateOf(NetworkResult.Fail(UnknownHostException()))
//                        mutableStateOf(NetworkResult.Loading)
                        mutableStateOf(NetworkResult.Success(
                            ProductReportSummaryUiModel(
                                totalStatsList = listOf(
                                    LiveStatsUiModel.EstimatedIncome("Rp5.000.000", clickableIcon = IconUnify.INFORMATION),
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
                            )
                        ))
                    }

                    ProductReportSummaryLayout(
                        productReportSummary = state,
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
