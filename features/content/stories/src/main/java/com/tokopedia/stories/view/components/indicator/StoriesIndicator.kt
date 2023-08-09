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
import com.tokopedia.stories.view.model.StoriesDataUiModel

@Composable
fun StoriesIndicator(
    data: StoriesDataUiModel,
    event: (StoriesIndicatorEvent) -> Unit,
) {
    val anim = remember { Animatable(0F) }
    LaunchedEffect(data) {
        if (data.isPause) anim.stop()
        else {
            anim.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = (7000 * (1f - anim.value)).toInt(),
                    easing = LinearEasing,
                )
            )
            event.invoke(
                if (data.selected >= data.count) StoriesIndicatorEvent.NEXT_CATEGORIES
                else StoriesIndicatorEvent.NEXT_STORIES
            )
            anim.snapTo(0F)
        }
    }
    StoriesIndicatorBar(
        count = data.count,
        position = data.selected,
        progress = anim.value,
    )
}

@Composable
private fun StoriesIndicatorBar(
    count: Int,
    position: Int,
    progress: Float,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(48.dp)
            .padding(horizontal = 8.dp),
    ) {
        for (index in 1..count) {
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
                                position -> it.fillMaxWidth(progress)
                                in 0..position -> it.fillMaxWidth(1f)
                                else -> it
                            }
                        },
                )
            }
            if (index != count) Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
internal fun StoriesIndicatorPreview() {
    StoriesIndicator(data = StoriesDataUiModel.Empty) { }
}

enum class StoriesIndicatorEvent {
    NEXT_STORIES,
    NEXT_CATEGORIES,
}
