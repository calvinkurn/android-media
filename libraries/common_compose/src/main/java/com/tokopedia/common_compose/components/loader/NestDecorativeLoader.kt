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
    // duration
    val scaleDurationAnimate = 300
    val moveDurationAnimate = 300
    val iconScaleDurationAnimate = 300
    // delay
    val circleDelayAnimate = 300
    val iconDelayAnimate = 300
    // object size
    val circleCenterSize = 18
    val circleSideSize = 12
    // spacing
    val spaceBetweenCircle = 14
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
                /*scaleX = 0f,
                scaleY = 0f*/
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
    var sideScaleState by remember { mutableStateOf(1.5f) }
    var centerScaleState by remember { mutableStateOf(1.5f) }
    var iconScaleState by remember { mutableStateOf(2f) }
    var moveState by remember { mutableStateOf(0) }
    val animatedCenterScale by animateFloatBy(
        scale = centerScaleState,
        easing = FastOutSlowInEasing,
        delay = circleDelayAnimate
    )
    val animatedSideScale by animateFloatBy(
        scale = sideScaleState,
        easing = FastOutSlowInEasing,
        delay = circleDelayAnimate
    )
    val animatedMoveToRight by animateMoveTo(x = moveState.dp, delay = circleDelayAnimate)
    val animatedMoveToLeft by animateMoveTo(x = -moveState.dp, delay = circleDelayAnimate)
    val animatedIconScale by animateFloatAsState(
        targetValue = iconScaleState,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = iconScaleDurationAnimate,
                delayMillis = iconDelayAnimate,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(key1 = Unit, block = {
        centerScaleState = 1f
        sideScaleState = 1f
        iconScaleState = 1f
        moveState = spaceBetweenCircle * 2
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
                scale = animatedSideScale,
                dotSize = circleSideSize.dp
            )

            Dot(
                scale = animatedCenterScale,
                dotSize = circleCenterSize.dp
            )

            Dot(
                modifier = Modifier
                    .offset(x = animatedMoveToRight),
                scale = animatedSideScale,
                dotSize = circleSideSize.dp
            )

            TokopediaIcon(
                modifier = Modifier
                    .scale(animatedIconScale)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun NestDecorativeLoaderPreview() {
    NestDecorativeLoader(
        modifier = Modifier
            .size(100.dp)
    )
}
