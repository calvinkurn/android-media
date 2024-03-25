package com.tokopedia.review.feature.reading.presentation.widget

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestChips
import com.tokopedia.nest.components.NestChipsLeft
import com.tokopedia.nest.components.NestChipsSize
import com.tokopedia.nest.components.NestChipsState
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.review.feature.reading.data.Option
import com.tokopedia.review.feature.reading.data.VariantData
import kotlinx.parcelize.Parcelize

@Composable
fun SelectVariantFilter(
    uiModel: SelectVariantUiModel,
    uiEvent: SelectVariantUiEvent
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        uiModel.variants.forEachIndexed { index, variant ->
            VariantOptions(
                index = index,
                variant = variant
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            NestButton(
                text = "Terapkan Filter",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                onClick = {
                    uiEvent.onApplyClicked(uiModel.generateFilter())
                }
            )
        }
    }
}

@Composable
fun VariantOptions(
    index: Int,
    variant: SelectVariantUiModel.Variant
) {
    Row(
        modifier = Modifier.padding(
            top = if (index == 0) 0.dp else 24.dp
        )
    ) {
        NestTypography(
            text = variant.name,
            textStyle = NestTheme.typography.display2.copy(
                color = NestTheme.colors.NN._950,
                fontWeight = FontWeight.Bold,
            )
        )
    }

    Row(
        modifier = Modifier.padding(
            top = 8.dp
        )
    ) {
        variant.options.forEachIndexed { index, option ->
            Option(
                index = index,
                option = option
            )
        }
    }

}

@Composable
fun Option(
    index: Int,
    option: SelectVariantUiModel.Option
) {
    var isSelected by remember { mutableStateOf(option.isSelected) }

    val image = option.image
    val left = if (image.isEmpty()) null
    else NestChipsLeft.NetworkImage(image)
    NestChips(
        text = option.name,
        left = left,
        size = NestChipsSize.Medium,
        state = if (isSelected) NestChipsState.Selected else NestChipsState.Default,
        onClick = {
            isSelected = !isSelected
            option.isSelected = isSelected
        },
        modifier = Modifier.padding(
            start = if (index == 0) 0.dp else 8.dp
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SelectVariantFilterPreview() {
    NestTheme {
        val uiModel = SelectVariantUiModel(
            variants = listOf(
                SelectVariantUiModel.Variant(
                    name = "Warna:",
                    options = listOf(
                        SelectVariantUiModel.Option(
                            name = "Putih",
                            image = ""
                        ),
                        SelectVariantUiModel.Option(
                            name = "Hitam",
                            image = ""
                        ),
                        SelectVariantUiModel.Option(
                            name = "Merah",
                            image = ""
                        )
                    ),
                    level = 1
                ),
                SelectVariantUiModel.Variant(
                    name = "Ukuran:",
                    options = listOf(
                        SelectVariantUiModel.Option(
                            name = "S",
                            image = ""
                        ),
                        SelectVariantUiModel.Option(
                            name = "M",
                            image = ""
                        ),
                        SelectVariantUiModel.Option(
                            name = "L",
                            image = ""
                        )
                    ),
                    level = 2
                )
            )
        )
        SelectVariantFilter(uiModel, object : SelectVariantUiEvent {
            override fun onApplyClicked(filter: String) {
            }

        })
    }
}

data class SelectVariantUiModel(
    val variants: List<Variant>
) {
    fun generateFilter(): String {
        return variants.mapNotNull { variant ->
            val selectedOptions = variant.options
                .filter { it.isSelected }.joinToString(",") { it.name }
            if (selectedOptions.isNotEmpty())
                "variant_l${variant.level}=${selectedOptions}"
            else null
        }.joinToString(";")
    }

    @Parcelize
    data class Variant(
        val name: String,
        val options: List<Option>,
        val level: Int
    ) : Parcelable

    @Parcelize
    data class Option(
        val name: String,
        val image: String,
        var isSelected: Boolean = false
    ) : Parcelable
}

interface SelectVariantUiEvent {
    fun onApplyClicked(filter: String)
}

internal fun List<VariantData>.toVariantUiModel(): List<SelectVariantUiModel.Variant> = map {
    SelectVariantUiModel.Variant(
        name = it.name,
        options = it.options.toOptionUiModel(),
        level = it.level
    )
}

internal fun List<Option>.toOptionUiModel(): List<SelectVariantUiModel.Option> = map {
    SelectVariantUiModel.Option(
        name = it.name,
        image = it.image
    )
}
