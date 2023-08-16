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
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesDetailUiModel.StoriesDetailUiEvent.PAUSE
import com.tokopedia.stories.view.model.StoriesDetailUiModel.StoriesDetailUiEvent.START

@Composable
fun StoriesDetailTimer(
    itemCount: Int,
    data: StoriesDetailUiModel,
    timerFinished: () -> Unit,
) {
    /**
     * TODO
     * pause mechanism still buggy because we depend
     * on data to reset timer
     *
     * current -> everytime user pause will reset timer and
     * trigger timerFinished because anim.target value will be 0
     * look at line (63); if (anim.value == anim.targetValue) timerFinished.invoke()
     *
     * expected -> every state should be restart timer but pause state
     * how? (don't tell me to not using compose >,<)
     */
    val anim = remember(data) { Animatable(0F) }

    LaunchedEffect(data) {
        when (data.event) {
            PAUSE -> anim.stop()
            START -> {
                anim.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = (TIMER_DURATION * (1f - anim.value)).toInt(),
                        easing = LinearEasing,
                    )
                )
            }
        }

        if (anim.value == anim.targetValue) timerFinished.invoke()
    }

    StoriesDetailTimerContent(
        count = itemCount,
        currentPosition = data.selected,
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
        itemCount = 3,
        data = StoriesDetailUiModel(
            id = "1",
            selected = 1,
            event = START,
            imageContent = "",
        )
    ) { }
}

private const val TIMER_DURATION = 7 * 1000
