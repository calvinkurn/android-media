package com.tokopedia.product.detail.view.widget

import android.graphics.Bitmap
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifyprinciples.UnifyMotion
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.math.roundToInt

private const val IMAGE_SIZE = 218
private const val BORDER_SIZE = 2
private const val ROUNDED_SIZE = 24
private const val TARGET_IMAGE_RATIO = 0.05F

@Composable
fun AnimatedImageAnchor(
    resetLaunchEffect: Boolean,
    xTarget: Int,
    yTarget: Int,
    image: Bitmap,
    onFinishAnimated: () -> Unit = {}
) {
    val alphaAnimatable = remember(resetLaunchEffect) { Animatable(0F) }
    val yAnimatable = remember(resetLaunchEffect) { Animatable(0F) }
    val xAnimatable = remember(resetLaunchEffect) { Animatable(0F) }
    val scaleAnimatable = remember(resetLaunchEffect) { Animatable(0F) }

    LaunchedEffect(resetLaunchEffect) {
        animateShow(alphaAnimatable, scaleAnimatable)

        animateTranslation(
            alphaAnimatable = alphaAnimatable,
            scaleAnimatable = scaleAnimatable,
            xAnimatable = xAnimatable,
            yAnimatable = yAnimatable,
            xTarget = xTarget,
            yTarget = yTarget
        )

        onFinishAnimated.invoke()
    }

    Image(
        painter = rememberAsyncImagePainter(image),
        contentDescription = null,
        modifier = Modifier
            .size(IMAGE_SIZE.dp)
            .offset {
                IntOffset(xAnimatable.value.roundToInt(), yAnimatable.value.roundToInt())
            }
            .graphicsLayer {
                scaleX = scaleAnimatable.value
                scaleY = scaleAnimatable.value
                alpha = alphaAnimatable.value
            }
            .clip(shape = RoundedCornerShape(ROUNDED_SIZE.dp))
            .border(BORDER_SIZE.dp, Color.White, RoundedCornerShape(ROUNDED_SIZE.dp))
    )
}

private suspend fun animateShow(
    alphaAnimatable: Animatable<Float, AnimationVector1D>,
    scaleAnimatable: Animatable<Float, AnimationVector1D>
) {
    coroutineScope {
        val initialAlphaAnimation = async {
            alphaAnimatable.animateTo(
                1F,
                animationSpec = tween(UnifyMotion.T3.toInt(), easing = FastOutSlowInEasing)
            )
        }
        val initialScaleAnimation = async {
            scaleAnimatable.animateTo(
                1F,
                animationSpec = tween(UnifyMotion.T3.toInt(), easing = FastOutSlowInEasing)
            )
        }

        awaitAll(initialAlphaAnimation, initialScaleAnimation)
    }
}

private suspend fun animateTranslation(
    alphaAnimatable: Animatable<Float, AnimationVector1D>,
    scaleAnimatable: Animatable<Float, AnimationVector1D>,
    xAnimatable: Animatable<Float, AnimationVector1D>,
    yAnimatable: Animatable<Float, AnimationVector1D>,
    xTarget: Int,
    yTarget: Int
) {
    coroutineScope {
        val yTranslationAnimation = async {
            yAnimatable.animateTo(
                yTarget.toFloat(),
                animationSpec = tween(
                    UnifyMotion.T5.toInt(),
                    easing = FastOutSlowInEasing
                )
            )
        }

        val xTranslationAnimation = async {
            xAnimatable.animateTo(
                xTarget.toFloat(),
                animationSpec = tween(
                    UnifyMotion.T5.toInt(),
                    easing = FastOutSlowInEasing
                )
            )
        }

        val alphaAnimation = async {
            alphaAnimatable.animateTo(
                Float.ZERO,
                animationSpec = tween(
                    UnifyMotion.T5.toInt(),
                    easing = FastOutSlowInEasing
                )
            )
        }

        val scaleAnimation = async {
            scaleAnimatable.animateTo(
                TARGET_IMAGE_RATIO,
                animationSpec = tween(
                    UnifyMotion.T5.toInt(),
                    easing = FastOutSlowInEasing
                )
            )
        }

        awaitAll(yTranslationAnimation, xTranslationAnimation, scaleAnimation, alphaAnimation)
    }
}
