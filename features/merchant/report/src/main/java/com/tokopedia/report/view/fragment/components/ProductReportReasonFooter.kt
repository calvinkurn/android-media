package com.tokopedia.report.view.fragment.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
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
            .padding(16.dp),
        text = composeSpannable(text = text, onClick = onClick),
        textStyle = NestTheme.typography.body3
    )
}

@Composable
private fun composeSpannable(
    text: String,
    onClick: (String) -> Unit
) = buildAnnotatedString {
    withStyle(style = SpanStyle(color = NestTheme.colors.GN500)) {
        append("Pelajari Lebih Lanjut")
    }
    append(" ")
    append("tipe-tipe pelanggaran produk di Tokopedia")
}

@Preview
@Composable
fun ProductReportReasonFooterPreview() {
    ProductReportReasonFooter(
        text = ""
    ) {

    }
}