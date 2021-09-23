package com.tkpd.atcvariant.data.uidata

import android.os.Bundle
import com.tkpd.atcvariant.view.adapter.AtcVariantTypeFactory
import com.tkpd.atcvariant.view.adapter.AtcVariantVisitable
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory

/**
 * Created by Yehezkiel on 06/05/21
 */
data class VariantComponentDataModel(
        val position: Long = 0,
        var listOfVariantCategory: List<VariantCategory>? = null,
        var mapOfSelectedVariant: MutableMap<String, String> = mutableMapOf(),
        var emptyOrInactiveCopy: String = ""
) : AtcVariantVisitable {

    override fun uniqueId(): Long = position

    override fun isEqual(newData: AtcVariantVisitable): Boolean {
        return if (newData is VariantComponentDataModel) {
            mapOfSelectedVariant == newData.mapOfSelectedVariant &&
                    listOfVariantCategory == newData.listOfVariantCategory &&
                    emptyOrInactiveCopy == newData.emptyOrInactiveCopy
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