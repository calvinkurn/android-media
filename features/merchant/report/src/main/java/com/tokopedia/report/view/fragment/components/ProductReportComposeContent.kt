package com.tokopedia.report.view.fragment.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.nest.principles.utils.getString
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.fragment.models.ProductReportUiState

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportComposeContent(
    modifier: Modifier = Modifier,
    uiState: ProductReportUiState,
    onEvent: (ProductReportUiEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier.tag("product_report_column")
    ) {
        item {
            ProductReportReasonHeader(
                text = uiState.title.getString()
            )
        }

        items(
            items = uiState.data,
            key = { it.value }
        ) { item ->
            ProductReportReasonItem(
                modifier = Modifier.tag("product_report_item:${item.value}"),
                reason = item,
                subtitleVisible = uiState.isSubtitleVisible(reason = item),
                onClick = {
                    onEvent(ProductReportUiEvent.OnItemClicked(it))
                }
            )
        }

        item {
            ProductReportReasonFooter(
                text = stringResource(id = R.string.product_report_see_all_types),
                onClick = {
                    onEvent(ProductReportUiEvent.OnFooterClicked(it))
                }
            )
        }
    }
}

@Preview
@Composable
fun ProductReportComposeContentPreview() {
    val iterateItem = 10
    ProductReportComposeContent(
        uiState = ProductReportUiState(
            data = (0..iterateItem).map {
                ProductReportReason(
                    categoryId = it.toString(),
                    children = emptyList(),
                    additionalFields = emptyList(),
                    additionalInfo = emptyList(),
                    detail = "detail",
                    value = "$it value"
                )
            }
        )
    ) {
    }
}

@Preview
@Composable
fun ProductReportComposeContentLoadingPreview() {
    ProductReportComposeContent(
        uiState = ProductReportUiState()
    ) {
    }
}
