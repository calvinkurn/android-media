package com.tokopedia.home_account.explicitprofile.personalize.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.nest.components.NestChips
import com.tokopedia.nest.components.NestChipsSize
import com.tokopedia.nest.components.NestChipsState
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun QuestionSection(
    indexCategory: Int,
    urlImage: String,
    sectionTitle: String,
    isMaxOptionsSelected: Boolean,
    questionId: Int,
    listOptions: List<QuestionDataModel.Property.Options>,
    onOptionSelected: (OptionSelected) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NestImage(
                source = ImageSource.Remote(source = urlImage),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            NestTypography(
                text = sectionTitle,
                textStyle = NestTheme.typography.display1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            listOptions.forEachIndexed { indexOption, item ->
                NestChips(
                    text = item.caption,
                    size = NestChipsSize.Large,
                    state = if (item.isSelected) {
                        NestChipsState.Selected
                    } else if (isMaxOptionsSelected) {
                        NestChipsState.Disabled
                    } else {
                        NestChipsState.Default
                    },
                    onClick = {
                        if (!isMaxOptionsSelected or item.isSelected) {
                            onOptionSelected(
                                OptionSelected(
                                    indexOption = indexOption,
                                    indexCategory = indexCategory,
                                    isSelected = item.isSelected,
                                    name = item.value,
                                    questionId = questionId
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}


data class OptionSelected(
    val indexOption: Int,
    val indexCategory: Int,
    val isSelected: Boolean,
    val name: String,
    val questionId: Int
)

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun QuestionSectionPreview() {
    NestTheme {
        QuestionSection(
            urlImage = "",
            sectionTitle = "Fashion",
            isMaxOptionsSelected = false,
            listOptions = listOf(
                QuestionDataModel.Property.Options(caption = "Fashion Muslim", isSelected = true),
                QuestionDataModel.Property.Options(caption = "Fashion Wanita", isSelected = false),
                QuestionDataModel.Property.Options(caption = "Fashion Anak & Bayi", isSelected = true),
                QuestionDataModel.Property.Options(caption = "Fashion Pria", isSelected = true)
            ),
            onOptionSelected = {},
            indexCategory = 0,
            questionId = 0
        )
    }
}
