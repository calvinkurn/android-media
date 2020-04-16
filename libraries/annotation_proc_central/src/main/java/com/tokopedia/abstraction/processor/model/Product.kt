package com.tokopedia.abstraction.processor.model

import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.Key
import com.tokopedia.annotation.defaultvalues.DefaultValueLong
import com.tokopedia.annotation.defaultvalues.DefaultValueString

private const val KEY_DIMENSION_40 = "dimension40"

@BundleThis(false, true)
data class Product(
        @Key(Param.ITEM_ID)
        val id: String,
        @Key(Param.ITEM_NAME)
        val name: String,
        @Key(Param.ITEM_CATEGORY)
        val category: String,
        @Key(Param.ITEM_VARIANT)
        val variant: String,
        @DefaultValueString("none")
        @Key(Param.ITEM_BRAND)
        val brand: String?,
        @Key(Param.PRICE)
        val price: Double,
        @DefaultValueString("IDR")
        @Key(Param.CURRENCY)
        val currency: String?,
        @Key(KEY_DIMENSION_40)
        val keyDimension40: String,
        @DefaultValueLong(0)
        @Key(Param.INDEX)
        val index: Long
)