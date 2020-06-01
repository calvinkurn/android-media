package com.tokopedia.abstraction.processor


import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.Key
import com.tokopedia.annotation.defaultvalues.DefaultValueLong
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.firebase.analytic.rules.ProductDetailViewsRules
import com.tokopedia.product.util.processor.KEY_SESSION_IRIS

/**
 * Product Detail
 */
@AnalyticEvent(false, Event.VIEW_ITEM, ProductDetailViewsRules::class)
data class ProductDetailViews(
        @Key("shopName")
        val shopName: String,
        @Key("shopId")
        val shopId: String,
        @Key("items")
        val items: List<ProductDetailProduct>,


        // MUST BE FILLED By USER
        @Key(KEY_SESSION_IRIS)
        val sessionIris: String,
        @DefaultValueString("")
        @Key("currentSite")
        val currentSite: String?,
        @DefaultValueString("")
        @Key("event")
        val event: String?,
        @DefaultValueString("")
        @Key("eventCategory")
        val eventCategory: String?,
        @DefaultValueString("")
        @Key("eventAction")
        val eventAction: String?,
        @DefaultValueString("")
        @Key("businessUnit")
        val businessUnit: String?,
        @DefaultValueString("")
        @Key("screenName")
        val screenName: String?

)


private const val KEY_DIMENSION_83 = "dimension83"
private const val KEY_DIMENSION_47 = "dimension47"
private const val KEY_DIMENSION_48 = "dimension48"
private const val KEY_DIMENSION_37 = "dimension37"
private const val KEY_DIMENSION_98 = "dimension98"


@BundleThis(false, true)
data class ProductDetailProduct(
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
        @DefaultValueLong(1)
        @Key(Param.INDEX)
        val index: Long,
        @DefaultValueString("none / other")
        @Key(KEY_DIMENSION_83)
        val dimension83: String?,
        @Key(KEY_DIMENSION_47)
        val dimension47: String,
        @Key(KEY_DIMENSION_48)
        val dimension48: String,
        @Key(KEY_DIMENSION_37)
        val dimension37: String,
        @Key(KEY_DIMENSION_98)
        val dimension98: String
)