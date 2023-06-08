package com.tokopedia.report.view.fragment.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

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
                onClick(text)
            },
        text = composeSpannable(),
        textStyle = NestTheme.typography.body3
    )
}

@Composable
private fun composeSpannable() = buildAnnotatedString {
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

@Preview
@Composable
fun ProductReportReasonFooterPreview() {
    Column {
        ProductReportReasonFooter(
            text = ""
        ) {
        }
    }
}
