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

    /***
     * process variant
     * @param variantData
     * @param selectedVariant is what will store the user-selected variant base on level
     *      selectedVariant value will like this
     *         index 0 : Key[productVariantID lvl1], value[variantID lvl1]
     *         index 1: Key[productVariantID  lvl2]value[variantID lvl2]
     *      if variant have one level so have index 0 only
     * @return
     */
    fun processVariant(
        variantData: ProductVariant?,
        selectedVariant: Map<String, String>
    ): List<VariantCategory>? {
        val variants = variantData ?: return null
        if (variants.variants.isEmpty()) return null

        return when (variants.variants.size) {
            VARIANT_HAVE_ONE_LEVEL -> {
                VariantOneLevelUseCase.process(
                    variantData = variantData,
                    mapOfSelectedVariant = selectedVariant
                )
            }
            VARIANT_HAVE_TWO_LEVEL -> {
                VariantTwoLevelUseCase.process(
                    variantData = variantData,
                    mapOfSelectedVariant = selectedVariant
                )
            }
            else -> null
        }
    }
}
