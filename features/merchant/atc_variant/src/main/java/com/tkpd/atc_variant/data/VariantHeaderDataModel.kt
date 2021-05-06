package com.tkpd.atc_variant.data

import com.tkpd.atc_variant.views.adapter.AtcVariantTypeFactory
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable

/**
 * Created by Yehezkiel on 06/05/21
 */
data class VariantHeaderDataModel(
        val title: String = "",
        val price: String = "",
        val priceFmt: String = "",
        val productImageUrl: String = ""
) : AtcVariantVisitable {
    override fun uniqueId(): Long {
        TODO("Not yet implemented")
    }

    override fun isEqual(newData: AtcVariantVisitable): Boolean {
        TODO("Not yet implemented")
    }

    override fun type(typeFactory: AtcVariantTypeFactory): Int {
        return typeFactory.type(this)
    }
}