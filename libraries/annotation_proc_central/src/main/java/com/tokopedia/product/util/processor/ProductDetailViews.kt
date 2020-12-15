package com.tokopedia.product.util.processor


import com.tokopedia.analytic.annotation.*
import com.tokopedia.analytic_constant.Event
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.firebase.analytic.rules.ProductDetailRules
import com.tokopedia.util.GTMErrorHandlerImpl
import com.tokopedia.util.logger.GTMLoggerImpl

const val KEY_SESSION_IRIS = "sessionIris"

@ErrorHandler(GTMErrorHandlerImpl::class)
@Logger(GTMLoggerImpl::class)
@AnalyticEvent(false, Event.VIEW_ITEM, ProductDetailRules::class)
data class ProductDetailViews(
        @Key(com.tokopedia.analytic_constant.Param.ITEM_LIST)
        val itemList: String,
        @CustomChecker(ProductDetailViewsChecker::class, Level.ERROR, functionName = ["isOnlyOneProduct"])
        @Key("items")
        val items: List<Product>,
        @Key("key")
        val key: String,
        @Key("shopName")
        val shopName: String,
        @Key("shopId")
        val shopId: String,
        @Key("shopDomain")
        val shopDomain: String,
        @Key("shopLocation")
        val shopLocation: String,
        @Key("shopIsGold")
        val shopIsGold: String,
        @Key("categoryId")
        val categoryId: String,
        @Key("shopType")
        val shopType: String,
        @Key("pageType")
        val pageType: String,
        @Key("subcategory")
        val subcategory: String,
        @Key("subcategoryId")
        val subcategoryId: String,
        @Key("productUrl")
        val productUrl: String,
        @Key("productDeeplinkUrl")
        val productDeeplinkUrl: String,
        @Key("productImageUrl")
        val productImageUrl: String,
        @Key("isOfficialStore")
        val officialStore: String,
        @Key("productPriceFormatted")
        val productPriceFormatted: String,
        @Key(ProductTrackingConstant.Tracking.KEY_PRODUCT_ID)
        val productId: String,
        @Key(ProductTrackingConstant.Tracking.KEY_LAYOUT)
        val layout: String,
        @Key(ProductTrackingConstant.Tracking.KEY_COMPONENT)
        val component: String,
        @Key("productPrice")
        val productPrice: String,
        @Key("productName")
        val productName: String,
        @Key("productGroupName")
        val productGroupName: String,
        @Key("productGroupId")
        val productGroupId: String,
        @Key("category")
        val category: String,
        @Key(KEY_SESSION_IRIS)
        val sessionIris: String,
        @DefaultValueString("")
        @Key("currentSite")
        val currentSite: String?,
        @CustomChecker(ProductDetailViewsChecker::class, Level.ERROR, functionName = ["onlyViewItem"])
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
        @Key("eventLabel")
        val eventLabel: String?,
        @DefaultValueString("")
        @Key("businessUnit")
        val businessUnit: String?,
        @DefaultValueString("")
        @Key("screenName")
        val screenName: String?,
        @DefaultValueString("")
        @Key("variant")
        val variant: String?,
        @DefaultValueString("")
        @Key("campaignCode")
        val campaignCode: String?,
        @DefaultValueString("")
        @Key("productStatus")
        val productStatus: String?,
        @DefaultValueString("")
        @Key("stockAmount")
        val stockAmount: String?

)

object ProductDetailViewsChecker {
        fun onlyViewItem(event: String?) =
                event?.toLowerCase()?.contains("view_item") ?: false

        fun isOnlyOneProduct(items: List<Product>) = items.size == 1

        fun checkMap(map: Map<String, String>) = map.isNotEmpty()

        fun isIndexNotZero(index: Long) = !(index > 0)
}