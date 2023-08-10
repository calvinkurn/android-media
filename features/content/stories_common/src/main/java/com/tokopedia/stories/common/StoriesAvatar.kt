package com.tokopedia.stories.common

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.ui.NestTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
@Composable
internal fun StoriesAvatarContent(
    imageUrl: String,
    storiesStatus: StoriesStatus,
    modifier: Modifier = Modifier,
    sizeConfig: StoriesAvatarView.SizeConfiguration = StoriesAvatarView.SizeConfiguration.Default
) {
    val animationState = rememberAvatarAnimationState()

    LaunchedEffect(storiesStatus) {
        if (storiesStatus != StoriesStatus.HasUnseenStories) return@LaunchedEffect
        animationState.animate()
    }

    NestTheme {
        Box(
            modifier
                .fillMaxSize()
                .clip(CircleShape)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .storiesBorder(
                        storiesStatus,
                        sizeConfig,
                        animationState.rotation,
                        animationState.scale,
                        animationState.dashedAlpha,
                    )
            )

            NestImage(
                imageUrl = imageUrl,
                Modifier
                    .matchParentSize()
                    .padding(if (storiesStatus == StoriesStatus.NoStories) 0.dp else sizeConfig.imageToBorderGap)
                    .clip(CircleShape)
            )
        }
    }
}

private fun Modifier.storiesBorder(
    storiesStatus: StoriesStatus,
    sizeConfig: StoriesAvatarView.SizeConfiguration,
    rotation: Float,
    scale: Float,
    dashedAlpha: Float
): Modifier = this.then(
    drawBehind {
        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            withTransform(
                transformBlock = {
                    scale(scale, scale)
                    rotate(rotation)
                }
            ) {
                drawCircle(
                    brush = storiesStatus.brush,
                    radius = size.width / 2
                )

                repeat(4) {
                    drawArc(
                        color = Color.Gray,
                        20f + it * 90f,
                        15f,
                        useCenter = true,
                        alpha = dashedAlpha,
                        blendMode = BlendMode.DstIn
                    )

                    drawArc(
                        color = Color.Gray,
                        75f + it * 90f,
                        15f,
                        useCenter = true,
                        alpha = dashedAlpha,
                        blendMode = BlendMode.DstIn
                    )
                }

                drawCircle(
                    color = Color.Gray,
                    radius = size.width / 2 - storiesStatus.borderDp(sizeConfig.unseenStoriesBorder).toPx(),
                    blendMode = BlendMode.DstOut
                )
            }

            restoreToCount(checkPoint)
        }
    }
)

@Composable
internal fun rememberAvatarAnimationState() = remember { StoriesAvatarAnimationState() }

@Stable
internal class StoriesAvatarAnimationState {

    private val _scale = Animatable(0.97f)
    val scale get() = _scale.value

    private val _rotation = Animatable(0f)
    val rotation get() = _rotation.value

    private val _dashedAlpha = Animatable(1f)
    val dashedAlpha get() = _dashedAlpha.value

    suspend fun animate() = coroutineScope {
        _scale.animateTo(
            0.94f,
            tween(200)
        )

        val scaleUp = async {
            _scale.animateTo(0.97f, tween(200))
        }
        val alphaDashed = async {
            _dashedAlpha.animateTo(0f, tween(200))
        }

        awaitAll(scaleUp, alphaDashed)

        val fullRotate = async {
            _rotation.animateTo(160f, tween(1200, easing = LinearEasing))
            _rotation.animateTo(360f, tween(600, easing = FastOutLinearInEasing))
        }

        val opaqueDashed = async {
            delay(1200)
            launch {
                _dashedAlpha.animateTo(1f, tween(durationMillis = 400))
            }
            launch {
                _scale.animateTo(1f, tween(400))
                _scale.animateTo(0.97f, tween(400))
            }
        }

        awaitAll(fullRotate, opaqueDashed)
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NoStoriesAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        StoriesStatus.NoStories,
        Modifier.size(200.dp)
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HasUnseenStoriesAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        StoriesStatus.HasUnseenStories,
        Modifier.size(200.dp)
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AllStoriesSeenAvatarPreview() {
    StoriesAvatarContent(
        "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg",
        StoriesStatus.AllStoriesSeen,
        Modifier.size(200.dp)
    )
}

private fun StoriesStatus.borderDp(hasStoriesBorder: Dp = 3.dp): Dp {
    return when (this) {
        StoriesStatus.NoStories -> 1.dp
        StoriesStatus.HasUnseenStories -> hasStoriesBorder
        StoriesStatus.AllStoriesSeen -> 1.dp
    }
}

private val StoriesStatus.brush: Brush
    get() = when (this) {
        StoriesStatus.NoStories -> SolidColor(Color(0x00000000))
        StoriesStatus.HasUnseenStories -> {
            Brush.linearGradient(
                0f to Color(0xFF69F2E2),
                1f to Color(0xFF00AA5B)
            )
        }
        StoriesStatus.AllStoriesSeen -> SolidColor(Color(0xFFD6DFEB))
    }
