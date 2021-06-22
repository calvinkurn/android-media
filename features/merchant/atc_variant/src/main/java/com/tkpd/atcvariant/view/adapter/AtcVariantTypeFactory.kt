package com.tkpd.atcvariant.view.adapter

import com.tkpd.atcvariant.data.uidata.VariantComponentDataModel
import com.tkpd.atcvariant.data.uidata.VariantHeaderDataModel
import com.tkpd.atcvariant.data.uidata.VariantQuantityDataModel
import com.tkpd.atcvariant.data.uidata.VariantShimmeringDataModel

/**
 * Created by Yehezkiel on 06/05/21
 */
interface AtcVariantTypeFactory {
    fun type(data: VariantHeaderDataModel): Int
    fun type(data: VariantComponentDataModel): Int
    fun type(data: VariantQuantityDataModel): Int
    fun type(data: VariantShimmeringDataModel): Int
}