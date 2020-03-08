package com.tokopedia.variant_common.model

import com.tokopedia.variant_common.constant.VariantConstant

/**
 * Created by Yehezkiel on 08/03/20
 */
data class VariantCategory(
        var name: String = "",
        var identifier: String = "",
        var variantGuideline: String = "",
        var hasCustomImage: Boolean = false,
        var selectedValue: String = "",
        var isLeaf: Boolean = false,
        var variantOptions: MutableList<VariantOptionWithAttribute> = arrayListOf()
) {
    fun getSelectedOption(): VariantOptionWithAttribute? {
        return variantOptions.find { it.currentState == VariantConstant.STATE_SELECTED }
    }

    fun getPositionSelectedOption(): Int {
        return variantOptions.indexOf(getSelectedOption())
    }
}