package com.tokopedia.report.view.fragment.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.fragment.models.ProductReportUiState
import com.tokopedia.report.view.fragment.unify_components.getString
import timber.log.Timber

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportComposeContent(
    uiState: ProductReportUiState,
    onEvent: (ProductReportUiEvent) -> Unit
) {
    if (uiState.error.isNullOrBlank().not()) {
        Text(
            text = "error: ${uiState.error.orEmpty()}",
            color = MaterialTheme.colors.error
        )
    }

    LazyColumn {
        item {
            ProductReportReasonHeader(
                text = uiState.title.getString().orEmpty()
            )
        }

        items(
            items = uiState.data,
            key = { it.value }
        ) { item ->
            Timber.d(item.value)
            ProductReportReasonItem(
                reason = item,
                subtitleVisible = uiState.isSubtitleVisible(reason = item),
                onClick = {
                    onEvent.invoke(ProductReportUiEvent.OnItemClicked(it))
                }
            )
        }

        item {
            ProductReportReasonFooter(
                text = stringResource(id = R.string.product_report_see_all_types),
                onClick = {
                    onEvent.invoke(ProductReportUiEvent.OnFooterClicked(it))
                }
            )
        }
    }
}

@Preview
@Composable
fun ProductReportComposeContentPreview() {
    ProductReportComposeContent(
        uiState = ProductReportUiState(
            data = (0..10).map {
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

@Preview
@Composable
fun ProductReportComposeContentErrorPreview() {
    ProductReportComposeContent(
        uiState = ProductReportUiState(error = "error gan")
    ) {
    }
}