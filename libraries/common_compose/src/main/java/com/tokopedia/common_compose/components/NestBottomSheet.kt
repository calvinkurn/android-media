package com.tokopedia.common_compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestBottomSheet(
    title: String,
    modifier: Modifier = Modifier,
    closeButtonColor: Color = Color.Gray,
    onClosePressed: () -> Unit,
    content: @Composable() () -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            NestTypography(
                text = title,
                textStyle = NestTheme.typography.heading3.copy(color = NestTheme.colors.NN._950)
            )
            IconButton(
                onClick = onClosePressed,
                modifier = modifier
                    .size(24.dp)
            ) {
                Icon(Icons.Filled.Close, tint = closeButtonColor, contentDescription = null)
            }
        }
        content()
    }
}

fun NestBottomSheetShape(): RoundedCornerShape {
    return RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp,
        bottomEnd = 0.dp,
        bottomStart = 0.dp
    )
}

@Preview
@Composable
fun NestBottomSheetPreview() {
    NestBottomSheet(title = "bottom sheet title", onClosePressed = {}) {
        NestTypography(text = "sample text")
    }
}
