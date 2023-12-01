package com.tokopedia.product.detail.unified

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.compose.NestRadioButton
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by astidhiyaa on 28/11/23
 */
@Composable
fun ReportScreen(reports: List<ReportUiModel>, onSubmit: (ReportUiModel) -> Unit = {}) {
    var text by remember { mutableStateOf("") }
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(ReportUiModel.Empty) }
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        NestTypography(
            text = "Bantu kami memahami apa yang terjadi. Mengapa Anda melaporkan ulasan ini?",
            textStyle = NestTheme.typography.paragraph2.copy(
                color = colorResource(id = unifyprinciplesR.color.Unify_NN950),
            )
        )
        LazyColumn(modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)) {
            items(reports) { item ->
                NestRadioButton(
                    text = item.text,
                    selected = item.text == selectedOption.text,
                    onSelected = {
                        onOptionSelected(item)
                    }
                )
            }
        }
        val isLastSelected = selectedOption == reports.last()

        //enable when option 3 is clicked
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(text = "Alasan") },
            enabled = isLastSelected,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 22.dp)
        )
        // enable when report is not empty
        NestButton(
            text = "Kirim", onClick = { onSubmit(selectedOption) },
            isEnabled = selectedOption != ReportUiModel.Empty,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun MyApp() {
    ReportScreen(
        reports = listOfReport
    )
}

internal val listOfReport: List<ReportUiModel>
    get() = buildList {
        add(ReportUiModel(text = "Ini adalah konten spam", isSelected = false, reasonCode = 1))
        add(
            ReportUiModel(
                text = "Konten mengandung SARA, diskriminasi, vulgar, ancaman, dan pelanggaran nilai/norma sosial",
                isSelected = false,
                reasonCode = 2,
            )
        )
        add(ReportUiModel(text = "Lainnya", isSelected = false, reasonCode = 3))
    }
