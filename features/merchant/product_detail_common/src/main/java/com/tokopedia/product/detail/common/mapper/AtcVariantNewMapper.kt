package com.tokopedia.product.detail.common.mapper

import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.mapper.usecase.VariantOneLevelUseCase
import com.tokopedia.product.detail.common.mapper.usecase.VariantTwoLevelUseCase

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
                VariantOneLevelUseCase.process(
                    variantData = variantData,
                    selectedVariant = selectedVariant.orEmpty()
                )
            }
            VARIANT_HAVE_TWO_LEVEL -> {
                VariantTwoLevelUseCase.process(
                    variantData = variantData,
                    selectedVariant = selectedVariant.orEmpty(),
                    selectedLevel = selectedLevel
                )
            }
            else -> null
        }
    }
}
