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
import com.tokopedia.nest.principles.utils.tag

@Composable
fun PersonalizeQuestionScreen(
    listQuestion: List<QuestionDataModel>,
    isLoadingSaveAnswer: Boolean,
    countItemSelected: Int,
    maxItemSelected: Int,
    minItemSelected: Int,
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
                        .tag("skip button")
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
                HeaderSection(
                    countItemSelected = countItemSelected,
                    maxItemSelected = maxItemSelected,
                    minItemSelected = minItemSelected
                )

                NestDivider(
                    modifier = Modifier.fillMaxWidth(),
                    size = NestDividerSize.Small
                )

                ListQuestion(
                    listQuestion = listQuestion,
                    modifier = Modifier.weight(1f),
                    isMaxOptionsSelected = if (maxItemSelected == 0) {
                        false
                    } else {
                        countItemSelected == maxItemSelected
                    },
                    onOptionSelected = { item -> onOptionSelected(item) }
                )

                NestDivider(
                    modifier = Modifier.fillMaxWidth(),
                    size = NestDividerSize.Small
                )

                NestButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .tag("button save"),
                    text = LocalContext.current.getString(R.string.explicit_personalize_save),
                    isEnabled = if (minItemSelected == 0) {
                        true
                    } else {
                        countItemSelected >= minItemSelected
                    },
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
                    name = "Fashion", options = mutableListOf(
                        QuestionDataModel.Property.Options(caption = "Fashion Muslim", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Fashion Wanita", isSelected = false),
                        QuestionDataModel.Property.Options(caption = "Fashion Anak & Bayi", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Fashion Pria", isSelected = true)
                    )
                )),
                QuestionDataModel(questionId = 2, property = QuestionDataModel.Property(
                    name = "Elektronik", options = mutableListOf(
                        QuestionDataModel.Property.Options(caption = "Audio", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Kamera", isSelected = false),
                        QuestionDataModel.Property.Options(caption = "Laptop & Komputer", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Elektronik", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Handphone & Tablet", isSelected = true)
                    )
                )),
                QuestionDataModel(questionId = 3, property = QuestionDataModel.Property(
                    name = "Kebutuhan Rumah", options = mutableListOf(
                        QuestionDataModel.Property.Options(caption = "Rumah Tangga", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Office & Stationery", isSelected = false),
                        QuestionDataModel.Property.Options(caption = "Makanan & Minuman", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Perlengkapan Pesta", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Ibu & Bayi", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Pertukangan", isSelected = true)
                    )
                )),
                QuestionDataModel(questionId = 4, property = QuestionDataModel.Property(
                    name = "Kesehatan & Kecantikan", options = mutableListOf(
                        QuestionDataModel.Property.Options(caption = "Perawatan Tubuh", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Kesehatan", isSelected = false),
                        QuestionDataModel.Property.Options(caption = "Kecantikan", isSelected = false),
                    )
                )),
                QuestionDataModel(questionId = 5, property = QuestionDataModel.Property(
                    name = "Hobi", options = mutableListOf(
                        QuestionDataModel.Property.Options(caption = "Buku", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Gaming", isSelected = false),
                        QuestionDataModel.Property.Options(caption = "Film & Musik", isSelected = false),
                        QuestionDataModel.Property.Options(caption = "Olahraga", isSelected = false),
                        QuestionDataModel.Property.Options(caption = "Otomotif", isSelected = false),
                        QuestionDataModel.Property.Options(caption = "Mainan & Hobi", isSelected = false),
                    )
                )),
                QuestionDataModel(questionId = 6, property = QuestionDataModel.Property(
                    name = "Lainnya", options = mutableListOf(
                        QuestionDataModel.Property.Options(caption = "Tiket, Travel, Voucher", isSelected = true),
                        QuestionDataModel.Property.Options(caption = "Logam Mulia", isSelected = false),
                        QuestionDataModel.Property.Options(caption = "Perawatan Hewan", isSelected = false)
                    )
                ))
            ),
            countItemSelected =  10,
            maxItemSelected = 10,
            onSave = {},
            onSkip = {},
            onOptionSelected = {},
            isLoadingSaveAnswer = true,
            minItemSelected = 1
        )
    }
}
