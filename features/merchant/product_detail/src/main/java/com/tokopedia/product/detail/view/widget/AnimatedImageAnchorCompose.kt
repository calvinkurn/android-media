package com.tokopedia.product.detail.view.widget

import android.graphics.Bitmap
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.unifyprinciples.UnifyMotion
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private const val IMAGE_SIZE = 218
private const val BORDER_SIZE = 2
private const val ROUNDED_SIZE = 24
private const val TARGET_IMAGE_RATIO = 0.1F

private val customBezier = CubicBezierEasing(0.63F, 0.01F, 0.29F, 1F)

@Stable
data class AnimationData(
    val resetLaunchEffect: Boolean,
    val xTarget: Int,
    val yTarget: Int,
    val image: Bitmap,
    val onFinishAnimated: () -> Unit = {}
)

@Composable
fun AnimatedImageAnchor(
    data: AnimationData
) {
    val alphaAnimatable = remember(data.resetLaunchEffect) { Animatable(0F) }
    val yAnimatable = remember(data.resetLaunchEffect) { Animatable(0F) }
    val xAnimatable = remember(data.resetLaunchEffect) { Animatable(0F) }
    val scaleAnimatable = remember(data.resetLaunchEffect) { Animatable(0F) }
    val radiusAnimatable = remember(data.resetLaunchEffect) { Animatable(ROUNDED_SIZE.toFloat()) }

    val relativeTargetPosition = remember {
        mutableStateOf(IntOffset(0, 0))
    }

    val shouldFindCoordinate by remember {
        derivedStateOf { relativeTargetPosition.value.x == 0 && relativeTargetPosition.value.y == 0 }
    }

    LaunchedEffect(data.resetLaunchEffect, relativeTargetPosition) {
        animateShow(alphaAnimatable, scaleAnimatable)

        delay(ProductDetailCommonConstant.START_DELAY_SECOND_ANIMATION)

        animateTranslation(
            alphaAnimatable = alphaAnimatable,
            scaleAnimatable = scaleAnimatable,
            xAnimatable = xAnimatable,
            yAnimatable = yAnimatable,
            radiusAnimatable = radiusAnimatable,
            xTarget = relativeTargetPosition.value.x,
            yTarget = -relativeTargetPosition.value.y
        )

        data.onFinishAnimated.invoke()
    }

    NestTheme {
        Card(
            border = BorderStroke(BORDER_SIZE.dp, NestTheme.colors.NN._50),
            shape = RoundedCornerShape(radiusAnimatable.value.dp),
            modifier = Modifier
                .size(IMAGE_SIZE.dp)
                .findCenterCoordinateAfterInflate(
                    shouldFindCoordinate,
                    scaleAnimatable.value == 1F
                ) { x, y ->
                    relativeTargetPosition.value =
                        IntOffset(
                            data.xTarget - x,
                            y - data.yTarget
                        )
                }
                .offset {
                    IntOffset(xAnimatable.value.roundToInt(), yAnimatable.value.roundToInt())
                }
                .graphicsLayer {
                    scaleX = scaleAnimatable.value
                    scaleY = scaleAnimatable.value
                    alpha = alphaAnimatable.value
                    shadowElevation = 10.dp.toPx()
                    shape = RoundedCornerShape(radiusAnimatable.value.dp)
                    clip = true
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                drawImage(
                    data.image.asImageBitmap(),
                    dstSize = IntSize(size.width.toInt(), size.height.toInt())
                )
            })
        }
    }
}

private fun Modifier.findCenterCoordinateAfterInflate(
    shouldFindCoordinate: Boolean,
    shouldTrigger: Boolean,
    finish: (Int, Int) -> Unit
): Modifier = composed {
    conditional(shouldFindCoordinate) {
        onPlaced {
            if (shouldTrigger) {
                val positionInRoot = it.positionInWindow()
                val xCenterCoordinate = (positionInRoot.x + it.size.width.half())
                val yCenterCoordinate = (positionInRoot.y + it.size.height.half())

                finish.invoke(
                    xCenterCoordinate.roundToInt(),
                    yCenterCoordinate.roundToInt()
                )
            }
        }
    }
}

private fun Int.half() = (this / 2)

private fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

private suspend fun animateShow(
    alphaAnimatable: Animatable<Float, AnimationVector1D>,
    scaleAnimatable: Animatable<Float, AnimationVector1D>
) {
    coroutineScope {
        val initialAlphaAnimation = async {
            alphaAnimatable.animateTo(
                1F,
                animationSpec = tween(UnifyMotion.T5.toInt(), easing = customBezier)
            )
        }
        val initialScaleAnimation = async {
            scaleAnimatable.animateTo(
                1F,
                animationSpec = tween(UnifyMotion.T5.toInt(), easing = customBezier)
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
    radiusAnimatable: Animatable<Float, AnimationVector1D>,
    xTarget: Int,
    yTarget: Int
) {
    coroutineScope {
        val yTranslationAnimation = async {
            yAnimatable.animateTo(
                yTarget.toFloat(),
                animationSpec = tween(
                    UnifyMotion.T5.toInt(),
                    easing = customBezier
                )
            )
        }

        val xTranslationAnimation = async {
            xAnimatable.animateTo(
                xTarget.toFloat(),
                animationSpec = tween(
                    UnifyMotion.T5.toInt(),
                    easing = customBezier
                )
            )
        }

        val alphaAnimation = async {
            alphaAnimatable.animateTo(
                Float.ZERO,
                animationSpec = tween(
                    UnifyMotion.T5.toInt(),
                    easing = customBezier
                )
            )
        }

        val scaleAnimation = async {
            scaleAnimatable.animateTo(
                TARGET_IMAGE_RATIO,
                animationSpec = tween(
                    UnifyMotion.T5.toInt(),
                    easing = customBezier
                )
            )
        }

        val radiusAnimation = async {
            radiusAnimatable.animateTo(
                radiusAnimatable.value * 8,
                animationSpec = tween(
                    UnifyMotion.T5.toInt(),
                    easing = customBezier
                )
            )
        }

        awaitAll(
            yTranslationAnimation,
            xTranslationAnimation,
            scaleAnimation,
            alphaAnimation,
            radiusAnimation
        )
    }
}
