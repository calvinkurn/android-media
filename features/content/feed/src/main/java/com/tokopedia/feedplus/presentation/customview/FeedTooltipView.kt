package com.tokopedia.feedplus.presentation.customview

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
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
import com.tokopedia.feedplus.presentation.tooltip.FeedSearchTooltipCategory
import com.tokopedia.kotlin.extensions.view.getLocationOnScreen
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.noRippleClickable
import com.tokopedia.unifyprinciples.UnifyMotion
import com.tokopedia.feedplus.R as feedplusR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class FeedTooltipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private var xLocation by mutableStateOf(0)

    private var text by mutableStateOf("")

    private var onClick by mutableStateOf({})

    private val xOffset = derivedStateOf {
        val locationOnScreen = getLocationOnScreen()
        xLocation - locationOnScreen.x
    }

    fun setAnchorXLocation(xLocation: Int) {
        this.xLocation = xLocation
    }

    fun setTooltipMessage(category: FeedSearchTooltipCategory) {
        val textRes = when (category) {
            FeedSearchTooltipCategory.UserAffinity -> feedplusR.string.feed_search_tooltip_user_affinity
            FeedSearchTooltipCategory.Creator -> feedplusR.string.feed_search_tooltip_creator
            FeedSearchTooltipCategory.Story -> feedplusR.string.feed_search_tooltip_story
            FeedSearchTooltipCategory.Trending -> feedplusR.string.feed_search_tooltip_trending_and_viral_video
            FeedSearchTooltipCategory.Promo -> feedplusR.string.feed_search_tooltip_promo
            else -> return
        }

        this.text = context.getString(textRes)
    }

    fun setOnClickTooltip(onClick: () -> Unit) {
        this.onClick = onClick
    }

    override fun setVisibility(visibility: Int) {
        val superSetVisibility = {
            super.setVisibility(visibility)
        }

        if (visibility == View.VISIBLE) {
            scaleX = 0f
            scaleY = 0f
            alpha = 0f

            animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f)
                .setInterpolator(UnifyMotion.EASE_OVERSHOOT)
                .setDuration(UnifyMotion.T3)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {
                        superSetVisibility()
                    }

                    override fun onAnimationEnd(p0: Animator) {}

                    override fun onAnimationCancel(p0: Animator) {}

                    override fun onAnimationRepeat(p0: Animator) {}
                })
                .start()
        } else {
            animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f)
                .setInterpolator(UnifyMotion.EASE_OUT)
                .setDuration(UnifyMotion.T3)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {}

                    override fun onAnimationEnd(p0: Animator) {
                        superSetVisibility()
                    }

                    override fun onAnimationCancel(p0: Animator) {}

                    override fun onAnimationRepeat(p0: Animator) {}
                })
                .start()
        }
    }

    @Composable
    override fun Content() {
        FeedTooltip(
            text = text,
            offsetX = xOffset.value.toFloat(),
            onClick = onClick,
        )
    }
}

@Composable
private fun FeedTooltip(
    text: String,
    offsetX: Float,
    onClick: () -> Unit,
) {
    val anchorColor = colorResource(id = feedplusR.color.feed_dms_tooltip_background)

    Box(
        modifier = Modifier
            .noRippleClickable(onClick)
            .drawWithCache {
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
    FeedTooltip(
        text = "Perlengkapan Medis lagi rame!",
        offsetX = 20f,
        onClick = {},
    )
}
