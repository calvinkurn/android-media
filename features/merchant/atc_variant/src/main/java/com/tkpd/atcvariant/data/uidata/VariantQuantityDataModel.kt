package com.tkpd.atcvariant.data.uidata

import android.os.Bundle
import com.tkpd.atcvariant.view.adapter.AtcVariantTypeFactory
import com.tkpd.atcvariant.view.adapter.AtcVariantVisitable

/**
 * Created by Yehezkiel on 11/05/21
 */
data class VariantQuantityDataModel(
        val position: Long = 0L,
        val productId: String = "",
        var quantity: Int = 0,
        var minOrder: Int = 0,
        var maxOrder: Int = 0,
        var shouldShowView: Boolean = false
) : AtcVariantVisitable {
    override fun uniqueId(): Long = position

    override fun isEqual(newData: AtcVariantVisitable): Boolean {
        return if (newData is VariantQuantityDataModel) {
            minOrder == newData.minOrder &&
                    shouldShowView == newData.shouldShowView &&
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