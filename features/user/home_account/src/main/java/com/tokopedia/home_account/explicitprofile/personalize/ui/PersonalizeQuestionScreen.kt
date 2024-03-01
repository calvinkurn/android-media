package com.tokopedia.home_account.explicitprofile.personalize.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.home_account.R
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun PersonalizeQuestionScreen(
    listQuestion: List<QuestionDataModel>,
    isLoadingSaveAnswer: Boolean,
    countItemSelected: Int,
    maxItemSelected: Int,
    onSave: () -> Unit,
    onSkip: () -> Unit,
    onOptionSelected: (OptionSelected) -> Unit,
) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
                NestTypography(
                    text = LocalContext.current.getString(R.string.explicit_personalize_skip),
                    textStyle = NestTheme.typography.paragraph2.copy(
                        color = NestTheme.colors.GN._500,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .align(Alignment.End)
                        .clickable { onSkip.invoke() }
                )

                NestDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    size = NestDividerSize.Small
                )
            }
        },
        content = {
            Column(
                modifier = Modifier.padding(it)
            ) {
                HeaderSection(countItemSelected = countItemSelected, maxItemSelected = maxItemSelected)

                NestDivider(
                    modifier = Modifier.fillMaxWidth(),
                    size = NestDividerSize.Small
                )

                ListQuestion(
                    listQuestion = listQuestion,
                    modifier = Modifier.weight(1f),
                    isMaxOptionsSelected = countItemSelected == maxItemSelected,
                    onOptionSelected = { item -> onOptionSelected(item) }
                )

                NestDivider(
                    modifier = Modifier.fillMaxWidth(),
                    size = NestDividerSize.Small
                )

                NestButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    text = LocalContext.current.getString(R.string.explicit_personalize_save),
                    isEnabled = countItemSelected.isMoreThanZero(),
                    onClick = { onSave.invoke() },
                    isLoading = isLoadingSaveAnswer
                )
            }
        }
    )
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun PersonalizeScreenPreview() {
    NestTheme {
        PersonalizeQuestionScreen(
            listQuestion = listOf(
                QuestionDataModel(questionId = 1, property = QuestionDataModel.Property(
                    title = "Fashion", options = mutableListOf(
                        QuestionDataModel.Property.Options(value = "Fashion Muslim", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Fashion Wanita", isSelected = false),
                        QuestionDataModel.Property.Options(value = "Fashion Anak & Bayi", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Fashion Pria", isSelected = true)
                    )
                )),
                QuestionDataModel(questionId = 2, property = QuestionDataModel.Property(
                    title = "Elektronik", options = mutableListOf(
                        QuestionDataModel.Property.Options(value = "Audio", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Kamera", isSelected = false),
                        QuestionDataModel.Property.Options(value = "Laptop & Komputer", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Elektronik", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Handphone & Tablet", isSelected = true)
                    )
                )),
                QuestionDataModel(questionId = 3, property = QuestionDataModel.Property(
                    title = "Kebutuhan Rumah", options = mutableListOf(
                        QuestionDataModel.Property.Options(value = "Rumah Tangga", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Office & Stationery", isSelected = false),
                        QuestionDataModel.Property.Options(value = "Makanan & Minuman", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Perlengkapan Pesta", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Ibu & Bayi", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Pertukangan", isSelected = true)
                    )
                )),
                QuestionDataModel(questionId = 4, property = QuestionDataModel.Property(
                    title = "Kesehatan & Kecantikan", options = mutableListOf(
                        QuestionDataModel.Property.Options(value = "Perawatan Tubuh", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Kesehatan", isSelected = false),
                        QuestionDataModel.Property.Options(value = "Kecantikan", isSelected = false),
                    )
                )),
                QuestionDataModel(questionId = 5, property = QuestionDataModel.Property(
                    title = "Hobi", options = mutableListOf(
                        QuestionDataModel.Property.Options(value = "Buku", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Gaming", isSelected = false),
                        QuestionDataModel.Property.Options(value = "Film & Musik", isSelected = false),
                        QuestionDataModel.Property.Options(value = "Olahraga", isSelected = false),
                        QuestionDataModel.Property.Options(value = "Otomotif", isSelected = false),
                        QuestionDataModel.Property.Options(value = "Mainan & Hobi", isSelected = false),
                    )
                )),
                QuestionDataModel(questionId = 6, property = QuestionDataModel.Property(
                    title = "Lainnya", options = mutableListOf(
                        QuestionDataModel.Property.Options(value = "Tiket, Travel, Voucher", isSelected = true),
                        QuestionDataModel.Property.Options(value = "Logam Mulia", isSelected = false),
                        QuestionDataModel.Property.Options(value = "Perawatan Hewan", isSelected = false)
                    )
                ))
            ),
            countItemSelected =  10,
            maxItemSelected = 10,
            onSave = {},
            onSkip = {},
            onOptionSelected = {},
            isLoadingSaveAnswer = true
        )
    }
}
