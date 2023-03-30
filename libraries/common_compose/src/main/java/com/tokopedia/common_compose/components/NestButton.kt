package com.tokopedia.common_compose.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val backgroundColor = ButtonDefaults.buttonColors(
        backgroundColor = NestTheme.colors.GN._500,
        disabledBackgroundColor = NestTheme.colors.NN._100
    )

    Button(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = backgroundColor,
        enabled = enabled
    ) {
        NestTypography(
            text,
            textStyle = NestTheme.typography.display1.copy(
                color = buttonTextColor(enabled),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(vertical = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun buttonTextColor(enabled: Boolean): Color {
    return if (enabled) {
        if (isSystemInDarkTheme()) {
            NestTheme.colors.NN._1000
        } else {
            NestTheme.colors.NN._0
        }
    } else {
        NestTheme.colors.NN._400
    }
}

@Preview(name = "Button")
@Composable
fun NestButtonPreview() {
    NestButton(
        Modifier,
        text = "Bagikan",
        onClick = {}
    )
}

@Preview(name = "Button disabled")
@Composable
fun NestButtonDisabledPreview() {
    NestButton(
        Modifier,
        text = "Bagikan",
        onClick = {},
        enabled = false
    )
}
