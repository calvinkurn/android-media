package com.tokopedia.report.view.fragment.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.principles.getString
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.models.ProductReportUiEvent
import com.tokopedia.report.view.fragment.models.ProductReportUiState
import com.tokopedia.report.view.fragment.unify_components.tag

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Composable
fun ProductReportComposeContent(
    uiState: ProductReportUiState,
    onEvent: (ProductReportUiEvent) -> Unit
) {
    Column {
        if (uiState.error.isNullOrBlank().not()) {
            NestTypography(
                text = "error: ${uiState.error.orEmpty()}",
                textStyle = NestTheme.typography.body3.copy(
                    color = colorResource(id = com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                )
            )
        } else {
            LazyColumn(
                modifier = Modifier.tag("product_report_column")
            ) {
                item {
                    ProductReportReasonHeader(
                        text = uiState.title.getString().orEmpty()
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