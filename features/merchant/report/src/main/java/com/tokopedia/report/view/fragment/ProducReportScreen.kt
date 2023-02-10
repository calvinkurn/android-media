package com.tokopedia.report.view.fragment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.components.ProductReportComposeContent
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.fragment.unify_components.AppBar
import com.tokopedia.report.view.viewmodel.ProductReportViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Created by yovi.putra on 05/10/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportScreen(
    viewModel: ProductReportViewModel,
    onFooterClicked: () -> Unit,
    onScrollTop: (ProductReportReason) -> Unit,
    onGoToForm: (ProductReportReason) -> Unit,
    onFinish: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            AppBar(
                modifier = Modifier.height(48.dp),
                title = stringResource(id = R.string.product_report),
                navigationClick = {
                    viewModel.onEvent(ProductReportUiEvent.OnBackPressed)
                }
            )
        }
    ) {
        ProductReportComposeContent(
            uiState = uiState.value,
            onEvent = viewModel::onEvent
        )
    }

    LaunchedEffect(key1 = viewModel.uiEvent, block = {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is ProductReportUiEvent.OnFooterClicked -> onFooterClicked()
                is ProductReportUiEvent.OnScrollTop -> onScrollTop(it.reason)
                is ProductReportUiEvent.OnGoToForm -> onGoToForm(it.reason)
                is ProductReportUiEvent.OnBackPressed -> {
                    onFinish()
                }
                else -> {
                }
            }
        }
    })
}
