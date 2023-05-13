package com.tokopedia.common_compose.components.loader

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme
import kotlinx.coroutines.launch

/**
 * Created by yovi.putra on 13/05/23"
 * Project name: android-tokopedia-core
 **/

@Composable
internal fun NestCircularLoader(
    modifier: Modifier = Modifier,
) {
    val vectorPainter = rememberVectorPainter(
        defaultWidth = 500f.dp,
        defaultHeight = 500f.dp,
        viewportWidth = 500f,
        viewportHeight = 500f,
        autoMirror = false
    ) { _, _ ->
        Group(name = "_R_G") {
            // circle right
            CircleRightGroup()

            // circle left
            CircleLeftGroup()
        }
    }

    Image(
        modifier = modifier,
        painter = vectorPainter,
        contentDescription = "circular_loader"
    )
}

@Composable
private fun CircleLeftGroup() {
    Group(
        name = "_R_G_L_0_G_N_1_T_0",
        translationX = 250f,
        translationY = 250f
    ) {
        Group(
            name = "_R_G_L_0_G",
            rotation = -90.806f
        ) {
            val animatedTrimStart = remember { Animatable(0f) }
            val animatedTrimEnd = remember { Animatable(.001f) }

            LaunchedEffect(key1 = Unit, block = {
                launch {
                    animatedTrimStart.animateTo(target = 0f, duration = 526)
                }
                launch {
                    animatedTrimStart.animateTo(target = .999f, duration = 790, delay = 526)
                }
                launch {
                    animatedTrimEnd.animateTo(target = .001f, duration = 167)
                }
                launch {
                    animatedTrimEnd.animateTo(target = 1f, duration = 970, delay = 167)
                }
            })

            Circle(
                name = "_R_G_L_0_G_D_0_P_0",
                color = NestTheme.colors.GN._500,
                trimPathStart = animatedTrimStart.value,
                trimPathEnd = animatedTrimEnd.value
            )
        }
    }
}

@Composable
private fun CircleRightGroup() {
    Group(
        name = "_R_G_L_1_G_N_1_T_0",
        translationX = 250f,
        translationY = 250f
    ) {
        Group(name = "_R_G_L_1_G", rotation = 90f) {
            val animatedTrimStart = remember { Animatable(.999f) }
            val animatedTrimEnd = remember { Animatable(1f) }

            LaunchedEffect(key1 = Unit, block = {
                launch {
                    animatedTrimStart.animateTo(target = 0f, duration = 750)
                }
                launch {
                    animatedTrimEnd.animateTo(target = 1f, duration = 433)
                }

                launch {
                    animatedTrimEnd.animateTo(target = .001f, duration = 654, delay = 433)
                }
            })

            Circle(
                name = "_R_G_L_1_G_D_0_P_0",
                color = if (isSystemInDarkTheme()) NestTheme.colors.GN._700 else NestTheme.colors.GN._300,
                trimPathStart = animatedTrimStart.value,
                trimPathEnd = animatedTrimEnd.value
            )
        }
    }
}

private suspend fun Animatable<Float, AnimationVector1D>.animateTo(
    target: Float,
    duration: Int,
    delay: Int = 0
) = this.animateTo(
    targetValue = target,
    animationSpec = tween(
        durationMillis = duration,
        easing = FastOutSlowInEasing,
        delayMillis = delay
    )
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
fun NestCircularLoaderPreview() {
    NestCircularLoader(
        modifier = Modifier.size(50.dp)
    )
}
