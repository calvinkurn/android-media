package com.tokopedia.product.detail.data.model.variant

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 2020-02-26
 */

data class VariantDataModel(
        val type: String = "",
        val name: String = "",
        var listOfVariantCategory: List<VariantCategory>? = null,
        var mapOfSelectedVariant: MutableMap<String, Int> = mutableMapOf()
) : DynamicPdpDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override val impressHolder = ImpressHolder()
}

data class VariantCategory(
        var name: String = "",
        var identifier: String = "",
        var variantGuideline: String = "",
        var hasCustomImage: Boolean = false,
        var selectedValue: String = "",
        var variantOptions: MutableList<VariantOptionWithAttribute> = arrayListOf()
) {
    fun getSelectedOption(): Int? {
        return variantOptions.find { it.currentState == ProductDetailConstant.STATE_SELECTED }?.variantId
    }
}