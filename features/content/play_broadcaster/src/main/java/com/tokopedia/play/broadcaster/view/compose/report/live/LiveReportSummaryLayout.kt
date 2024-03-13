package com.tokopedia.play.broadcaster.view.compose.report.live

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsCardModel
import com.tokopedia.play.broadcaster.ui.model.report.live.LiveStatsUiModel
import kotlin.math.ceil

/**
 * Created by Jonathan Darwin on 01 March 2024
 */
@Composable
fun LiveReportSummaryLayout(
    listData: List<LiveStatsCardModel>,
    modifier: Modifier = Modifier,
    gridCount: Int = 2,
) {
    LiveReportSummaryContainer(
        modifier = modifier,
        itemCount = listData.size,
        gridCount = gridCount,
        content = {
            val curr = listData[it.coerceAtMost(listData.size - 1)]

            LiveStatsCardView(
                modifier = Modifier.weight(1f),
                liveStatsCardModel = curr,
            )
        }
    )
}

@Composable
fun LiveReportSummaryShimmer(
    modifier: Modifier = Modifier,
    itemCount: Int = 4,
    gridCount: Int = 2,
) {
    LiveReportSummaryContainer(
        modifier = modifier,
        itemCount = itemCount,
        gridCount = gridCount,
        content = {
            NestLoader(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp),
                variant = NestLoaderType.Shimmer(NestShimmerType.Rect(12.dp)),
            )
        }
    )
}

@Composable
private fun LiveReportSummaryContainer(
    itemCount: Int,
    gridCount: Int,
    content: @Composable RowScope.(idx: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (gridCount <= 0 || itemCount <= 0) return

    Column(
        modifier = modifier,
    ) {
        val rowCount = ceil(itemCount / gridCount.toFloat()).toInt()

        for (i in 0 until rowCount) {
            val start = i * gridCount
            val end = (start + gridCount - 1)

            Row(
                modifier = Modifier.padding(
                    bottom = if (i == rowCount - 1) 0.dp else 8.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (j in start..end) {
                    if (j < itemCount) {
                        content(j)
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun LiveReportSummaryLayoutPreview() {
    NestTheme {
        Surface {
            LiveReportSummaryLayout(
                listData = listOf(
                    LiveStatsCardModel.NotClickable(LiveStatsUiModel.Viewer()),
                    LiveStatsCardModel.NotClickable(LiveStatsUiModel.TotalViewer()),
                    LiveStatsCardModel.NotClickable(LiveStatsUiModel.EstimatedIncome()),
                    LiveStatsCardModel.NotClickable(LiveStatsUiModel.Like()),
                    LiveStatsCardModel.NotClickable(LiveStatsUiModel.Duration()),
                )
            )
        }
    }
}

@Composable
@Preview
private fun LiveReportSummaryShimmerPreview() {
    NestTheme {
        Surface {
            LiveReportSummaryShimmer(
                itemCount = 4,
                gridCount = 2,
            )
        }
    }
}
