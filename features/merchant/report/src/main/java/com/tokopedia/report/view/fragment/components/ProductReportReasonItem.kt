package com.tokopedia.report.view.fragment.components

import android.text.TextUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.unify_components.TextUnify
import com.tokopedia.report.view.fragment.unify_components.TextUnifyType
import com.tokopedia.report.view.fragment.unify_components.TextUnifyWeight

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportReasonItem(
    reason: ProductReportReason,
    onClick: (ProductReportReason) -> Unit
) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .clickable {
                onClick.invoke(reason)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            TextUnify(
                modifier = Modifier
                    .padding(16.dp),
                type = TextUnifyType.Body2,
                weight = TextUnifyWeight.Bold,
                properties = { context ->
                    val color = ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                    )
                    setTextColor(color)
                    text = reason.value
                }
            )

            if (reason.detail.isNotBlank()) {
                TextUnify(
                    modifier = Modifier
                        .padding(16.dp),
                    type = TextUnifyType.Body3,
                    properties = { context ->
                        val color = ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                        )
                        setTextColor(color)
                        text = reason.detail
                        maxLines = 2
                        ellipsize = TextUtils.TruncateAt.END
                    }
                )
            }
        }

        Icon(
            modifier = Modifier
                .width(24.dp)
                .height(24.dp),
            painter = painterResource(id = com.tokopedia.design.R.drawable.ic_arrow_right_grey),
            contentDescription = "icn_button_${reason.strLabel}"
        )
    }
}

@Preview
@Composable
fun ProductReportReasonItem() {
    ProductReportReasonItem(
        reason = ProductReportReason(
            "1",
            children = emptyList(),
            additionalInfo = emptyList(),
            detail = "adsf",
            value = "adf"
        )
    ) {

    }
}