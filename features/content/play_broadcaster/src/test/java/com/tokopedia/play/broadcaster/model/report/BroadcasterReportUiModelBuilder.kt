package com.tokopedia.play.broadcaster.model.report

import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import com.tokopedia.play.broadcaster.ui.model.report.product.ProductReportSummaryUiModel
import com.tokopedia.play.broadcaster.ui.model.report.product.ProductStatsUiModel

/**
 * Created by Jonathan Darwin on 18 March 2024
 */
class BroadcasterReportUiModelBuilder {

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
