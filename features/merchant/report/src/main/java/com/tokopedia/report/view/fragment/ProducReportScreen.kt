package com.tokopedia.report.view.fragment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.report.R
import com.tokopedia.report.view.fragment.components.ProductReportComposeContent
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.fragment.unify_components.AppBar
import com.tokopedia.report.view.viewmodel.ProductReportViewModel

/**
 * Created by yovi.putra on 05/10/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportScreen(
    viewModel: ProductReportViewModel,
) {
    val uiState = viewModel.uiState.collectAsState()

    NestTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                AppBar(title = stringResource(id = R.string.product_report)) {
                    viewModel.onEvent(ProductReportUiEvent.OnBackPressed)
                }
            }
        ) {
            ProductReportComposeContent(
                uiState = uiState.value,
                onEvent = viewModel::onEvent
            )
        }
    }
}
