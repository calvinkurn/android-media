package com.tokopedia.common_compose.components.loader

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.common_compose.ui.NestNN
import com.tokopedia.common_compose.ui.NestTheme
import kotlinx.coroutines.launch

/**
 * Created by yovi.putra on 14/05/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun NestDecorativeLoader(
    modifier: Modifier = Modifier,
    isWhite: Boolean = false,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    var state by remember { mutableStateOf(true) }
    val color = if (isWhite) NestNN.light._0 else NestTheme.colors.GN._500

    NestInfiniteLoader(
        modifier = modifier,
        name = "nest_decorative_loader",
        totalDuration = 1_333L,
        vectorWidth = 36f,
        vectorHeight = 36f,
        lifecycleOwner = lifecycleOwner,
        tintColor = color,
        updateState = {
            state = !state
        },
        content = { _, _ ->
            Group(name = "_R_G") {
                CircleLeftGroup(state = state)
                CircleCenterGroup(state = state)
                CircleRightGroup(state = state)
                TokopediaGroup(state = state)
            }
        }
    )
}

@Composable
private fun CircleLeftGroup(state: Boolean) {
    val animatedScaleX = remember { Animatable(initialValue = 1f) }
    val animatedScaleY = remember { Animatable(initialValue = 1f) }
    val animatedTranslateX = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(key1 = state, block = {
        launch {
            animatedScaleX.animateTo(target = 2f, duration = 300)
            animatedScaleX.animateTo(target = 1f, duration = 1000, delay = 300)
        }
        launch {
            animatedScaleY.animateTo(target = 2f, duration = 300)
            animatedScaleY.animateTo(target = 1f, duration = 1000, delay = 300)
        }

        launch {
            animatedTranslateX.animateTo(target = 13f, duration = 300)
            animatedTranslateX.animateTo(target = 0f, duration = 300, delay = 600)
        }
    })

    Group(
        name = "path_1_group",
        pivotX = 6f,
        pivotY = 18f,
        scaleX = animatedScaleX.value,
        scaleY = animatedScaleY.value,
        translationX = animatedTranslateX.value
    ) {
        Circle(name = "path_1", path = CircleLeftPath)
    }
}

@Composable
private fun CircleCenterGroup(state: Boolean) {
    val animatedScaleX = remember { Animatable(initialValue = 1f) }
    val animatedScaleY = remember { Animatable(initialValue = 1.25f) }

    LaunchedEffect(key1 = state, block = {
        launch {
            animatedScaleX.animateTo(target = 1.25f, duration = 300)
            animatedScaleX.animateTo(target = 1f, duration = 1000, delay = 300)
        }
        launch {
            animatedScaleY.animateTo(target = 1.25f, duration = 300)
            animatedScaleY.animateTo(target = 1f, duration = 1000, delay = 300)
        }
    })

    Group(
        name = "path_2_group",
        pivotX = 18f,
        pivotY = 18f,
        scaleX = animatedScaleX.value,
        scaleY = animatedScaleY.value
    ) {
        Circle(name = "path_2", path = CircleCenterPath)
    }
}

@Composable
private fun CircleRightGroup(state: Boolean) {
    val animatedScaleX = remember { Animatable(initialValue = 1f) }
    val animatedScaleY = remember { Animatable(initialValue = 1f) }
    val animatedTranslateX = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(key1 = state, block = {
        launch {
            animatedScaleX.animateTo(target = 2f, duration = 300)
            animatedScaleX.animateTo(target = 1f, duration = 1000, delay = 300)
        }
        launch {
            animatedScaleY.animateTo(target = 2f, duration = 300)
            animatedScaleY.animateTo(target = 1f, duration = 1000, delay = 300)
        }
        launch {
            animatedTranslateX.animateTo(target = -13f, duration = 300)
            animatedTranslateX.animateTo(target = 0f, duration = 300, delay = 600)
        }
    })

    Group(
        name = "path_3_group",
        pivotX = 30f,
        pivotY = 18f,
        scaleX = animatedScaleX.value,
        scaleY = animatedScaleY.value,
        translationX = animatedTranslateX.value
    ) {
        Circle(name = "path_3", path = CircleRightPath)
    }
}

@Composable
private fun TokopediaGroup(state: Boolean) {
    val animatedScaleX = remember { Animatable(initialValue = 0f) }
    val animatedScaleY = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(key1 = state, block = {
        launch {
            animatedScaleX.animateTo(target = 1f, duration = 300)
            animatedScaleX.animateTo(target = 0f, duration = 1000)
        }
        launch {
            animatedScaleY.animateTo(target = 1f, duration = 300)
            animatedScaleY.animateTo(target = 0f, duration = 1000)
        }
    })
    Group(
        name = "toped_group",
        pivotX = 18f,
        pivotY = 18f,
        scaleX = animatedScaleX.value,
        scaleY = animatedScaleY.value
    ) {
        TokopediaIcon()
    }
}

@Composable
private fun TokopediaIcon() = Path(
    name = "toped",
    pathData = TokopediaPath,
    fill = SolidColor(NestTheme.colors.GN._500),
    strokeLineWidth = 1f,
    pathFillType = PathFillType.EvenOdd
)

@Composable
private fun Circle(name: String, path: List<PathNode>) = Path(
    name = name,
    pathData = path,
    fill = SolidColor(NestTheme.colors.GN._500),
    strokeLineWidth = 1f,
    pathFillType = PathFillType.EvenOdd
)

internal suspend fun Animatable<Float, AnimationVector1D>.animateTo(
    target: Float,
    duration: Int = 0,
    delay: Int = 0,
    easing: Easing = FastOutSlowInEasing
) = this.animateTo(
    targetValue = target,
    animationSpec = tween(duration, delay, easing)
)

private val TokopediaPath by lazy {
    PathParser().parsePathString(
        "M 22.589 10 L 24.205 10 C 25.408 10 26.002 10.597 26.002 11.797 L 26.002 19.916 C 25.975 21.536 25.311 23.084 24.156 24.224 C 23.001 25.36 21.443 26 19.821 26 L 11.77 26 C 10.581 26 10 25.419 10 24.23 L 10 11.792 C 10 10.592 10.594 10 11.792 10 L 13.411 10 C 15.98 10 17.298 11.361 17.999 12.5 C 18.7 11.361 20.022 10 22.587 10 L 22.589 10 Z"
    ).toNodes()
}

private val CircleLeftPath by lazy {
    PathParser().parsePathString(
        "M 5.5 15.6 C 4.837 15.6 4.201 15.864 3.732 16.332 C 3.264 16.801 3 17.437 3 18.1 C 3 18.763 3.264 19.399 3.732 19.868 C 4.201 20.336 4.837 20.6 5.5 20.6 C 6.163 20.6 6.799 20.336 7.268 19.868 C 7.736 19.399 8 18.763 8 18.1 C 8 17.437 7.736 16.801 7.268 16.332 C 6.799 15.864 6.163 15.6 5.5 15.6 Z"
    ).toNodes()
}

private val CircleRightPath by lazy {
    PathParser().parsePathString(
        "M 30.5 15.6 C 29.837 15.6 29.201 15.864 28.732 16.332 C 28.264 16.801 28 17.437 28 18.1 C 28 18.763 28.264 19.399 28.732 19.868 C 29.201 20.336 29.837 20.6 30.5 20.6 C 31.163 20.6 31.799 20.336 32.268 19.868 C 32.736 19.399 33 18.763 33 18.1 C 33 17.437 32.736 16.801 32.268 16.332 C 31.799 15.864 31.163 15.6 30.5 15.6 Z"
    ).toNodes()
}

private val CircleCenterPath by lazy {
    PathParser().parsePathString(
        "M 18 14 C 16.94 14 15.921 14.422 15.172 15.172 C 14.422 15.921 14 16.94 14 18 C 14 19.06 14.422 20.079 15.172 20.828 C 15.921 21.578 16.94 22 18 22 C 19.06 22 20.079 21.578 20.828 20.828 C 21.578 20.079 22 19.06 22 18 C 22 16.94 21.578 15.921 20.828 15.172 C 20.079 14.422 19.06 14 18 14 Z"
    ).toNodes()
}

@Preview
@Composable
private fun NestDecorationLoader() {
    NestDecorativeLoader(
        modifier = Modifier.size(50.dp),
        isWhite = false
    )
}
