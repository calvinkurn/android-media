package com.tokopedia.sellerpersona.view.compose.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * Created by @ilhamsuaib on 21/07/23.
 */

private val TrackWidth = 42.dp
private val ThumbDiameter = 22.dp
private val ThumbAndTrackSpace = 3.dp
private val SwitchWidth = TrackWidth

private const val BiasEnd = 1f
private const val BiasStart = -1f

@Composable
fun Switch(
    isSwitchedOn: Boolean,
    onCheckedChanged: ((Boolean) -> Unit)? = null
) {

    val staticThumbColor = NestNN.light._0
    val checkedTrackColor: Color = NestTheme.colors.GN._500
    val uncheckedTrackColor: Color = NestTheme.colors.NN._900

    val switchState = remember {
        mutableStateOf(isSwitchedOn)
    }

    val alignment by animateAlignmentAsState(if (switchState.value) BiasEnd else BiasStart)

    Box(
        modifier = Modifier
            .width(SwitchWidth)
            .background(
                color = if (switchState.value) checkedTrackColor else uncheckedTrackColor,
                shape = RoundedCornerShape(percent = 50)
            )
            .clickable(
                indication = null, interactionSource = MutableInteractionSource()
            ) {
                switchState.value = !switchState.value
                onCheckedChanged?.invoke(switchState.value)
            }, contentAlignment = alignment
    ) {

        Box(
            modifier = Modifier
                .size(size = ThumbDiameter)
                .padding(all = ThumbAndTrackSpace)
                .background(color = staticThumbColor, shape = CircleShape)
        )
    }
}

@Composable
private fun animateAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment> {
    val bias by animateFloatAsState(targetBiasValue)
    return remember {
        derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
    }
}