package com.tkpd.atc_variant.data.uidata

import android.os.Bundle
import com.tkpd.atc_variant.views.adapter.AtcVariantTypeFactory
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable

/**
 * Created by Yehezkiel on 11/05/21
 */
data class VariantQuantityDataModel(
        val position: Long = 0L,
        val productId: String = "",
        val quantity: Int = 0
) : AtcVariantVisitable {
    override fun uniqueId(): Long = position

    override fun isEqual(newData: AtcVariantVisitable): Boolean {
        return if (newData is VariantQuantityDataModel) {
            productId == newData.productId &&
                    quantity == newData.quantity
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