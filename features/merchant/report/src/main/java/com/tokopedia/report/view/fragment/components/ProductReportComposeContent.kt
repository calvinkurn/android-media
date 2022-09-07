package com.tokopedia.report.view.fragment.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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