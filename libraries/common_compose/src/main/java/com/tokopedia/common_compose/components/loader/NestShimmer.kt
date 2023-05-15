package com.tokopedia.common_compose.components.loader

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme

/**
 * Created by yovi.putra on 31/03/23"
 * Project name: compose_skeleton
 **/

@Composable
fun Float.px(): Float = this / LocalDensity.current.density

@Composable
internal fun NestShimmer(
    modifier: Modifier = Modifier,
    type: NestShimmerType
) {
    when (type) {
        is NestShimmerType.Rect -> {
            NestShimmerLayout(modifier = modifier, rounded = type.rounded)
        }

        else -> {
            val height = remember { mutableStateOf(0) }
            NestShimmerLayout(
                modifier = modifier
                    .onGloballyPositioned {
                        val heightSize = it.size.height
                        if (height.value != heightSize) {
                            height.value = heightSize
                        }
                    },
                rounded = (height.value / 2).dp
            )
        }
    }
}

@Composable
private fun NestShimmerLayout(
    modifier: Modifier = Modifier,
    rounded: Dp
) {
    val width = remember { mutableStateOf(0f) }
    val firstDuration = 899
    val secondaryDuration = 301
    val firstSizing by animateDpAsState(
        targetValue = width.value.px().dp,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = firstDuration,
                delayMillis = secondaryDuration,
                easing = LinearOutSlowInEasing
            )
        )
    )
    val secondarySizing by animateDpAsState(
        targetValue = width.value.px().dp,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = secondaryDuration,
                delayMillis = firstDuration
            )
        )
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(rounded))
            .background(NestTheme.colors.NN._100)
            .onSizeChanged {
                val widthSize = it.width.toFloat()
                if (width.value != widthSize) {
                    width.value = widthSize
                }
            }
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(firstSizing)
                .background(NestTheme.colors.NN._200)
        )

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(secondarySizing)
                .background(NestTheme.colors.NN._100)
        )
    }
}

@Preview
@Composable
private fun ShimmerPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NestShimmer(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            type = NestShimmerType.Rect()
        )

        NestShimmer(
            modifier = Modifier
                .size(100.dp, 50.dp),
            type = NestShimmerType.Rect(rounded = 8.dp)
        )

        NestShimmer(
            modifier = Modifier
                .size(100.dp, 50.dp),
            type = NestShimmerType.Rect(rounded = 16.dp)
        )

        NestShimmer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp),
            type = NestShimmerType.Line
        )

        NestShimmer(
            modifier = Modifier.size(100.dp, 16.dp),
            type = NestShimmerType.Line
        )

        NestShimmer(
            modifier = Modifier.size(100.dp),
            type = NestShimmerType.Circle
        )
    }
}
