package com.tkpd.atcvariant.data.uidata

import android.os.Bundle
import com.tkpd.atcvariant.util.DEFAULT_ATC_MAX_ORDER
import com.tkpd.atcvariant.util.PAYLOAD_UPDATE_PRODUCT_ID_ONLY
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
        var maxOrder: Int = DEFAULT_ATC_MAX_ORDER,
        var shouldShowDeleteButton: Boolean = false,
        var shouldShowView: Boolean = false
) : AtcVariantVisitable {
    override fun uniqueId(): Long = position

    override fun isEqual(newData: AtcVariantVisitable): Boolean {
        return if (newData is VariantQuantityDataModel) {
            minOrder == newData.minOrder &&
                    maxOrder == newData.maxOrder &&
                    shouldShowView == newData.shouldShowView &&
                    productId == newData.productId &&
                    quantity == newData.quantity &&
                    shouldShowDeleteButton == newData.shouldShowDeleteButton
        } else {
            false
        }
    }

    override fun getChangePayload(newData: AtcVariantVisitable): Bundle? {
        val bundle = Bundle()
        return if (newData is VariantQuantityDataModel) {
            if (allTheSameExceptProductId(newData)) {
                bundle.putInt("payload", PAYLOAD_UPDATE_PRODUCT_ID_ONLY)
                return bundle
            }

            null
        } else {
            null
        }
    }

    override fun type(typeFactory: AtcVariantTypeFactory): Int {
        return typeFactory.type(this)
    }

    private fun allTheSameExceptProductId(newData: VariantQuantityDataModel): Boolean {
        return minOrder == newData.minOrder &&
                maxOrder == newData.maxOrder &&
                shouldShowView == newData.shouldShowView &&
                quantity == newData.quantity &&
                shouldShowDeleteButton == newData.shouldShowDeleteButton &&
                productId != newData.productId
    }
}