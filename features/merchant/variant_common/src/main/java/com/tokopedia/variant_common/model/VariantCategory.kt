package com.tokopedia.variant_common.model

import com.tokopedia.variant_common.constant.VariantConstant

/**
 * Created by Yehezkiel on 08/03/20
 */
data class VariantCategory(
        val name: String = "",
        val identifier: String = "",
        val variantGuideline: String = "",
        val hasCustomImage: Boolean = false,
        val selectedValue: String = "",
        val isLeaf: Boolean = false,
        val variantOptions: List<VariantOptionWithAttribute> = emptyList()
) {
    fun getSelectedOption(): VariantOptionWithAttribute? {
        return variantOptions.find { it.currentState == VariantConstant.STATE_SELECTED }
    }
}