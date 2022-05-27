package com.tokopedia.createpost.producttag.analytic.product

import com.tokopedia.createpost.producttag.analytic.*
import com.tokopedia.createpost.producttag.analytic.KEY_BUSINESS_UNIT
import com.tokopedia.createpost.producttag.analytic.KEY_CURRENT_SITE
import com.tokopedia.createpost.producttag.analytic.KEY_EVENT
import com.tokopedia.createpost.producttag.analytic.KEY_EVENT_ACTION
import com.tokopedia.createpost.producttag.analytic.KEY_EVENT_CATEGORY
import com.tokopedia.createpost.producttag.analytic.KEY_EVENT_LABEL
import com.tokopedia.createpost.producttag.analytic.KEY_SESSION_IRIS
import com.tokopedia.createpost.producttag.analytic.VAL_CURRENT_SITE
import com.tokopedia.createpost.producttag.view.uimodel.*
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
class ProductTagAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val trackingQueue: TrackingQueue,
) : ProductTagAnalytic {

    override fun trackGlobalSearchProduct(
        header: SearchHeaderUiModel,
        param: SearchParamUiModel,
    ) {
        hitGlobalSearchTracker(VAL_FEED_PRODUCT, header, param)
    }

    override fun trackGlobalSearchShop(
        header: SearchHeaderUiModel,
        param: SearchParamUiModel,
    ) {
        hitGlobalSearchTracker(VAL_FEED_SHOP, header, param)
    }

    override fun clickBreadcrumb(isOnShop: Boolean) {
        sendClickEvent(
            if(isOnShop) "click - product tagging source list on toko"
            else "click - product tagging source list"
        )
    }

    override fun clickProductTagSource(source: ProductTagSource) {
        sendClickEvent("click - product tagging source", source.labelAnalytic)
    }

    override fun impressProductCard(
        source: ProductTagSource,
        products: List<Pair<ProductUiModel, Int>>,
        isGlobalSearch: Boolean
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = "productView",
                category = "content feed post creation - product tagging",
                action = if(isGlobalSearch) "impression - product card" else "impression - entry point product card",
                label = "${source.labelAnalytic} - ${userSession.shopId} - ${products.firstOrNull()?.first?.id ?: 0}"
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "currencyCode" to "IDR",
                    "impressions" to products.map {
                        convertProductToHashMapWithList(it.first, it.second)
                    }
                )
            ),
            hashMapOf(
                KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to VAL_CONTENT
            )
        )
    }

    override fun clickProductCard(
        source: ProductTagSource,
        product: ProductUiModel,
        position: Int,
        isGlobalSearch: Boolean
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = "productClick",
                category = "content feed post creation - product tagging",
                action = if(isGlobalSearch) "click - product card" else "click - entry point product card",
                label = "${source.labelAnalytic} - ${userSession.shopId} - ${product.id}"
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "click" to hashMapOf(
                        "actionField" to hashMapOf( "list" to "/feed - creation tagging page"),
                        "products" to listOf(convertProductToHashMapWithList(product, position))
                    )
                )
            ),
            hashMapOf(
                KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to VAL_CONTENT
            )
        )
    }

    override fun clickSearchBar(source: ProductTagSource) {
        sendClickEvent("click - search bar", source.labelAnalytic)
    }

    override fun clickGlobalSearchTab(tabName: String) {
        sendClickEvent("click - tab", tabName)
    }

    override fun clickBackButton(source: ProductTagSource) {
        if(source == ProductTagSource.Shop)
            sendClickEvent("click - back button on toko")
        else
            sendClickEvent("click - back on product tagging", source.labelAnalytic)
    }

    override fun impressShopCard(
        source: ProductTagSource,
        shops: List<Pair<ShopUiModel, Int>>,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = "promoView",
                category = "content feed post creation - product tagging",
                action = "impression - toko product tagging search result",
                label = userSession.shopId
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "promoView" to hashMapOf(
                        "promotions" to shops.map {
                            convertToPromotion(it.first, it.second)
                        }
                    )
                )
            ),
            hashMapOf(
                KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to VAL_CONTENT
            )
        )
    }

    override fun clickShopCard(
        shop: ShopUiModel,
        position: Int,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = "promoClick",
                category = "content feed post creation - product tagging",
                action = "click - toko product tagging search result",
                label = userSession.shopId
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "promoClick" to hashMapOf(
                        "promotions" to listOf(convertToPromotion(shop, position))
                    )
                )
            ),
            hashMapOf(
                KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to VAL_CONTENT
            )
        )
    }

    override fun clickSearchBarOnShop() {
        sendClickEvent("click - search button on toko")
    }

    override fun impressProductCardOnShop(
        products: List<Pair<ProductUiModel, Int>>
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = "productView",
                category = "content feed post creation - product tagging",
                action = "impression - product card on toko",
                label = "${userSession.shopId} - ${products.firstOrNull()?.first?.id ?: 0}"
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "currencyCode" to "IDR",
                    "impressions" to products.map {
                        convertProductToHashMapWithList(it.first, it.second)
                    }
                )
            ),
            hashMapOf(
                KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to VAL_CONTENT
            )
        )
    }

    override fun clickProductCardOnShop(
        product: ProductUiModel,
        position: Int
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = "productClick",
                category = "content feed post creation - product tagging",
                action = "click - product card on toko",
                label = "${userSession.shopId} - ${product.id}"
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "click" to hashMapOf(
                        "actionField" to hashMapOf( "list" to "/feed - creation tagging page"),
                        "products" to listOf(convertProductToHashMapWithList(product, position))
                    )
                )
            ),
            hashMapOf(
                KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to VAL_CONTENT
            )
        )
    }

    override fun sendAll() {
        trackingQueue.sendAll()
    }

    private fun sendClickEvent(action: String, label: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to "clickPG",
                KEY_EVENT_ACTION to action,
                KEY_EVENT_CATEGORY to "content feed post creation - product tagging",
                KEY_EVENT_LABEL to label,
                KEY_BUSINESS_UNIT to VAL_CONTENT,
                KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userSession.userId,
            )
        )
    }

    private fun hitGlobalSearchTracker(
        type: String,
        header: SearchHeaderUiModel,
        param: SearchParamUiModel,
    ) {
        val action = when(type) {
            VAL_FEED_PRODUCT -> "general search"
            VAL_FEED_SHOP -> "general search shop"
            else -> ""
        }
        val prevKeyword = if(param.prevQuery.isNotEmpty()) param.prevQuery else "none"

        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to "clickTopNav",
                KEY_EVENT_ACTION to action,
                KEY_EVENT_CATEGORY to "top nav",
                /** TODO: srp page title ? */
                KEY_EVENT_LABEL to "${param.query}|${header.keywordProcess}|${header.responseCode}|physical goods|$type|{}|${header.totalData}",
                KEY_BUSINESS_UNIT to "physical goods",
                KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                KEY_SRP_COMPONENT to header.componentId,
                KEY_SRP_KEYWORD to param.query,
                KEY_SRP_PAGE_SOURCE to "feed.$type.local_search.{}", /** TODO: srp page title ? */
                KEY_SRP_RELATED_KEYWORD to "$prevKeyword - none",
                KEY_SRP_SEARCH_FILTER to param.toTrackerString(),
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userSession.userId,
            )
        )
    }

    private fun convertProductToHashMapWithList(product: ProductUiModel, position: Int): HashMap<String, Any> {
        return hashMapOf(
            "name" to product.name,
            "id" to product.id,
            "price" to product.price,
            "brand" to "",
            "category" to "",
            "variant" to "",
            "position" to position,
            "list" to "/feed - creation tagging page",
        )
    }

    private fun convertToPromotion(shop: ShopUiModel, position: Int): HashMap<String, Any> {
        return hashMapOf(
            "creative" to shop.shopImage,
            "position" to position,
            "id" to shop.shopId,
            "name" to "/feed content creation - toko tab search result",
        )
    }
}