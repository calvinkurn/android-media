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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.ui.NestTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    sizeConfig: StoriesAvatarView.SizeConfiguration = StoriesAvatarView.SizeConfiguration.Default,
) {
    val scale = remember { Animatable(0.97f) }
    val rotation = remember { Animatable(0f) }
    val dashedAlpha = remember { Animatable(1f) }

    LaunchedEffect(storiesStatus) {
        if (storiesStatus != StoriesStatus.HasUnseenStories) return@LaunchedEffect

        scale.animateTo(
            0.95f, tween(200)
        )

        val scaleUp = async {
            scale.animateTo(0.97f, tween(200))
        }
        val alphaDashed = async {
            dashedAlpha.animateTo(0f, tween(200))
        }

        awaitAll(scaleUp, alphaDashed)

        val fullRotate = async {
            rotation.animateTo(160f, tween(1200, easing = LinearEasing))
            rotation.animateTo(360f, tween(600, easing = FastOutLinearInEasing))
        }

        val opaqueDashed = async {
            delay(1200)
            launch {
                dashedAlpha.animateTo(1f, tween(durationMillis = 400))
            }
            launch {
                scale.animateTo(1f, tween(400))
                scale.animateTo(0.97f, tween(400))
            }
        }

        awaitAll(fullRotate, opaqueDashed)
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
                    .storiesBorder(storiesStatus, rotation.value, scale.value, dashedAlpha.value)
            )

            NestImage(
                imageUrl = imageUrl,
                Modifier
                    .matchParentSize()
                    .padding(sizeConfig.imageToBorderGap)
                    .clip(CircleShape)
            )
        }
    }
}

private fun Modifier.storiesBorder(
    storiesStatus: StoriesStatus,
    rotation: Float,
    scale: Float,
    dashedAlpha: Float,
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
                    radius = size.width / 2,
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
                    radius = size.width / 2 - storiesStatus.borderDp.toPx(),
                    blendMode = BlendMode.DstOut,
                )
            }

            restoreToCount(checkPoint)
        }
    }
)

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

private val StoriesStatus.borderDp: Dp
    get() = when (this) {
        StoriesStatus.NoStories -> 1.dp
        StoriesStatus.HasUnseenStories -> 3.dp
        StoriesStatus.AllStoriesSeen -> 1.dp
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
