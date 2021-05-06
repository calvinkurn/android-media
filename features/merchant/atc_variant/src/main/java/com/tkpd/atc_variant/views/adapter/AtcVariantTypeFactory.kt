package com.tkpd.atc_variant.views.adapter

import com.tkpd.atc_variant.data.VariantHeaderDataModel
import com.tkpd.atc_variant.data.VariantStockDataModel

/**
 * Created by Yehezkiel on 06/05/21
 */
interface AtcVariantTypeFactory {
    fun type(data: VariantHeaderDataModel): Int
    fun type(data: VariantStockDataModel): Int
}