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
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
class ProductTagAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : ProductTagAnalytic {

    override fun clickBreadcrumb() {
        sendClickEvent("click - product tagging source list")
    }

    override fun clickProductTagSource(source: String) {
        sendClickEvent("click - product tagging source", source)
    }

    override fun impressProductCard(
        source: String,
        shopId: String,
        productId: String,
        products: List<ProductUiModel>,
        isGlobalSearch: Boolean
    ) {
        /** TODO("Not yet implemented") */
    }

    override fun clickProductCard(
        source: String,
        shopId: String,
        productId: String,
        products: List<ProductUiModel>,
        isGlobalSearch: Boolean
    ) {
        /** TODO("Not yet implemented") */
    }

    override fun clickSearchBar(source: String) {
        sendClickEvent("click - search bar", source)
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

    override fun clickBackButton(source: String) {
        sendClickEvent("click - back on product tagging", source)
    }

    override fun impressShopCard() {
        /** TODO("Not yet implemented") */
    }

    override fun clickShopCard() {
        /** TODO("Not yet implemented") */
    }

    override fun clickBackOnShop() {
        sendClickEvent("click - back button on toko")
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
}