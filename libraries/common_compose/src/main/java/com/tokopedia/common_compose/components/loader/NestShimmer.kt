package com.tokopedia.common_compose.components.loader

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.common_compose.ui.NestTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by yovi.putra on 15/05/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun NestShimmer(
    modifier: Modifier = Modifier,
    type: NestShimmerType,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    when (type) {
        is NestShimmerType.Rect -> {
            NestShimmerLayout(modifier = modifier, rounded = type.rounded, lifecycleOwner = lifecycleOwner)
        }

        else -> {
            val height = remember { mutableStateOf(0) }
            NestShimmerLayout(
                modifier = modifier
                    .onGloballyPositioned {
                        val heightSize = it.size.height
                        if (height.value != heightSize) {
                            height.value = heightSize
                        }
                    },
                rounded = (height.value / 2).dp,
                lifecycleOwner = lifecycleOwner
            )
        }
    }
}

@Composable
private fun NestShimmerLayout(
    modifier: Modifier,
    rounded: Dp,
    lifecycleOwner: LifecycleOwner
) {
    var state by remember { mutableStateOf(true) }
    var isRunning by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val delayAnimation = 1200L

    val vectorPainter = rememberVectorPainter(
        defaultWidth = 24f.dp,
        defaultHeight = 24f.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = false
    ) { _, _ -> PathGroup(state = state) }

    suspend fun runLoopingAnimation() {
        while (isRunning) {
            state = !state
            delay(delayAnimation)
        }
    }

    DisposableEffect(key1 = Unit, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    isRunning = true
                    scope.launch {
                        runLoopingAnimation()
                    }
                }

                Lifecycle.Event.ON_PAUSE -> {
                    isRunning = false
                }

                else -> {
                    // no ops
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    Image(
        modifier = modifier
            .clip(RoundedCornerShape(rounded))
            .background(Color.LightGray),
        painter = vectorPainter,
        contentDescription = "nest_shimmer",
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun PathGroup(state: Boolean) {
    val trimPathStart = remember { Animatable(initialValue = 0f) }
    val trimPathEnd = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(key1 = state, block = {
        launch {
            trimPathEnd.animateTo(target = 0f)
            trimPathEnd.animateTo(target = 1f, duration = 899)
        }

        launch {
            trimPathStart.animateTo(target = 0f)
            trimPathStart.animateTo(target = 1f, duration = 301, delay = 899)
        }
    })

    Path(
        name = "path",
        pathData = FirstVector,
        fill = SolidColor(NestTheme.colors.NN._100)
    )

    Path(
        name = "path_1",
        pathData = SecondVector,
        stroke = SolidColor(NestTheme.colors.NN._200),
        strokeLineWidth = 30f,
        trimPathStart = trimPathStart.value,
        trimPathEnd = trimPathEnd.value
    )
}

private val FirstVector by lazy {
    PathParser().parsePathString(
        "M -0.776 24.207 L -0.776 -0.052 L 26.586 -0.052 L 26.586 24.207 Z"
    ).toNodes()
}

private val SecondVector by lazy {
    PathParser().parsePathString(
        "M 0 11.897 L 24 11.897"
    ).toNodes()
}

@Preview
@Composable
private fun NestShimmerPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NestShimmer(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            type = NestShimmerType.Rect()
        )

        NestShimmer(
            modifier = Modifier
                .size(100.dp, 50.dp),
            type = NestShimmerType.Rect(rounded = 8.dp)
        )

        NestShimmer(
            modifier = Modifier
                .size(100.dp, 50.dp),
            type = NestShimmerType.Rect(rounded = 16.dp)
        )

        NestShimmer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp),
            type = NestShimmerType.Line
        )

        NestShimmer(
            modifier = Modifier.size(100.dp, 16.dp),
            type = NestShimmerType.Line
        )

        NestShimmer(
            modifier = Modifier.size(100.dp),
            type = NestShimmerType.Circle
        )
    }
}
