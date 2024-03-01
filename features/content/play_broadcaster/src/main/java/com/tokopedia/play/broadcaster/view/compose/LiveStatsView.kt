package com.tokopedia.play.broadcaster.view.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.noRippleClickable
import com.tokopedia.play.broadcaster.ui.model.stats.LiveStatsUiModel

/**
 * Created by Jonathan Darwin on 29 February 2024
 */
@Composable
fun LiveStatsView(
    liveStatsList: List<LiveStatsUiModel>,
    onClick: () -> Unit,
) {
    LazyRow(
        modifier = Modifier.noRippleClickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        itemsIndexed(liveStatsList) { idx, liveStats ->

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LiveStatsItem(liveStats)

                if (idx < liveStatsList.size - 1) {
                    LiveStatsSpacer()

                    Canvas(
                        modifier = Modifier.size(4.dp),
                        onDraw = {
                            drawCircle(color = Color.White)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LiveStatsItem(
    liveStats: LiveStatsUiModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NestIcon(
            modifier = Modifier.size(16.dp),
            iconId = liveStats.icon,
            colorLightEnable = Color.White,
            colorLightDisable = Color.White,
            colorNightDisable = Color.White,
            colorNightEnable = Color.White,
        )

        LiveStatsSpacer()

        NestTypography(
            text = liveStats.text,
            textStyle = NestTheme.typography.body3.copy(
                color = Color.White,
            )
        )
    }
}

@Composable
private fun LiveStatsSpacer() {
    Spacer(modifier = Modifier.width(6.dp))
}

@Composable
@Preview
private fun LiveStatsViewPreview() {
    NestTheme {
        LiveStatsView(
            liveStatsList = listOf(
                LiveStatsUiModel.Viewer("16,5rb"),
                LiveStatsUiModel.TotalViewer("16,5rb"),
                LiveStatsUiModel.EstimatedIncome("16,5rb"),
                LiveStatsUiModel.Like("16,5rb"),
                LiveStatsUiModel.Duration(1000),
            ),
            onClick = {},
        )
    }
}
