package com.tokopedia.content.product.preview.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewReportUiModel
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.compose.NestRadioButton

/**
 * @author by astidhiyaa on 28/11/23
 */
@Composable
internal fun ReportScreen(reports: List<ReviewReportUiModel>, onSubmit: (ReviewReportUiModel) -> Unit = {}) {
    var reason by remember { mutableStateOf("") }
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(ReviewReportUiModel.Empty) }
    NestTheme(isOverrideStatusBarColor = false) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            NestTypography(
                text = stringResource(R.string.review_report_header),
                textStyle = NestTheme.typography.paragraph2.copy(
                    color = NestTheme.colors.NN._950
                )
            )
            LazyColumn(modifier = Modifier.padding(vertical = 12.dp)) {
                items(reports) { item ->
                    NestRadioButton(
                        modifier = Modifier.testTag(stringResource(id =R.string.product_prev_test_tag_option_report)),
                        text = item.text,
                        selected = item.reasonCode == selectedOption.reasonCode,
                        onSelected = { isChecked ->
                            if (isChecked) onOptionSelected(item)
                        }
                    )
                }
            }
            val isOtherReason = selectedOption.reasonCode == OTHER_REASON

            // enable when option 3 is clicked
            TextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text(text = stringResource(R.string.review_report_reason)) },
                enabled = isOtherReason,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 22.dp)
            )
            NestButton(
                text = stringResource(R.string.review_report_send),
                onClick = {
                    onSubmit(selectedOption.copy(text = if (isOtherReason) reason else selectedOption.text))
                },
                isEnabled = selectedOption != ReviewReportUiModel.Empty,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(stringResource(id = R.string.product_prev_test_tag_submit_report))
            )
        }
    }
}

@Preview
@Composable
internal fun ReportScreenPreview() {
    ReportScreen(
        reports = emptyList()
    )
}

private const val OTHER_REASON = 3 // Reason code for other reason is 3.
