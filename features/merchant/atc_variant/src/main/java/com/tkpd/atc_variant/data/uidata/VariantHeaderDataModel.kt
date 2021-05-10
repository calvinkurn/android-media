package com.tkpd.atc_variant.data.uidata

import android.os.Bundle
import com.tkpd.atc_variant.views.adapter.AtcVariantTypeFactory
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable

/**
 * Created by Yehezkiel on 06/05/21
 */
data class VariantHeaderDataModel(
        val position: Long = 0,
        val title: String = "",
        val priceFmt: String = "",
        val discountedPrice: String = "",
        val productImageUrl: String = "",
        val cashbackPercentage: Int = 0,
        val discountPercentage: Int = 0,
        val stock: Int = 0
) : AtcVariantVisitable {
    override fun uniqueId(): Long = position

    override fun isEqual(newData: AtcVariantVisitable): Boolean {
        return if (newData is VariantHeaderDataModel) {
            title == newData.title &&
                    priceFmt == newData.priceFmt &&
                    discountedPrice == newData.discountedPrice &&
                    productImageUrl == newData.productImageUrl &&
                    cashbackPercentage == newData.cashbackPercentage &&
                    discountPercentage == newData.discountPercentage &&
                    stock == newData.stock
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