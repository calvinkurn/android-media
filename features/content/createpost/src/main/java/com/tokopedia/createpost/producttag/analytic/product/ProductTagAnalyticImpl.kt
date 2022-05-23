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
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
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
        shopId: String,
        productId: String,
        products: List<Pair<ProductUiModel, Int>>,
        isGlobalSearch: Boolean
    ) {
        /** TODO("Not yet implemented") */
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
                label = "${source.labelAnalytic} - {shop_id} - ${product.id}" /** TODO: shopId? */
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

    override fun clickLastSearch() {
        sendClickEvent("click - search terakhir dicari")
    }

    override fun clickShopSuggestion(shopId: String) {
        sendClickEvent("click - toko section search result", shopId)
    }

    override fun impressShopSuggestion(shopId: String) {
        /** TODO: not sure dunno how */
    }

    override fun clickKeywordSuggestion() {
        sendClickEvent("click - recommendation search result")
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

    override fun impressShopCard() {
        /** TODO("Not yet implemented") */
    }

    override fun clickShopCard() {
        /** TODO("Not yet implemented") */
    }

    override fun clickSearchBarOnShop() {
        sendClickEvent("click - search bar on toko")
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
                label = "{shop_id} - ${product.id}" /** TODO: shopId? */
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

    private fun convertProductToHashMapWithList(product: ProductUiModel, position: Int): HashMap<String, Any> {
        return hashMapOf(
            "name" to product.name,
            "id" to product.id,
            "price" to product.price,
            "brand" to "",
            "category" to "",
            "variant" to "",
            "position" to position
        )
    }
}