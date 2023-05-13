package com.tokopedia.common_compose.components.loader

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestNN
import com.tokopedia.common_compose.ui.NestTheme

/**
 * Created by yovi.putra on 12/05/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun NestDecorativeLoader(
    modifier: Modifier = Modifier,
    isWhite: Boolean = false
) {

    // color
    val color = if (isWhite) NestNN.light._0 else NestTheme.colors.GN._500

    @Composable
    fun Dot(
        modifier: Modifier = Modifier,
        dotSize: Dp = circleSideSize.dp,
        scale: Float
    ) = Spacer(
        modifier
            .size(dotSize)
            .scale(scale)
            .background(
                color = color,
                shape = CircleShape
            )
    )

    @Composable
    fun TokopediaIcon(
        modifier: Modifier = Modifier
    ) {
        val path = PathParser().parsePathString(
            "M 22.589 10 L 24.205 10 C 25.408 10 26.002 10.597 26.002 11.797 L 26.002 19.916 C 25.975 21.536 25.311 23.084 24.156 24.224 C 23.001 25.36 21.443 26 19.821 26 L 11.77 26 C 10.581 26 10 25.419 10 24.23 L 10 11.792 C 10 10.592 10.594 10 11.792 10 L 13.411 10 C 15.98 10 17.298 11.361 17.999 12.5 C 18.7 11.361 20.022 10 22.587 10 L 22.589 10 Z"
        ).toNodes()
        val vectorPainter = rememberVectorPainter(
            defaultWidth = 36f.dp,
            defaultHeight = 36f.dp,
            viewportWidth = 36f,
            viewportHeight = 36f,
            autoMirror = true
        ) { _, _ ->
            Group(
                name = "tokopedia",
                pivotX = 18f,
                pivotY = 18f
            ) {
                Path(
                    pathData = path,
                    fill = SolidColor(color),
                    pathFillType = PathFillType.EvenOdd
                )
            }
        }

        Image(
            modifier = modifier,
            painter = vectorPainter,
            contentDescription = "tokopedia"
        )
    }

    @Composable
    fun animateFloatBy(
        scale: Float,
        duration: Int = scaleDurationAnimate,
        delay: Int = 0,
        easing: Easing = FastOutSlowInEasing
    ) = animateFloatAsState(
        targetValue = scale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                delayMillis = delay,
                easing = easing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    @Composable
    fun animateMoveTo(x: Dp, delay: Int) = animateDpAsState(
        targetValue = x,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = moveDurationAnimate,
                delayMillis = delay,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    var containerWidth by remember { mutableStateOf(0) }
    var dotSideScaleState by remember { mutableStateOf(2f) }
    var dotCenterScaleState by remember { mutableStateOf(1.33f) }
    var iconScaleState by remember { mutableStateOf(2f) }
    var dotMovingState by remember { mutableStateOf(0) }
    val animatedDotCenterScale by animateFloatBy(
        scale = dotCenterScaleState,
        easing = FastOutSlowInEasing,
        delay = circleDelayAnimate
    )
    val animatedDotSideScale by animateFloatBy(
        scale = dotSideScaleState,
        easing = FastOutSlowInEasing,
        delay = circleDelayAnimate
    )
    val animatedMoveToRight by animateMoveTo(x = dotMovingState.dp, delay = circleDelayAnimate)
    val animatedMoveToLeft by animateMoveTo(x = -dotMovingState.dp, delay = circleDelayAnimate)
    val animatedIconScale by animateFloatBy(
        scale = iconScaleState,
        easing = FastOutSlowInEasing,
        delay = iconDelayAnimate,
        duration = iconScaleDurationAnimate
    )

    LaunchedEffect(key1 = Unit, block = {
        dotCenterScaleState = 1f
        dotSideScaleState = 1f
        iconScaleState = 0f
        dotMovingState = spaceBetweenCircle * 2
    })

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .onGloballyPositioned {
                    if (containerWidth != it.size.width) {
                        containerWidth = it.size.width
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Dot(
                modifier = Modifier
                    .offset(x = animatedMoveToLeft),
                scale = animatedDotSideScale,
                dotSize = circleSideSize.dp
            )

            Dot(
                scale = animatedDotCenterScale,
                dotSize = circleCenterSize.dp
            )

            Dot(
                modifier = Modifier
                    .offset(x = animatedMoveToRight),
                scale = animatedDotSideScale,
                dotSize = circleSideSize.dp
            )

            TokopediaIcon(
                modifier = Modifier
                    .scale(animatedIconScale)
            )
        }
    }
}

// duration
private const val scaleDurationAnimate = 300
private const val moveDurationAnimate = 300
private const val iconScaleDurationAnimate = 300
// delay
private const val circleDelayAnimate = 300
private const val iconDelayAnimate = 300
// object size
private const val circleCenterSize = 18
private const val circleSideSize = 12
// spacing
private const val spaceBetweenCircle = 14

@Preview(showSystemUi = true)
@Composable
fun NestDecorativeLoaderPreview() {
    NestDecorativeLoader(
        modifier = Modifier
            .size(100.dp)
    )
}
