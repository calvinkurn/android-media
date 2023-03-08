package com.tokopedia.product.detail.common.mapper

import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.mapper.usecase.VariantOneLevelUseCase
import com.tokopedia.product.detail.common.mapper.usecase.VariantTwoLevelByOneLevelSelectedUseCase
import com.tokopedia.product.detail.common.mapper.usecase.VariantTwoLevelByTwoLevelSelectedUseCase

/**
 * Created by yovi.putra on 08/03/23"
 * Project name: android-tokopedia-core
 **/

object AtcVariantNewMapper {
    private const val VARIANT_HAVE_ONE_LEVEL = 1
    private const val VARIANT_HAVE_TWO_LEVEL = 2

    const val VARIANT_LEVEL_ONE_SELECTED = 0
    const val VARIANT_LEVEL_TWO_SELECTED = 1

    fun processVariantNew(
        variantData: ProductVariant?,
        selectedVariant: MutableMap<String, String>?,
        level: Int
    ): List<VariantCategory>? {
        val variants = variantData ?: return null
        if (variants.variants.isEmpty()) return null
        val variantSize = variants.variants.size
        // for new logic, initialize value by default is level one selected
        val selectedLevel = if (level == AtcVariantMapper.VARIANT_LEVEL_INITIALIZE) {
            VARIANT_LEVEL_ONE_SELECTED
        } else {
            level
        }

        return when (variantSize) {
            VARIANT_HAVE_ONE_LEVEL -> {
                VariantOneLevelUseCase.byLevelOneSelected(
                    variantData = variantData,
                    selectedVariant = selectedVariant.orEmpty()
                )
            }
            VARIANT_HAVE_TWO_LEVEL -> {
                processVariantTwoLevel(
                    variantData = variantData,
                    selectedVariant = selectedVariant.orEmpty(),
                    selectedLevel = selectedLevel
                )
            }
            else -> null
        }
    }

    private fun processVariantTwoLevel(
        variantData: ProductVariant,
        selectedVariant: Map<String, String>,
        selectedLevel: Int
    ): List<VariantCategory>? = when (selectedLevel) {
        VARIANT_LEVEL_ONE_SELECTED -> VariantTwoLevelByOneLevelSelectedUseCase.process(
            variantData = variantData,
            mapOfSelectedVariant = selectedVariant
        )
        VARIANT_LEVEL_TWO_SELECTED -> VariantTwoLevelByTwoLevelSelectedUseCase.process(
            variantData = variantData,
            mapOfSelectedVariant = selectedVariant
        )
        else -> null
    }
}
