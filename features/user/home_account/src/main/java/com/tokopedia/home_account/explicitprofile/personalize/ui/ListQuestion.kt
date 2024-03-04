package com.tokopedia.home_account.explicitprofile.personalize.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun ListQuestion(
    listQuestion: List<QuestionDataModel>,
    isMaxOptionsSelected: Boolean,
    onOptionSelected: (OptionSelected) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(
            items = listQuestion,
            key = { _, item ->
                item.questionId
            }
        ) { index, item ->
            QuestionSection(
                urlImage = item.property.image,
                sectionTitle = item.property.name,
                isMaxOptionsSelected = isMaxOptionsSelected,
                listOptions = item.property.options,
                onOptionSelected = {
                    onOptionSelected(it)
                },
                indexCategory = index,
                questionId = item.questionId
            )

        }
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun ListQuestionPreview() {
    NestTheme {
        ListQuestion(listQuestion = listOf(
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
        ), isMaxOptionsSelected = false, onOptionSelected = {})
    }
}
