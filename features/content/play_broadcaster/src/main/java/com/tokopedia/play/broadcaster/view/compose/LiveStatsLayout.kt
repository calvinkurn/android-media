package com.tokopedia.play.broadcaster.view.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.ui.model.stats.LiveStatsUiModel

/**
 * Created by Jonathan Darwin on 01 March 2024
 */
@Composable
fun LiveStatsLayout(
    liveStats: List<LiveStatsUiModel>,
    onEstimatedIncomeClicked: () -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(liveStats.size) { idx ->
            val currLiveStats = liveStats[idx]
            LiveStatsBoxView(
                liveStats = currLiveStats,
                onClick = if (currLiveStats is LiveStatsUiModel.EstimatedIncome) {
                    onEstimatedIncomeClicked
                } else {
                    null
                }
            )
        }
    }
}

@Composable
@Preview
private fun LiveStatsLayoutPreview() {
    NestTheme {
        Surface {
            LiveStatsLayout(
                liveStats = listOf(
                    LiveStatsUiModel.Viewer(),
                    LiveStatsUiModel.TotalViewer(),
                    LiveStatsUiModel.EstimatedIncome(),
                    LiveStatsUiModel.Like(),
                    LiveStatsUiModel.Duration(),
                ),
                onEstimatedIncomeClicked = {},
            )
        }
    }
}
