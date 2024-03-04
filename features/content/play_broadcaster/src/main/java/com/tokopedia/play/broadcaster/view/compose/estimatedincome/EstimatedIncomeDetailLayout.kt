package com.tokopedia.play.broadcaster.view.compose.estimatedincome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.ui.model.stats.EstimatedIncomeDetailUiModel
import com.tokopedia.play.broadcaster.ui.model.stats.LiveStatsUiModel
import com.tokopedia.play.broadcaster.ui.model.stats.ProductStatsUiModel
import com.tokopedia.play.broadcaster.view.compose.livestats.LiveStatsLayout
import com.tokopedia.play.broadcaster.view.compose.livestats.LiveStatsShimmer
import com.tokopedia.play_common.model.result.NetworkResult

/**
 * Created by Jonathan Darwin on 04 March 2024
 */

@Composable
fun EstimatedIncomeDetailLayout(
    estimatedIncomeDetail: NetworkResult<EstimatedIncomeDetailUiModel>,
    onEstimatedIncomeClicked: () -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        when(estimatedIncomeDetail) {
            is NetworkResult.Loading -> {
                item {
                    LiveStatsShimmer()
                }

                items(3) {
                    ProductStatsShimmer()
                }
            }
            is NetworkResult.Success -> {
                item {
                    LiveStatsLayout(
                        liveStats = estimatedIncomeDetail.data.totalStatsList,
                        gridCount = 2,
                        onEstimatedIncomeClicked = onEstimatedIncomeClicked,
                    )
                }

                items(estimatedIncomeDetail.data.productStatsList) {
                    ProductStatsCardView(productStats = it)
                }
            }
            is NetworkResult.Fail -> {
                /** JOE TODO: handle error state */
            }
            else -> {}
        }
    }
}

@Composable
@Preview
private fun EstimatedIncomeDetailLayoutPreview() {
    NestTheme {
        Surface {
            EstimatedIncomeDetailLayout(
                estimatedIncomeDetail = NetworkResult.Success(
                    EstimatedIncomeDetailUiModel(
                        totalStatsList = listOf(
                            LiveStatsUiModel.EstimatedIncome(value ="Rp5.000.000", clickableIcon = IconUnify.INFORMATION),
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
                    )
                ),
                onEstimatedIncomeClicked = {}
            )
        }
    }
}

@Composable
@Preview
private fun EstimatedIncomeDetailShimmerPreview() {
    NestTheme {
        Surface {
            EstimatedIncomeDetailLayout(
                estimatedIncomeDetail = NetworkResult.Loading,
                onEstimatedIncomeClicked = {}
            )
        }
    }
}
