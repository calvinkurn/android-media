package com.tokopedia.content.common.sample.analytic

import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.view.uimodel.*
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 12, 2022
 */
class ContentProductTagSampleAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val trackingQueue: TrackingQueue,
) : ContentProductTagAnalytic {

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

    override fun clickProductTagSource(
        source: ProductTagSource,
        authorId: String,
        authorType: String
    ) {
        sendClickEvent("click - product tagging source", source.labelAnalytic)
    }

    override fun impressProductCard(
        source: ProductTagSource,
        products: List<Pair<ProductUiModel, Int>>,
        isEntryPoint: Boolean
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_PRODUCT_VIEW,
                category = VAL_EVENT_CATEGORY,
                action = if(isEntryPoint) "impression - entry point product card" else "impression - product card",
                label = "${source.labelAnalytic} - ${userSession.shopId} - ${products.firstOrNull()?.first?.id ?: 0}"
            ),
            hashMapOf(
                KEY_ECOMMERCE to hashMapOf(
                    KEY_CURRENCY_CODE to VAL_IDR,
                    KEY_IMPRESSIONS to products.map {
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
        isEntryPoint: Boolean
    ) {
        trackingQueue.putEETracking(
            EventModel(
                event = EVENT_PRODUCT_CLICK,
                category = VAL_EVENT_CATEGORY,
                action = if(isEntryPoint) "click - entry point product card" else "click - product card",
                label = "${source.labelAnalytic} - ${userSession.shopId} - ${product.id}"
            ),
            hashMapOf(
                KEY_ECOMMERCE to hashMapOf(
                    KEY_CLICK to hashMapOf(
                        KEY_ACTION_FIELD to hashMapOf( "list" to "/feed - creation tagging page"),
                        KEY_PRODUCTS to listOf(convertProductToHashMapWithList(product, position))
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
                event = EVENT_PROMO_VIEW,
                category = VAL_EVENT_CATEGORY,
                action = "impression - toko product tagging search result",
                label = userSession.shopId
            ),
            hashMapOf(
                KEY_ECOMMERCE to hashMapOf(
                    EVENT_PROMO_VIEW to hashMapOf(
                        KEY_PROMOTIONS to shops.map {
                            convertToPromotion(it.first, it.second + 1)
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
                event = EVENT_PROMO_CLICK,
                category = VAL_EVENT_CATEGORY,
                action = "click - toko product tagging search result",
                label = userSession.shopId
            ),
            hashMapOf(
                KEY_ECOMMERCE to hashMapOf(
                    EVENT_PROMO_CLICK to hashMapOf(
                        KEY_PROMOTIONS to listOf(convertToPromotion(shop, position))
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
                event = EVENT_PRODUCT_VIEW,
                category = VAL_EVENT_CATEGORY,
                action = "impression - product card on toko",
                label = "${userSession.shopId} - ${products.firstOrNull()?.first?.id ?: 0}"
            ),
            hashMapOf(
                KEY_ECOMMERCE to hashMapOf(
                    KEY_CURRENCY_CODE to VAL_IDR,
                    KEY_IMPRESSIONS to products.map {
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
                event = EVENT_PRODUCT_CLICK,
                category = VAL_EVENT_CATEGORY,
                action = "click - product card on toko",
                label = "${userSession.shopId} - ${product.id}"
            ),
            hashMapOf(
                KEY_ECOMMERCE to hashMapOf(
                    KEY_CLICK to hashMapOf(
                        KEY_ACTION_FIELD to hashMapOf( "list" to "/feed - creation tagging page"),
                        KEY_PRODUCTS to listOf(convertProductToHashMapWithList(product, position))
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

    override fun clickSaveProduct(source: ProductTagSource) {

    }

    override fun clickAdvancedProductFilter() {

    }

    override fun clickSaveAdvancedProductFilter() {

    }

    override fun clickProductFilterChips() {

    }

    override fun viewProductTagSourceBottomSheet(authorId: String, authorType: String) {

    }

    override fun clickCloseOnProductTagSourceBottomSheet(authorId: String, authorType: String) {

    }

    override fun sendAll() {
        trackingQueue.sendAll()
    }

    private fun sendClickEvent(action: String, label: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to "clickPG",
                KEY_EVENT_ACTION to action,
                KEY_EVENT_CATEGORY to VAL_EVENT_CATEGORY,
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

        val srpPageTitle = when(type) {
            VAL_FEED_PRODUCT -> 0
            VAL_FEED_SHOP -> userSession.shopId
            else -> 0
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to "clickTopNav",
                KEY_EVENT_ACTION to action,
                KEY_EVENT_CATEGORY to "top nav",
                KEY_EVENT_LABEL to "${param.query}|${header.keywordProcess}|${header.responseCode}|physical goods|$type|$srpPageTitle|${header.totalData}",
                KEY_BUSINESS_UNIT to "physical goods",
                KEY_CURRENT_SITE to VAL_CURRENT_SITE,
                KEY_SRP_COMPONENT to header.componentId,
                KEY_SRP_KEYWORD to param.query,
                KEY_SRP_PAGE_SOURCE to "feed.$type.local_search.$srpPageTitle",
                KEY_SRP_RELATED_KEYWORD to "$prevKeyword - none",
                KEY_SRP_SEARCH_FILTER to param.toTrackerString(),
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userSession.userId,
            )
        )
    }

    private fun convertProductToHashMapWithList(product: ProductUiModel, position: Int): HashMap<String, Any> {
        return hashMapOf(
            KEY_PRODUCT_NAME to product.name,
            KEY_PRODUCT_ID to product.id,
            KEY_PRODUCT_PRICE to product.price,
            KEY_PRODUCT_BRAND to "",
            KEY_PRODUCT_CATEGORY to "",
            KEY_PRODUCT_VARIANT to "",
            KEY_PRODUCT_POSITION to position,
            KEY_PRODUCT_LIST to VAL_PRODUCT_LIST,
        )
    }

    private fun convertToPromotion(shop: ShopUiModel, position: Int): HashMap<String, Any> {
        return hashMapOf(
            KEY_CREATIVE to shop.shopImage,
            KEY_CREATIVE_POSITION to position,
            KEY_CREATIVE_ID to shop.shopId,
            KEY_CREATIVE_NAME to VAL_CREATIVE_NAME,
        )
    }
}
