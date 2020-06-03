package com.tokopedia.abstraction.processor


import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.Key
import com.tokopedia.annotation.defaultvalues.DefaultValueLong
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.firebase.analytic.rules.ProductClicksRules

/**
 * Product List Click
 */
@AnalyticEvent(true, Event.SELECT_CONTENT, ProductClicksRules::class)
data class ProductListClick(
        @DefaultValueString("Search Results")
        val item_list: String,
        val items: ArrayList<ProductListClickProduct>,
        @DefaultValueString("")
        val eventCategory: String?,
        @DefaultValueString("")
        val eventAction: String?,
        @DefaultValueString("")
        val event: String?,
        @DefaultValueString("")
        val currentSite: String?,
        @DefaultValueString("")
        val businessUnit: String?,
        @DefaultValueString("")
        val screenName: String?
)


private const val KEY_DIMENSION_40 = "dimension40"

@BundleThis(false, true)
data class ProductListClickProduct(
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
