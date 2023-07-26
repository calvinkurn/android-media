package com.tokopedia.sellerpersona.view.compose.screen.selecttype

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.ui.NestNN
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * Created by @ilhamsuaib on 26/07/23.
 */

@Composable
internal fun RadioButtonComponent(
    modifier: Modifier = Modifier, isChecked: Boolean, onClicked: () -> Unit
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = NestNN.light._0, shape = RoundedCornerShape(
                        bottomStart = 40.dp, bottomEnd = 28.dp, topStart = 40.dp, topEnd = 16.dp
                    )
                )
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource(),
                    onClick = onClicked
                ), contentAlignment = Alignment.Center
        ) {
            RadioButton(
                selected = isChecked,
                onClick = onClicked,
                modifier = Modifier.size(24.dp),
                colors = RadioButtonDefaults.colors(
                    selectedColor = NestTheme.colors.GN._500,
                    unselectedColor = NestTheme.colors.NN._900
                )
            )
        }
    }
}