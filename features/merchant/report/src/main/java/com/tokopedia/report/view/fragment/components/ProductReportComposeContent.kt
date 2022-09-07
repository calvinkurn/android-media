package com.tokopedia.report.view.fragment.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.models.ProductReportUiState

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportComposeContent(
    uiState: ProductReportUiState
) {
    if (uiState.isLoading) {
        Text(
            text = "Loading...",
        )
    }

    if (uiState.error.isNullOrBlank().not()) {
        Text(
            text = "error: ${uiState.error.orEmpty()}",
            color = MaterialTheme.colors.error
        )
    }

    if (uiState.data.isNotEmpty()) {
        val data = uiState.data
        LazyColumn {
            items(data) { item ->
                Text(text = item.strLabel)
            }
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
    )
}

@Preview
@Composable
fun ProductReportComposeContentLoadingPreview() {
    ProductReportComposeContent(
        uiState = ProductReportUiState(isLoading = true)
    )
}

@Preview
@Composable
fun ProductReportComposeContentErrorPreview() {
    ProductReportComposeContent(
        uiState = ProductReportUiState(error = "error gan")
    )
}