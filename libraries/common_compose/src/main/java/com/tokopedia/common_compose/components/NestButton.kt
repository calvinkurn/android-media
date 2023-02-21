package com.tokopedia.common_compose.principles

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tokopedia.common_compose.ui.NestTheme

@Composable
fun NestButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {

    val backgroundColor = ButtonDefaults.buttonColors(backgroundColor = NestTheme.colors.GN._500)

    Button(
        modifier = modifier.height(32.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = backgroundColor
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
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

