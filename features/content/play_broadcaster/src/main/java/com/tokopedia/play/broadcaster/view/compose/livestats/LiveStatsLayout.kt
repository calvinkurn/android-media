package com.tokopedia.play.broadcaster.view.compose.livestats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.ui.model.stats.LiveStatsUiModel
import kotlin.math.ceil

/**
 * Created by Jonathan Darwin on 01 March 2024
 */
@Composable
fun LiveStatsLayout(
    liveStats: List<LiveStatsUiModel>,
    onEstimatedIncomeClicked: () -> Unit,
    modifier: Modifier = Modifier,
    gridCount: Int = 2,
) {
    if (gridCount <= 0) return

    Column(
        modifier = modifier,
    ) {
        val rowCount = ceil(liveStats.size / gridCount.toFloat()).toInt()

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
                    val currLiveStats = if (j < liveStats.size) {
                        liveStats[j.coerceAtMost(liveStats.size - 1)]
                    } else {
                        null
                    }

                    if (currLiveStats != null) {
                        LiveStatsCardView(
                            modifier = Modifier.weight(1f),
                            liveStats = currLiveStats,
                            type = if (currLiveStats is LiveStatsUiModel.EstimatedIncome) {
                                /** JOE TODO: make icon here dynamic */
                                LiveStatsBoxType.Clickable(
                                    icon = IconUnify.CHEVRON_RIGHT,
                                    onClick = onEstimatedIncomeClicked,
                                )
                            } else {
                                LiveStatsBoxType.NotClickable
                            }
                        )
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
