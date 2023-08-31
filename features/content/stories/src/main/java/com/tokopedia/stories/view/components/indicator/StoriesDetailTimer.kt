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
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesDetailItemUiEvent
import kotlinx.coroutines.delay

@Composable
fun StoriesDetailTimer(
    currentPosition: Int,
    itemCount: Int,
    data: StoriesDetailItemUiModel,
    timerFinished: () -> Unit,
) {
    val anim = remember(
        currentPosition,
        data.id,
        data.resetValue,
    ) { Animatable(INITIAL_ANIMATION) }

    LaunchedEffect(data) {
        when (data.event) {
            StoriesDetailItemUiEvent.PAUSE -> anim.stop()
            StoriesDetailItemUiEvent.RESUME -> {
                delay(100)
                anim.animateTo(
                    targetValue = TARGET_ANIMATION,
                    animationSpec = tween(
                        durationMillis = (TIMER_DURATION * (TARGET_ANIMATION - anim.value)).toInt(),
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .background(Color.Transparent),
    ) {
        for (index in 0 until count) {
            Row(
                modifier = Modifier
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .weight(1f)
                    .background(Color.White.copy(alpha = 0.4f))
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxHeight().let {
                            when (index) {
                                currentPosition -> it.fillMaxWidth(progress)
                                in 0..currentPosition -> it.fillMaxWidth(1f)
                                else -> it
                            }
                        },
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun StoriesDetailTimerPreview() {
    StoriesDetailTimer(
        currentPosition = 0,
        itemCount = 3,
        data = StoriesDetailItemUiModel()
    ) { }
}

private const val TARGET_ANIMATION = 1F
private const val INITIAL_ANIMATION = 0F
private const val TIMER_DURATION = 7 * 1000
