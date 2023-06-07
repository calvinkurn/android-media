package com.tokopedia.report.view.fragment.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportReasonHeader(
    text: String
) {
    NestTypography(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = text,
        textStyle = NestTheme.typography.heading3
    )
}

@Preview
@Composable
fun ProductReportReasonHeaderPreview() {
    ProductReportReasonHeader(
        stringResource(id = com.tokopedia.report.R.string.product_report_header)
    )
}
