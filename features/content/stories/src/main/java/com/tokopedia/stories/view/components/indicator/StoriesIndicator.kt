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

@Composable
fun StoriesIndicator(
    storiesCount: Int,
    storiesCurrentPosition: Int,
    paused: Boolean,
    onFinished: () -> Unit,
) {
    val percent = remember { Animatable(0f) }
    LaunchedEffect(paused) {
        if (paused) percent.stop()
        else {
            percent.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = (7000 * (1f - percent.value)).toInt(),
                    easing = LinearEasing
                )
            )
            onFinished()
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(48.dp)
            .padding(8.dp, 0.dp),

        ) {
        for (index in 1..storiesCount) {
            Row(
                modifier = Modifier
                    .height(4.dp)
                    .clip(RoundedCornerShape(50, 50, 50, 50))
                    .weight(1f)
                    .background(Color.White.copy(alpha = 0.4f))
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxHeight().let {
                            when (index) {
                                storiesCurrentPosition -> it.fillMaxWidth(percent.value)
                                in 0..storiesCurrentPosition -> it.fillMaxWidth(1f)
                                else -> it
                            }
                        },
                )
            }
            if (index != storiesCount) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Preview
@Composable
fun StoriesIndicatorPreview() {
    StoriesIndicator(storiesCount = 3, storiesCurrentPosition = 0, paused = false) { }
}
