package com.tokopedia.review.feature.reading.presentation.widget

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestChips
import com.tokopedia.nest.components.NestChipsLeft
import com.tokopedia.nest.components.NestChipsSize
import com.tokopedia.nest.components.NestChipsState
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.review.feature.reading.data.Option
import com.tokopedia.review.feature.reading.data.VariantData

@Composable
fun SelectVariantFilter(
    uiModel: SelectVariantUiModel,
    uiEvent: SelectVariantUiEvent
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            uiModel.variants.forEachIndexed { index, variant ->
                VariantOptions(
                    index = index,
                    variant = variant
                )
            }
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
                    uiEvent.onApplyClicked(uiModel)
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

    FlowRow(
        modifier = Modifier.padding(
            top = 8.dp
        ),
        crossAxisSpacing = 8.dp,
        mainAxisSpacing = 8.dp
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
    val image = option.image
    val left = if (image.isEmpty()) null
    else NestChipsLeft.NetworkImage(image)
    NestChips(
        text = option.name,
        left = left,
        size = NestChipsSize.Medium,
        state = if (option.isSelected) NestChipsState.Selected else NestChipsState.Default,
        onClick = {
            option.isSelected = !option.isSelected
        },
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
                            id = "",
                            name = "Putih",
                            image = ""
                        ),
                        SelectVariantUiModel.Option(
                            id = "",
                            name = "Hitam",
                            image = ""
                        ),
                        SelectVariantUiModel.Option(
                            id = "",
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
                            id = "",
                            name = "S",
                            image = ""
                        ),
                        SelectVariantUiModel.Option(
                            id = "",
                            name = "M",
                            image = ""
                        ),
                        SelectVariantUiModel.Option(
                            id = "",
                            name = "L",
                            image = ""
                        )
                    ),
                    level = 2
                )
            ),
            pairedOptions = emptyList()
        )
        SelectVariantFilter(
            uiModel,
            object : SelectVariantUiEvent {
                override fun onApplyClicked(uiModel: SelectVariantUiModel) {
                }
            }
        )
    }
}

// TODO - cek variant yang text panjang, perlu scoll atau ngga di column paling atas
// nanti coba tambahin modifier scrollable

data class SelectVariantUiModel(
    val variants: List<Variant>,
    val pairedOptions: List<List<String>>
) {

    var count: Int = 0
    var filter: String = ""
    var opt: String = ""

    fun calculate() {
        reset()

        val filter = mutableListOf<String>()
        val pairedOption = mutableListOf<String>()
        var isEachLevelOption = true

        variants.forEach { variant ->
            val selectedOptions = variant.options.filter { it.isSelected }

            count += selectedOptions.size

            if (selectedOptions.isNotEmpty()) {
                val optionNames = selectedOptions.joinToString(",") { it.name }
                filter.add("variant_l${variant.level}=$optionNames")
            }

            if (selectedOptions.size == 1) {
                val option = selectedOptions.first()
                pairedOption.add(option.id)
            }
            isEachLevelOption = isEachLevelOption && selectedOptions.size == 1
        }

        if (isEachLevelOption && variants.size > 1) {
            val isAvailable = pairedOptions.any { it.containsAll(pairedOption) }
            if (!isAvailable) this.opt = "variant_unavailable=true"
        }

        this.filter = filter.joinToString(";")
    }

    private fun reset() {
        count = 0
        filter = ""
        opt = ""
    }

    data class Variant(
        val name: String,
        val options: List<Option>,
        val level: Int
    )

    data class Option(
        val id: String,
        val name: String,
        val image: String,
    ) {
        var isSelected by mutableStateOf(false)
    }
}

interface SelectVariantUiEvent {
    fun onApplyClicked(uiModel: SelectVariantUiModel)
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
        id = it.id,
        name = it.name,
        image = it.image
    )
}
