package com.tokopedia.common_compose.components.loader

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.common_compose.ui.NestGN
import com.tokopedia.common_compose.ui.NestNN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by yovi.putra on 13/05/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun NestCircularLoader(
    modifier: Modifier = Modifier,
    isWhite: Boolean,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    var state by remember { mutableStateOf(true) }
    var isRunning by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val delayAnimation = 1_333L
    val vectorPainter = rememberVectorPainter(
        defaultWidth = 500f.dp,
        defaultHeight = 500f.dp,
        viewportWidth = 500f,
        viewportHeight = 500f,
        autoMirror = false
    ) { _, _ ->
        Group(name = "_R_G") {
            // circle right
            CircleRightGroup(scope = scope, state = state, isWhite = isWhite)

            // circle left
            CircleLeftGroup(scope = scope, state = state, isWhite = isWhite)
        }
    }

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
        modifier = modifier,
        painter = vectorPainter,
        contentDescription = "nest_circular_loader"
    )
}

@Composable
private fun CircleLeftGroup(scope: CoroutineScope, state: Boolean, isWhite: Boolean) {
    Group(
        name = "_R_G_L_0_G_N_1_T_0",
        translationX = 250f,
        translationY = 250f
    ) {
        Group(
            name = "_R_G_L_0_G",
            rotation = -90.806f
        ) {
            val initialTrimStart = 0f
            val initialTrimEnd = .001f
            val animatedTrimStart = remember { Animatable(initialValue = initialTrimStart) }
            val animatedTrimEnd = remember { Animatable(initialValue = initialTrimEnd) }
            val color = if (isWhite) {
                NestNN.light._0
            } else {
                NestGN.light._500
            }

            LaunchedEffect(key1 = state, block = {
                launch {
                    // reset animation to initial value
                    animatedTrimStart.animateTo(target = initialTrimStart, duration = 0) {
                        scope.launch {
                            // start animation
                            animatedTrimStart.animateTo(target = .999f, duration = 790, delay = 526)
                        }
                    }
                }
                launch {
                    // reset animation to initial value
                    animatedTrimEnd.animateTo(target = initialTrimEnd, duration = 0) {
                        scope.launch {
                            // start animation
                            animatedTrimEnd.animateTo(target = 1f, duration = 970, delay = 167)
                        }
                    }
                }
            })

            Circle(
                name = "_R_G_L_0_G_D_0_P_0",
                color = color,
                trimPathStart = animatedTrimStart.value,
                trimPathEnd = animatedTrimEnd.value
            )
        }
    }
}

@Composable
private fun CircleRightGroup(scope: CoroutineScope, state: Boolean, isWhite: Boolean) {
    Group(
        name = "_R_G_L_1_G_N_1_T_0",
        translationX = 250f,
        translationY = 250f
    ) {
        Group(name = "_R_G_L_1_G", rotation = 90f) {
            val initialTrimStart = .999f
            val initialTrimEnd = 1f
            val animatedTrimStart = remember { Animatable(initialValue = initialTrimStart) }
            val animatedTrimEnd = remember { Animatable(initialValue = initialTrimEnd) }
            val color = if (isWhite) {
                NestNN.light._300
            } else {
                NestGN.light._300
            }

            LaunchedEffect(key1 = state, block = {
                launch {
                    // reset animation to initial value
                    animatedTrimStart.animateTo(target = initialTrimStart, duration = 0) {
                        scope.launch {
                            // start animation
                            animatedTrimStart.animateTo(target = 0f, duration = 750)
                        }
                    }
                }
                launch {
                    // reset animation to initial value
                    animatedTrimEnd.animateTo(target = initialTrimEnd) {
                        scope.launch {
                            // start animation
                            animatedTrimEnd.animateTo(target = .001f, duration = 654, delay = 433)
                        }
                    }
                }
            })

            Circle(
                name = "_R_G_L_1_G_D_0_P_0",
                color = color,
                trimPathStart = animatedTrimStart.value,
                trimPathEnd = animatedTrimEnd.value
            )
        }
    }
}

private suspend fun Animatable<Float, AnimationVector1D>.animateTo(
    target: Float,
    duration: Int = 0,
    delay: Int = 0,
    finish: () -> Unit = {}
) = this.animateTo(
    targetValue = target,
    animationSpec = tween(
        durationMillis = duration,
        easing = FastOutSlowInEasing,
        delayMillis = delay
    ),
    block = {
        finish.invoke()
    }
)

@Composable
private fun Circle(name: String, color: Color, trimPathStart: Float, trimPathEnd: Float) = Path(
    name = name,
    pathData = CirclePath,
    stroke = SolidColor(color),
    strokeLineWidth = 60f,
    strokeLineCap = StrokeCap.Round,
    strokeLineJoin = StrokeJoin.Round,
    trimPathStart = trimPathStart,
    trimPathEnd = trimPathEnd
)

private val CirclePath by lazy {
    PathParser().parsePathString(
        " M0 -180.08 C99.39,-180.08 180.08,-99.39 180.08,0 C180.08,99.39 99.39,180.08 0,180.08 C-99.39,180.08 -180.08,99.39 -180.08,0 C-180.08,-99.39 -99.39,-180.08 0,-180.08c "
    ).toNodes()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NestCircularLoaderPreview() {
    NestCircularLoader(
        modifier = Modifier.size(50.dp),
        isWhite = false
    )
}
