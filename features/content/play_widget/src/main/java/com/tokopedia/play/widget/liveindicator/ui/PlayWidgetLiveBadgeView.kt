package com.tokopedia.play.widget.liveindicator.ui

import android.content.Context
import android.util.AttributeSet
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.NoMinimumTouchArea
import com.tokopedia.play.widget.liveindicator.analytic.PlayWidgetLiveIndicatorAnalytic
import com.tokopedia.play.widget.liveindicator.di.rememberDaggerComponent
import com.tokopedia.play.widget.R as playwidgetR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PlayWidgetLiveBadgeView : AbstractComposeView {

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs,
        0
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mAnalyticModel: PlayWidgetLiveIndicatorAnalytic.Model? by mutableStateOf(null)
    private var mImpressionTag by mutableStateOf("")

    private var mOnClicked by mutableStateOf({})

    @Composable
    override fun Content() {
        NestTheme {
            PlayWidgetLiveBadge(mOnClicked, analyticModel = mAnalyticModel, impressionTag = mImpressionTag)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mOnClicked = { l?.onClick(this) }
    }

    fun setAnalyticModel(model: PlayWidgetLiveIndicatorAnalytic.Model) {
        mAnalyticModel = model
    }

    fun setImpressionTag(tag: String) {
        mImpressionTag = tag
    }
}

@Composable
fun PlayWidgetLiveBadge(
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
    analyticModel: PlayWidgetLiveIndicatorAnalytic.Model? = null,
    impressionTag: String = "",
) {

    val component = rememberDaggerComponent()

    LaunchedEffect(analyticModel, impressionTag) {
        analyticModel?.let {
            component.getAnalytic().impressLiveBadge(it, impressionTag)
        }
    }

    NoMinimumTouchArea {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .requiredHeight(24.dp)
                .requiredWidth(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = {
                    analyticModel?.let {
                        component.getAnalytic().clickLiveBadge(it)
                    }
                    onClicked()
                })
                .background(colorResource(playwidgetR.color.play_widget_live_indicator_dms_bg))
        ) {
            LiveIndicatorDot(
                Modifier.padding(start = 8.dp)
            )
            LiveText(
                Modifier.padding(start = 4.dp, end = 8.dp)
            )
            IconChevronRight(
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
private fun LiveIndicatorDot(modifier: Modifier = Modifier) {

    val circleColor = colorResource(id = unifyprinciplesR.color.Unify_Static_White)

    val infiniteTransition = rememberInfiniteTransition()

    val blinkOut by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = 600
                delayMillis = 500
                1.0f at 0 with BlinkEaseInOut
                0f at 300 with BlinkEaseInOut
                1.0f at 600
            },
            repeatMode = RepeatMode.Restart,
        )
    )

    Box(
        modifier
            .drawWithCache {
                onDrawBehind {
                    drawCircle(
                        color = circleColor,
                        radius = 3.dp.toPx(),
                        alpha = blinkOut
                    )
                }
            }
            .requiredSize(6.dp)
    ) {}
}

@Composable
private fun LiveText(modifier: Modifier = Modifier) {
    NestTypography(
        text = "LIVE",
        textStyle = NestTheme.typography.small.copy(
            color = colorResource(id = unifyprinciplesR.color.Unify_Static_White),
            fontWeight = FontWeight.Bold
        ),
        modifier = modifier
    )
}

@Composable
private fun IconChevronRight(modifier: Modifier = Modifier) {
    val iconColor = colorResource(id = unifyprinciplesR.color.Unify_Static_White)

    NestIcon(
        iconId = IconUnify.CHEVRON_RIGHT,
        colorLightEnable = iconColor,
        colorLightDisable = iconColor,
        colorNightEnable = iconColor,
        colorNightDisable = iconColor,
        modifier = modifier.requiredSize(16.dp),
    )
}

private val BlinkEaseInOut = CubicBezierEasing(0.63f, 0.01f, 0.29f, 1.0f)

@Preview
@Composable
private fun PlayWidgetLiveIndicatorViewPreview() {
    PlayWidgetLiveBadge(onClicked = {}, )
}
