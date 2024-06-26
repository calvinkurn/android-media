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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.utils.isDeviceAnimationsEnabled
import com.tokopedia.stories.view.viewmodel.state.TimerStatusInfo
import kotlinx.coroutines.delay
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@Composable
fun StoriesDetailTimer(
    timerInfo: TimerStatusInfo,
    timerFinished: () -> Unit
) {
    if (LocalContext.current.isDeviceAnimationsEnabled()) {
        StoriesTimerAnimationEnabled(timerInfo = timerInfo, timerFinished = timerFinished)
    } else {
        StoriesTimerAnimationDisabled(timerInfo = timerInfo, timerFinished = timerFinished)
    }
}

@Composable
private fun StoriesTimerAnimationEnabled(
    timerInfo: TimerStatusInfo,
    timerFinished: () -> Unit
) {
    val anim = remember(timerInfo.story.id, timerInfo.story.resetValue) {
        Animatable(INITIAL_ANIMATION)
    }

    LaunchedEffect(timerInfo) {
        when (timerInfo.event) {
            StoriesDetailItemUiEvent.PAUSE, StoriesDetailItemUiEvent.BUFFERING -> anim.stop()
            StoriesDetailItemUiEvent.RESUME -> {
                delay(DELAY_MILLIS)
                anim.animateTo(
                    targetValue = TARGET_ANIMATION,
                    animationSpec = tween(
                        durationMillis = (timerInfo.story.duration * (TARGET_ANIMATION - anim.value)).toInt(),
                        easing = LinearEasing
                    )
                )
            }
        }
        if ((anim.value >= anim.targetValue) && (anim.targetValue != INITIAL_ANIMATION)) timerFinished.invoke()
    }

    StoriesDetailTimerContent(
        count = timerInfo.story.itemCount,
        currentPosition = timerInfo.story.position,
        progress = anim.value
    )
}

@Composable
private fun StoriesTimerAnimationDisabled(
    timerInfo: TimerStatusInfo,
    timerFinished: () -> Unit
) {
    val timer = remember(timerInfo.story.id, timerInfo.story.resetValue) {
        mutableStateOf(INITIAL_ANIMATION)
    }
    val currentTime = remember(timerInfo.story.id, timerInfo.story.resetValue) {
        mutableStateOf(0L)
    }
    val isTimerRunning = remember(timerInfo.story.id, timerInfo.story.resetValue) {
        mutableStateOf(false)
    }

    LaunchedEffect(timerInfo, currentTime.value, isTimerRunning.value) {
        when (timerInfo.event) {
            StoriesDetailItemUiEvent.PAUSE,
            StoriesDetailItemUiEvent.BUFFERING -> isTimerRunning.value = false
            StoriesDetailItemUiEvent.RESUME -> {
                isTimerRunning.value = true
                if (currentTime.value < timerInfo.story.duration && isTimerRunning.value) {
                    delay(DELAY_MILLIS)
                    currentTime.value += DELAY_MILLIS
                    timer.value = currentTime.value / timerInfo.story.duration.toFloat()
                }
            }
        }
        if (timer.value >= TARGET_ANIMATION) timerFinished.invoke()
    }

    StoriesDetailTimerContent(
        count = timerInfo.story.itemCount,
        currentPosition = timerInfo.story.position,
        progress = timer.value
    )
}

@Composable
private fun StoriesDetailTimerContent(
    count: Int,
    currentPosition: Int,
    progress: Float
) {
    NestTheme(darkTheme = true) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(Color.Transparent)
                .height(2.dp)
        ) {
            for (index in 0 until count) {
                Row(
                    modifier = Modifier
                        .height(2.dp)
                        .clip(RoundedCornerShape(60))
                        .weight(1f)
                        .background(
                            colorResource(id = unifyprinciplesR.color.Unify_Static_White)
                                .copy(alpha = 0.4f)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .background(colorResource(id = unifyprinciplesR.color.Unify_Static_White))
                            .fillMaxHeight().let {
                                when (index) {
                                    currentPosition -> it.fillMaxWidth(progress)
                                    in 0..currentPosition -> it.fillMaxWidth(1f)
                                    else -> it
                                }
                            }
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
        timerInfo = TimerStatusInfo.Empty
    ) { }
}

private const val DELAY_MILLIS = 100L
private const val TARGET_ANIMATION = 1F
private const val INITIAL_ANIMATION = 0F
