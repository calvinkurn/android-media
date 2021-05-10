package com.tkpd.atc_variant.data.uidata

import android.os.Bundle
import com.tkpd.atc_variant.views.adapter.AtcVariantTypeFactory
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable

/**
 * Created by Yehezkiel on 06/05/21
 */
data class VariantStockDataModel(
        val position: Long = 0,
        val stockWording: String = ""
) : AtcVariantVisitable {
    override fun uniqueId(): Long = position
    override fun isEqual(newData: AtcVariantVisitable): Boolean {
        return if (newData is VariantStockDataModel) {
            stockWording == newData.stockWording
        } else {
            false
        }
    }

    override fun getChangePayload(newData: AtcVariantVisitable): Bundle? {
        return null
    }

    override fun type(typeFactory: AtcVariantTypeFactory): Int {
        return typeFactory.type(this)
    }

}