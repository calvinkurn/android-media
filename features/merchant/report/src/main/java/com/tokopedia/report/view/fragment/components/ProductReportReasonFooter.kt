package com.tokopedia.report.view.fragment.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
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

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportReasonFooter(
    text: String,
    onClick: (String) -> Unit
) {
    NestTypography(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick.invoke(text)
            },
        text = composeSpannable(text = text, onClick = onClick),
        textStyle = NestTheme.typography.body3
    )
}

@Composable
private fun composeSpannable(
    text: String,
    onClick: (String) -> Unit
) = buildAnnotatedString {
    withStyle(
        style = SpanStyle(
            color = NestTheme.colors.GN._500,
            fontWeight = FontWeight.Bold
        )
    ) {
        append("Pelajari Lebih Lanjut")
    }
    append(" ")
    append("tipe-tipe pelanggaran produk di Tokopedia")
}

@Composable
fun AnnotatedClickableText() {
    val annotatedText = buildAnnotatedString {
        append("Click ")

        // We attach this *URL* annotation to the following content
        // until `pop()` is called
        pushStringAnnotation(
            tag = "URL",
            annotation = "https://developer.android.com"
        )
        withStyle(
            style = SpanStyle(
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("here")
        }

        pop()
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            // We check if there is an *URL* annotation attached to the text
            // at the clicked position
            annotatedText.getStringAnnotations(
                tag = "URL",
                start = offset,
                end = offset
            ).firstOrNull()?.let { annotation ->
                // If yes, we log its value
                Log.d("Clicked URL", annotation.item)
            }
        }
    )
}

@Preview
@Composable
fun ProductReportReasonFooterPreview() {
    Column {
        AnnotatedClickableText()
        ProductReportReasonFooter(
            text = ""
        ) {

        }
    }
}
