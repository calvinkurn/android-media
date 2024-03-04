package com.tokopedia.play.broadcaster.view.compose.estimatedincome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.ui.model.stats.EstimatedIncomeDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.stats.LiveStatsUiModel
import com.tokopedia.play.broadcaster.ui.model.stats.ProductStatsUiModel
import com.tokopedia.play.broadcaster.view.compose.livestats.LiveStatsLayout

/**
 * Created by Jonathan Darwin on 04 March 2024
 */

@Composable
fun EstimatedIncomeDetailLayout(
    estimatedIncomeDetail: EstimatedIncomeDetailUiModel,
    onEstimatedIncomeClicked: () -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        item {
            LiveStatsLayout(
                liveStats = estimatedIncomeDetail.totalStatsList,
                gridCount = 2,
                onEstimatedIncomeClicked = onEstimatedIncomeClicked,
            )
        }

        items(estimatedIncomeDetail.productStatsList) {
            ProductStatsCardView(productStats = it)
        }
    }
}
@Composable
@Preview
private fun EstimatedIncomeLayoutPreview() {
    NestTheme {
        Surface {
            EstimatedIncomeDetailLayout(
                estimatedIncomeDetail = EstimatedIncomeDetailUiModel(
                    totalStatsList = listOf(
                        LiveStatsUiModel.EstimatedIncome("Rp5.000.000"),
                        LiveStatsUiModel.Visit("1"),
                        LiveStatsUiModel.AddToCart("2"),
                        LiveStatsUiModel.TotalSold("3"),
                    ),
                    productStatsList = listOf(
                        ProductStatsUiModel(
                            id = "123",
                            name = "Product Name 1",
                            imageUrl = "",
                            addToCartFmt = "1",
                            paymentVerifiedFmt = "2",
                            visitPdpFmt = "3",
                            productSoldQtyFmt = "4",
                            estimatedIncomeFmt = "Rp5.000.000",
                        ),
                        ProductStatsUiModel(
                            id = "456",
                            name = "Product Name 2",
                            imageUrl = "",
                            addToCartFmt = "1",
                            paymentVerifiedFmt = "2",
                            visitPdpFmt = "3",
                            productSoldQtyFmt = "4",
                            estimatedIncomeFmt = "Rp5.000.000",
                        )
                    )
                ),
                onEstimatedIncomeClicked = {}
            )
        }
    }
}
