package com.tokopedia.product.detail.common.mapper.usecase

import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.mapper.AtcVariantNewMapper

/**
 * Created by yovi.putra on 08/03/23"
 * Project name: android-tokopedia-core
 **/

object VariantTwoLevelUseCase {

    fun process(
        variantData: ProductVariant,
        selectedVariant: Map<String, String>,
        selectedLevel: Int
    ): List<VariantCategory>? = when (selectedLevel) {
        AtcVariantNewMapper.VARIANT_LEVEL_ONE_SELECTED -> VariantTwoLevelByOneLevelSelectedUseCase.process(
            variantData = variantData,
            mapOfSelectedVariant = selectedVariant
        )
        AtcVariantNewMapper.VARIANT_LEVEL_TWO_SELECTED -> VariantTwoLevelByTwoLevelSelectedUseCase.process(
            variantData = variantData,
            mapOfSelectedVariant = selectedVariant
        )
        else -> null
    }
}
