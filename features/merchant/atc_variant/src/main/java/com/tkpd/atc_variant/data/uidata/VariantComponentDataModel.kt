package com.tkpd.atc_variant.data.uidata

import android.os.Bundle
import com.tkpd.atc_variant.views.adapter.AtcVariantTypeFactory
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory

/**
 * Created by Yehezkiel on 06/05/21
 */
data class VariantComponentDataModel(
        val position: Long = 0,
        var listOfVariantCategory: List<VariantCategory>? = null,
        var mapOfSelectedVariant: MutableMap<String, String> = mutableMapOf()
) : AtcVariantVisitable {

    fun isPartialySelected(): Boolean = mapOfSelectedVariant.any {
        it.value.toLongOrZero() == 0L
    } || mapOfSelectedVariant.isEmpty()

    override fun uniqueId(): Long = position

    override fun isEqual(newData: AtcVariantVisitable): Boolean {
        return if (newData is VariantComponentDataModel) {
            mapOfSelectedVariant == newData.mapOfSelectedVariant &&
                    listOfVariantCategory == newData.listOfVariantCategory
        } else {
            false
        }
    }

    override fun getChangePayload(newData: AtcVariantVisitable): Bundle? {
        val bundle = Bundle()
        return if (newData is VariantComponentDataModel && isSelectedVariantChanged(newData)) {
            bundle.putInt("payload", 1)
            bundle
        } else {
            null
        }
    }

    private fun isSelectedVariantChanged(newVariantDataModel: VariantComponentDataModel): Boolean {
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

    override fun type(typeFactory: AtcVariantTypeFactory): Int {
        return typeFactory.type(this)
    }
}