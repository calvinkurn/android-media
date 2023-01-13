package com.tokopedia.common_compose.principles

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val backgroundColor = ButtonDefaults.buttonColors(backgroundColor = NestTheme.colors.GN._500)

    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = backgroundColor,
        enabled = enabled
    ) {
        NestTypography(
            text,
            textStyle = NestTheme.typography.display1.copy(
                color = NestTheme.colors.NN._0,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(vertical = 10.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
