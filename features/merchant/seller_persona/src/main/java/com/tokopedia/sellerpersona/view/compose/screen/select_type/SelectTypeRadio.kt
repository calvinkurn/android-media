package com.tokopedia.sellerpersona.view.compose.screen.select_type

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * Created by @ilhamsuaib on 26/07/23.
 */

@Composable
internal fun RadioButtonComponent(
    modifier: Modifier = Modifier, isChecked: Boolean, onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = NestTheme.colors.NN._0, shape = RoundedCornerShape(
                        bottomStart = 40.dp, bottomEnd = 28.dp, topStart = 40.dp, topEnd = 16.dp
                    )
                )
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource(),
                    onClick = onClick
                ), contentAlignment = Alignment.Center
        ) {
            RadioButton(
                selected = isChecked, onClick = onClick, modifier = Modifier.size(24.dp)
            )
        }
    }
}