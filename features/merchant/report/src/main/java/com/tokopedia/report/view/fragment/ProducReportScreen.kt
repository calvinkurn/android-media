package com.tokopedia.report.view.fragment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tokopedia.report.R
import com.tokopedia.report.view.fragment.components.ProductReportComposeContent
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.fragment.models.ProductReportUiState
import com.tokopedia.report.view.fragment.unify_components.AppBar

/**
 * Created by yovi.putra on 05/10/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportScreen(
    uiState: ProductReportUiState,
    onEvent: (ProductReportUiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            AppBar(
                modifier = Modifier.height(48.dp),
                title = stringResource(id = R.string.product_report),
                navigationClick = {
                    onEvent(ProductReportUiEvent.OnBackPressed)
                }
            )
        }
    ) {
        ProductReportComposeContent(
            modifier = Modifier.padding(it),
            uiState = uiState,
            onEvent = onEvent
        )
    }
}
