package com.tokopedia.product.detail.data.model.variant

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.variant_common.model.VariantCategory

/**
 * Created by Yehezkiel on 2020-02-26
 */

data class VariantDataModel(
        val type: String = "",
        val name: String = "",
        var listOfVariantCategory: List<VariantCategory>? = null,
        var mapOfSelectedVariant: MutableMap<String, Int> = mutableMapOf(),
        var isVariantError: Boolean = false,
        var isRefreshing: Boolean = false
) : DynamicPdpDataModel {

    fun isPartialySelected(): Boolean = mapOfSelectedVariant.any {
        it.value == 0
    } || mapOfSelectedVariant.isEmpty()

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override val impressHolder = ImpressHolder()
}