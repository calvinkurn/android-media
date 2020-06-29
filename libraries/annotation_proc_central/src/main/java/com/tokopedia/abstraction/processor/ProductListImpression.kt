package com.tokopedia.abstraction.processor


import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.Key
import com.tokopedia.annotation.defaultvalues.DefaultValueLong
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.firebase.analytic.rules.ProductImpressionsRules

/**
 * Product Detail
 */
@AnalyticEvent(true, Event.VIEW_ITEM_LIST, ProductImpressionsRules::class)
data class ProductListImpression(
        val item_list: String,
        val items: List<ProductListImpressionProduct>,

        @DefaultValueString("")
        val currentSite: String?,
        @DefaultValueString("")
        val event: String?,
        @DefaultValueString("")
        val eventCategory: String?,
        @DefaultValueString("")
        val eventAction: String?,
        @DefaultValueString("")
        val businessUnit: String?,
        @DefaultValueString("")
        val screenName: String?

)


private const val KEY_DIMENSION_40 = "dimension40"


@BundleThis(false, true)
data class ProductListImpressionProduct(
        @Key(Param.ITEM_ID)
        val id: String,
        @Key(Param.ITEM_NAME)
        val name: String,
        @DefaultValueString("none")
        @Key(Param.ITEM_BRAND)
        val brand: String?,
        @Key(Param.ITEM_CATEGORY)
        val category: String,
        @Key(Param.ITEM_VARIANT)
        val variant: String,
        @Key(Param.PRICE)
        val price: Double,
        @DefaultValueString("IDR")
        @Key(Param.CURRENCY)
        val currency: String?,
        @DefaultValueLong(1)
        @Key(Param.INDEX)
        val index: Long,
        @Key("list")
        val list: String,
        @DefaultValueString("none / other")
        @Key(KEY_DIMENSION_40)
        val dimension40: String?
)