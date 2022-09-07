package com.tokopedia.report.view.fragment.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tokopedia.report.view.fragment.unify_components.TextUnify
import com.tokopedia.report.view.fragment.unify_components.TextUnifyType
import com.tokopedia.report.view.fragment.unify_components.TextUnifyWeight

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportReasonFooter(
    text: String
) {
    TextUnify(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        type = TextUnifyType.Body3,
        weight = TextUnifyWeight.Regular,
        properties = { context ->
            val color = ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_96
            )
            setTextColor(color)
            setText(text)
        }
    )
}

@Preview
@Composable
fun ProductReportReasonFooterPreview() {
    ProductReportReasonHeader(
        stringResource(id = com.tokopedia.report.R.string.product_report_header)
    )
}