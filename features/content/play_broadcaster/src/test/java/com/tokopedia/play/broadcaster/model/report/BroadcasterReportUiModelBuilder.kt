package com.tokopedia.play.broadcaster.model.report

import com.tokopedia.play.broadcaster.ui.model.report.live.LiveReportSummaryUiModel
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import com.tokopedia.play.broadcaster.ui.model.report.product.ProductReportSummaryUiModel
import com.tokopedia.play.broadcaster.ui.model.report.product.ProductStatsUiModel

/**
 * Created by Jonathan Darwin on 18 March 2024
 */
class BroadcasterReportUiModelBuilder {

    fun buildLiveReportSummary(
        estimatedIncome: String = "Rp123.456",
        viewer: String = "1",
        totalViewer: String = "3",
        like: String = "5",
        timestamp: String = "123",
        isShuffled: Boolean = true,
    ) = LiveReportSummaryUiModel(
        liveStats = listOf(
            LiveStatsUiModel.EstimatedIncome(estimatedIncome),
            LiveStatsUiModel.Viewer(viewer),
            LiveStatsUiModel.TotalViewer(totalViewer),
            LiveStatsUiModel.Like(like),
        ).run {
            if (isShuffled) {
                shuffled()
            } else {
                this
            }
        },
        timestamp = timestamp,
    )

    fun buildProductReportSummary(
        productStatsSize: Int = 5,
    ): ProductReportSummaryUiModel {
        return ProductReportSummaryUiModel(
            totalStatsList = listOf(
                LiveStatsUiModel.EstimatedIncome(),
                LiveStatsUiModel.Visit(),
                LiveStatsUiModel.AddToCart(),
                LiveStatsUiModel.TotalSold(),
            ),
            productStatsList = List(productStatsSize) {
                ProductStatsUiModel(
                    id = it.toString(),
                    name = "Product $it",
                    imageUrl = "Image URL $it",
                    addToCart = it.toString(),
                    visitPdp = it.toString(),
                    productSoldQty = it.toString(),
                    estimatedIncome = it.toString(),
                )
            }
        )
    }
}
