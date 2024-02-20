package com.tokopedia.product.detail.view.viewholder.media.component

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestRN
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.product.detail.databinding.WidgetVideoPictureBinding
import com.tokopedia.product.detail.view.util.setContentUi

/**
 * Created by yovi.putra on 2/20/24.
 * Copyright (c) 2024 android-tokopedia-core All rights reserved.
 */

@Composable
private fun LiveIndicatorComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val animationState = blinkOutAnimationState()

    Row(
        modifier = modifier
            .requiredHeight(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(NestRN.light._600)
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        NestTypography(
            text = "●",
            textStyle = NestTheme.typography.display2.copy(
                fontWeight = FontWeight.Bold,
                color = NestNN.light._0
            ),
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(bottom = 1.5.dp)
                .graphicsLayer(alpha = animationState.value)
        )

        NestTypography(
            text = "LIVE",
            textStyle = NestTheme.typography.small.copy(
                fontWeight = FontWeight.Bold,
                color = NestNN.light._0
            )
        )

        NestIcon(
            iconId = IconUnify.CHEVRON_RIGHT,
            colorLightEnable = NestNN.light._0,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun blinkOutAnimationState(): State<Float> {
    val infiniteTransition = rememberInfiniteTransition()
    val blinkOutEaseInOut = remember { CubicBezierEasing(0.63f, 0.01f, 0.29f, 1.0f) }

    return infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 300, delayMillis = 500, easing = blinkOutEaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )
}

fun WidgetVideoPictureBinding.liveIndicator(
    shouldShow: Boolean,
    onClick: () -> Unit
) {
    liveIndicatorContainer.setContentUi {
        if (shouldShow) {
            NestTheme {
                LiveIndicatorComponent(onClick = onClick)
            }
        }
    }
}

@Preview
@Composable
private fun LiveIndicatorPreview() {
    NestTheme {
        Box(modifier = Modifier, contentAlignment = Alignment.Center) {
            LiveIndicatorComponent {

            }
        }
    }
}
