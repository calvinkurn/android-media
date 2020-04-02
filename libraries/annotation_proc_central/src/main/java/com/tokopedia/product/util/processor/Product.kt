package com.tokopedia.product.util.processor

import com.example.annotation.BundleThis
import com.example.annotation.Key
import com.example.annotation.defaultvalues.DefaultValueInt
import com.example.annotation.defaultvalues.DefaultValueLong
import com.example.annotation.defaultvalues.DefaultValueString
import com.tokopedia.analytic_constant.Param
import com.tokopedia.product.detail.data.util.ProductTrackingConstant

private const val KEY_DIMENSION_40 = "dimension40"

@BundleThis(false, true)
data class Product(
        @Key(Param.ITEM_NAME)
        val name: String,
        @Key(Param.ITEM_ID)
        val id: String,
        @Key(Param.PRICE)
        val price: Double,
        @DefaultValueString("none")
        @Key(Param.ITEM_BRAND)
        val brand: String,
        @Key(Param.ITEM_VARIANT)
        val variant: String,
        @Key(Param.ITEM_CATEGORY)
        val category: String,
        @DefaultValueString("IDR")
        @Key(Param.CURRENCY)
        val currency: String,
        @DefaultValueString(ProductTrackingConstant.Tracking.DEFAULT_VALUE)
        @Key(ProductTrackingConstant.Tracking.KEY_DIMENSION_38)
        val dimension38: String,
        @Key(ProductTrackingConstant.Tracking.KEY_DIMENSION_55)
        val dimension55: String,
        @Key(ProductTrackingConstant.Tracking.KEY_DIMENSION_54)
        val dimension54: String,
        @Key(ProductTrackingConstant.Tracking.KEY_DIMENSION_83)
        val dimension83: String,
        @Key(ProductTrackingConstant.Tracking.KEY_DIMENSION_81)
        val dimension81: String,
        @DefaultValueLong(0)
        @Key(Param.INDEX)
        val index: Long
)