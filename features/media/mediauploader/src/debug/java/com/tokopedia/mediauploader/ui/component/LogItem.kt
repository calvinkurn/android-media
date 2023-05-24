package com.tokopedia.mediauploader.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.tokopedia.mediauploader.data.entity.LogType
import com.tokopedia.mediauploader.data.entity.Logs

@Composable
fun LogSubItem(log: Logs, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .border(
                border = BorderStroke(0.5.dp, NestTheme.colors.NN._300),
                shape = RoundedCornerShape(4.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        NestTypography(
            modifier = Modifier.weight(1f),
            textStyle = NestTheme.typography.display3,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = NestTheme.colors.NN._600)) {
                    append(log.title)
                }
            }
        )

        NestTypography(
            modifier = Modifier.weight(2.5f),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = NestTheme.colors.NN._800
                    )
                ) {
                    append(log.value)
                }
            }
        )
    }
}

@Composable
fun LogItem(type: String, logs: List<Logs>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .border(
                border = BorderStroke(0.5.dp, NestTheme.colors.NN._300),
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        NestTypography(
            textStyle = NestTheme.typography.heading6,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = NestTheme.colors.NN._900)) {
                    append(type)
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        logs.forEach {
            LogSubItem(
                log = it,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogSubItemPreview() {
    LogSubItem(Logs("Foo", "Bar"))
}

@Preview(showBackground = true)
@Composable
fun LogItemPreview() {
    LogItem(
        type = LogType.map(LogType.CompressInfo),
        logs = listOf(
            Logs("Upload ID", "foo-bar"),
            Logs("Path", "/DCIM/Camera/Test/sample.jpg")
        )
    )
}
