package com.tokopedia.product.detail.view.widget.campaign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by yovi.putra on 14/12/23"
 * Project name: android-tokopedia-core
 **/

@Composable
fun CampaignView(uiModel: CampaignUiModel, onTimerFinish: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = uiModel.backgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CampaignTitle(uiModel)

            CampaignStock(uiModel)
        }
        CampaignCountDown(uiModel, onTimerFinish)
    }
}

@Composable
private fun CampaignCountDown(
    uiModel: CampaignUiModel,
    onTimerFinish: () -> Unit
) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        NestTypography(
            text = uiModel.timerLabel,
            textStyle = NestTheme.typography.small.copy(
                color = Color.White
            ),
            overflow = TextOverflow.Ellipsis
        )
        CampaignTimer(
            modifier = Modifier.wrapContentSize(),
            endTimeMs = uiModel.endTimeMs,
            onFinish = onTimerFinish
        )
    }
}

@Composable
private fun CampaignStock(uiModel: CampaignUiModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        StockCampaignPercentage(
            modifier = Modifier
                .wrapContentHeight()
                .width(64.dp),
            value = uiModel.stockPercentage
        )
        NestTypography(
            text = uiModel.stockLabel,
            textStyle = NestTheme.typography.small.copy(
                color = Color.White
            ),
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CampaignTitle(uiModel: CampaignUiModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (uiModel.shouldShowLogo) {
            NestImage(
                modifier = Modifier
                    .height(18.dp)
                    .wrapContentWidth(),
                source = ImageSource.Remote(
                    source = uiModel.logoUrl,
                    loaderType = ImageSource.Remote.LoaderType.SHIMMER
                ),
                type = NestImageType.Rect(rounded = 0.dp)
            )
        } else {
            NestTypography(
                text = uiModel.title,
                textStyle = NestTheme.typography.body2.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun CampaignTimer(
    modifier: Modifier = Modifier,
    endTimeMs: Long,
    onFinish: () -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TimerUnifySingle(context).apply {
                timerVariant = TimerUnifySingle.VARIANT_INFORMATIVE_ALTERNATE
                timerFormat = TimerUnifySingle.FORMAT_AUTO
                this.onFinish = onFinish
                this.isShowClockIcon = false

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = endTimeMs
                this.targetDate = calendar
            }
        }
    )
}

@Composable
private fun StockCampaignPercentage(
    modifier: Modifier = Modifier,
    value: Int = 0,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ProgressBarUnify(context).apply {
                val color = ContextCompat.getColor(
                    context, unifyprinciplesR.color.Unify_Static_White
                )
                progressBarColor = intArrayOf(color, color)
                progressBarHeight = ProgressBarUnify.SIZE_MEDIUM
                val trackColor = ContextCompat.getColor(
                    context, unifyprinciplesR.color.Unify_Overlay_Lvl2
                )
                trackDrawable.setColor(trackColor)
            }
        },
        update = { it.setValue(value, true) }
    )
}

@Preview
@Composable
fun CampaignViewPreview() {
    NestTheme {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            CampaignView(CampaignUiModel(stockPercentage = 30)) {}
            CampaignView(CampaignUiModel(stockPercentage = 75)) {}
            CampaignView(CampaignUiModel(logoUrl = "")) {}
        }
    }
}
