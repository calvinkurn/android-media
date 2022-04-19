package com.tkpd.atcvariant.view.adapter

import com.tkpd.atcvariant.data.uidata.*

/**
 * Created by Yehezkiel on 06/05/21
 */
interface AtcVariantTypeFactory {
    fun type(data: VariantHeaderDataModel): Int
    fun type(data: VariantComponentDataModel): Int
    fun type(data: VariantQuantityDataModel): Int
    fun type(data: VariantShimmeringDataModel): Int
    fun type(data: VariantErrorDataModel): Int
}