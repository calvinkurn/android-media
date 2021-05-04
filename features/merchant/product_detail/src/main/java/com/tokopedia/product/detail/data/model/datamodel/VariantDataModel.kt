package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.variant_common.model.VariantCategory

/**
 * Created by Yehezkiel on 2020-02-26
 */

data class VariantDataModel(
        val type: String = "",
        val name: String = "",
        var listOfVariantCategory: List<VariantCategory>? = null,
        var mapOfSelectedVariant: MutableMap<String, String> = mutableMapOf(),
        var isVariantError: Boolean = false,
        var isRefreshing: Boolean = false
) : DynamicPdpDataModel {

    fun isPartialySelected(): Boolean = mapOfSelectedVariant.any {
        it.value.toLongOrZero() == 0L
    } || mapOfSelectedVariant.isEmpty()

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override val impressHolder = ImpressHolder()

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is VariantDataModel) {
            isVariantError == newData.isVariantError
                    && listOfVariantCategory == newData.listOfVariantCategory
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        return if (newData is VariantDataModel && isSelectedVariantChanged(newData)) {
            bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_VARIANT_COMPONENT)
            bundle
        } else {
            null
        }
    }

    private fun isSelectedVariantChanged(newVariantDataModel: VariantDataModel): Boolean {
        var isChanged = false
        for ((key, value) in newVariantDataModel.mapOfSelectedVariant) {
            val currentValue = mapOfSelectedVariant[key]

            if (currentValue != value) {
                isChanged = true
                break
            }
        }
        return isChanged
    }
}