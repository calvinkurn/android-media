package com.tokopedia.feedplus.presentation.customview

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.kotlin.extensions.view.getLocationOnScreen
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.feedplus.R as feedplusR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class FeedTooltipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private var xLocation by mutableStateOf(0)

    private val xOffset = derivedStateOf {
        val locationOnScreen = getLocationOnScreen()
        xLocation - locationOnScreen.x
    }

    fun setAnchorXLocation(xLocation: Int) {
        this.xLocation = xLocation
    }

    @Composable
    override fun Content() {
        FeedTooltip(text = "Perlengkapan Medis lagi rame!", offsetX = xOffset.value.toFloat())
    }
}

@Composable
private fun FeedTooltip(
    text: String,
    offsetX: Float,
) {
    val anchorColor = colorResource(id = feedplusR.color.feed_dms_tooltip_background)

    Box(
        Modifier.drawWithCache {
            onDrawBehind {
                translate(
                    left = offsetX - 6.dp.toPx(),
                    top = 2.dp.toPx()
                ) {
                    rotate(45f, pivot = Offset(6.dp.toPx(), 6.dp.toPx())) {
                        drawRoundRect(
                            color = anchorColor,
                            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                            size = Size(width = 12.dp.toPx(), height = 12.dp.toPx()),
                        )
                    }
                }
            }
        }
    ) {
        TextContainer(
            text = text,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
private fun TextContainer(
    text: String,
    modifier: Modifier,
) {
    Box(
        modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = colorResource(id = feedplusR.color.feed_dms_tooltip_background)
            )
    ) {
        NestTypography(
            text = text,
            textStyle = NestTheme.typography.paragraph3.copy(
                color = colorResource(id = unifyprinciplesR.color.Unify_Static_White)
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
@Preview
private fun FeedTooltipPreview() {
    FeedTooltip(text = "Perlengkapan Medis lagi rame!", 20f)
}
