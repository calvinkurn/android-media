package com.tokopedia.appdownloadmanager_common.presentation.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingProgressUiModel
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun AnimatedCircularProgressIndicator(
    downloadingProgressUiModel: DownloadingProgressUiModel,
    progressBackgroundColor: Color,
    progressIndicatorColor: Color,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressStatus(
            downloadingProgressUiModel = downloadingProgressUiModel,
            modifier = modifier
        )

        val animateFloat = remember { Animatable(0f) }

        LaunchedEffect(downloadingProgressUiModel.currentProgressInPercent) {
            animateFloat.animateTo(
                targetValue = downloadingProgressUiModel.currentProgressInPercent / MAX_VALUE.toFloat(),
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
        }

        Canvas(
            Modifier
                .progressSemantics(downloadingProgressUiModel.currentProgressInPercent / MAX_VALUE.toFloat())
                .size(CIRCULAR_INDICATOR_SIZE)
        ) {
            val startAngle = 270f
            val sweep: Float = animateFloat.value * 360f
            val diameterOffset = stroke.width / 2

            drawCircle(
                color = progressBackgroundColor,
                style = stroke,
                radius = size.minDimension / 2.0f - diameterOffset
            )
            drawCircularProgressIndicator(startAngle, sweep, progressIndicatorColor, stroke)
        }
    }
}

@Composable
fun CircularProgressStatus(
    downloadingProgressUiModel: DownloadingProgressUiModel,
    modifier: Modifier = Modifier
) {
    Column {
        NestTypography(
            modifier = modifier.wrapContentSize(Alignment.Center),
            text = "${downloadingProgressUiModel.currentProgressInPercent}%",
            textStyle = NestTheme.typography.heading1.copy(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.NN._950
            )
        )

        val currentSizeInProgress =
            "${downloadingProgressUiModel.currentDownloadedSize} dari ${downloadingProgressUiModel.totalResourceSize}"

        NestTypography(
            modifier = modifier.wrapContentSize(Alignment.Center).padding(top = 4.dp),
            text = currentSizeInProgress,
            textStyle = NestTheme.typography.small.copy(
                fontWeight = FontWeight.Normal,
                color = NestTheme.colors.NN._600
            )
        )
    }
}

private fun DrawScope.drawCircularProgressIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}

private val MAX_VALUE = 100
private val CIRCULAR_INDICATOR_SIZE = 160.dp
