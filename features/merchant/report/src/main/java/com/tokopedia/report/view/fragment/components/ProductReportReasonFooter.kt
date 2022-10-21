package com.tokopedia.report.view.fragment.components

import android.content.Context
import android.text.method.LinkMovementMethod
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportReasonFooter(
    context: Context = LocalContext.current,
    text: String,
    onClick: (String) -> Unit
) {
    NestTypography(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = text,
        textStyle = NestTheme.typography.body3
    )
}

private fun composeSpannable(
    typography: Typography,
    text: String,
    onClick: (String) -> Unit
) = with(typography) {
    movementMethod = LinkMovementMethod.getInstance()
    val helper = HtmlLinkHelper(
        context = context,
        htmlString = text
    )
    helper.urlList.forEach {
        it.onClick = {
            onClick.invoke(it.linkUrl)
        }
    }
    this.text = helper.spannedString
}

@Preview
@Composable
fun ProductReportReasonFooterPreview() {
    /*ProductReportReasonFooter(
        stringResource(id = com.tokopedia.report.R.string.product_report_see_all_types)
    ) {

    }*/
}