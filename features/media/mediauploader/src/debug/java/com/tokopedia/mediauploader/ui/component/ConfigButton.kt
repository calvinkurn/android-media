package com.tokopedia.mediauploader.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
inline fun ConfigButton(
    modifier: Modifier = Modifier,
    crossinline onClick: () -> Unit
) {
    Button(
        shape = RoundedCornerShape(65.dp),
        colors = ButtonDefaults.buttonColors(
            NestTheme.colors.NN._0
        ),
        modifier = modifier
            .padding(6.dp),
        onClick = {
            onClick()
        }
    ) {
        Text(
            text = "Config",
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            modifier = Modifier
        )
    }
}

@Preview
@Composable
fun ConfigButtonPreview() {
    ConfigButton(onClick = {})
}
