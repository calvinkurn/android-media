package com.tkpd.atc_variant.views.adapter

import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tkpd.atc_variant.data.uidata.VariantHeaderDataModel
import com.tkpd.atc_variant.data.uidata.VariantQuantityDataModel
import com.tkpd.atc_variant.data.uidata.VariantShimmeringDataModel

/**
 * Created by Yehezkiel on 06/05/21
 */
interface AtcVariantTypeFactory {
    fun type(data: VariantHeaderDataModel): Int
    fun type(data: VariantComponentDataModel): Int
    fun type(data: VariantQuantityDataModel): Int
    fun type(data: VariantShimmeringDataModel): Int
}