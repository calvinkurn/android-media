package com.tokopedia.mediauploader.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.mediauploader.LogType

@Composable
fun LogItem(type: String, content: String) {
    Column(
        modifier = Modifier
            .border(border = BorderStroke(0.5.dp, NestTheme.colors.NN._300), shape = RoundedCornerShape(8.dp))
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        NestTypography(
            textStyle = NestTheme.typography.display3,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = NestTheme.colors.NN._600)) {
                    append(type)
                }
            }
        )

        Spacer(modifier = Modifier.height(2.dp))

        NestTypography(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = NestTheme.colors.NN._800
                    )
                ) {
                    append(content)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LogItemPreview() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(8.dp)
    ) {
        item {
            LogItem(
                type = LogType.map(LogType.CompressInfo),
                content = buildString {
                    append("foo: bar")
                    append("\n")
                    append("loren: ipsum")
                }
            )
        }
    }
}
