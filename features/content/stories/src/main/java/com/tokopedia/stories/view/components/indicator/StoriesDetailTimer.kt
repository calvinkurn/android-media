package com.tokopedia.stories.view.components.indicator

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent
import kotlinx.coroutines.delay

@Composable
fun StoriesDetailTimer(
    currentPosition: Int,
    itemCount: Int,
    data: StoriesDetailItem,
    timerFinished: () -> Unit,
) {
    val anim = remember(data.id, data.resetValue) { Animatable(INITIAL_ANIMATION) }

    LaunchedEffect(data) {
        when (data.event) {
            StoriesDetailItemUiEvent.PAUSE -> anim.stop()
            StoriesDetailItemUiEvent.RESUME -> {
                delay(100)
                anim.animateTo(
                    targetValue = TARGET_ANIMATION,
                    animationSpec = tween(
                        durationMillis = (data.content.duration * (TARGET_ANIMATION - anim.value)).toInt(),
                        easing = LinearEasing,
                    )
                )
            }
        }

        if ((anim.value == anim.targetValue) && (anim.targetValue != 0F)) timerFinished.invoke()
    }

    StoriesDetailTimerContent(
        count = itemCount,
        currentPosition = currentPosition,
        progress = anim.value,
    )
}

@Composable
private fun StoriesDetailTimerContent(
    count: Int,
    currentPosition: Int,
    progress: Float,
) {
    NestTheme(darkTheme = false) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(Color.Transparent)
                .height(2.dp),
        ) {
            for (index in 0 until count) {
                Row(
                    modifier = Modifier
                        .height(2.dp)
                        .clip(RoundedCornerShape(60))
                        .weight(1f)
                        .background(NestTheme.colors.NN._100.copy(alpha = 0.4f))
                ) {
                    Box(
                        modifier = Modifier
                            .background(NestTheme.colors.NN._100)
                            .fillMaxHeight().let {
                                when (index) {
                                    currentPosition -> it.fillMaxWidth(progress)
                                    in 0..currentPosition -> it.fillMaxWidth(1f)
                                    else -> it
                                }
                            },
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun StoriesDetailTimerPreview() {
    StoriesDetailTimer(
        currentPosition = 0,
        itemCount = 3,
        data = StoriesDetailItem()
    ) { }
}

private const val TARGET_ANIMATION = 1F
private const val INITIAL_ANIMATION = 0F
