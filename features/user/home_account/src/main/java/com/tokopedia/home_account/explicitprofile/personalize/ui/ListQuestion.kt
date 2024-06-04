package com.tokopedia.home_account.explicitprofile.personalize.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.tag

@Composable
fun ListQuestion(
    listQuestion: List<QuestionDataModel>,
    isMaxOptionsSelected: Boolean,
    onOptionSelected: (OptionSelected) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.tag("question section")) {
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
        ), isMaxOptionsSelected = false, onOptionSelected = {})
    }
}
