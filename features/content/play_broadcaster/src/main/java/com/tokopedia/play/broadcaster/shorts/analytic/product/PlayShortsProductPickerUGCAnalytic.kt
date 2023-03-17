package com.tokopedia.play.broadcaster.shorts.analytic.product

import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.view.uimodel.*
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.shorts.analytic.helper.PlayShortsAnalyticHelper
import com.tokopedia.play.broadcaster.shorts.analytic.sender.PlayShortsAnalyticSender
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 29, 2022
 */

/**
 * Mynakama Tracker
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3511
 */
class PlayShortsProductPickerUGCAnalytic @Inject constructor(
    private val analyticSender: PlayShortsAnalyticSender,
) : ContentProductTagAnalytic {

    /**
     * Row 76
     */
    override fun viewProductTagSourceBottomSheet(authorId: String, authorType: String) {
        analyticSender.sendGeneralViewEvent(
            eventAction = "view - product selection user profile",
            eventLabel = PlayShortsAnalyticHelper.getEventLabelByAccount(authorId, authorType),
            trackerId = "37599",
        )
    }

    /**
     * Row 77
     */
    override fun clickCloseOnProductTagSourceBottomSheet(authorId: String, authorType: String) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - close product selection filter",
            eventLabel = PlayShortsAnalyticHelper.getEventLabelByAccount(authorId, authorType),
            trackerId = "37600",
        )
    }

    /**
     * Row 78
     */
    override fun clickProductTagSource(
        source: ProductTagSource,
        authorId: String,
        authorType: String
    ) {
        analyticSender.sendGeneralClickEvent(
            eventAction = "click - product selection filter",
            eventLabel = PlayShortsAnalyticHelper.getEventLabelByAccount(authorId, authorType),
            trackerId = "37601",
        )
    }

    override fun trackGlobalSearchProduct(header: SearchHeaderUiModel, param: SearchParamUiModel) {
        /** not applicable for Play Shorts */
    }

    override fun trackGlobalSearchShop(header: SearchHeaderUiModel, param: SearchParamUiModel) {
        /** not applicable for Play Shorts */
    }

    override fun clickBreadcrumb(isOnShop: Boolean) {
        /** not applicable for Play Shorts */
    }

    override fun impressProductCard(
        source: ProductTagSource,
        products: List<Pair<ProductUiModel, Int>>,
        isEntryPoint: Boolean
    ) {
        /** not applicable for Play Shorts */
    }

    override fun clickProductCard(
        source: ProductTagSource,
        product: ProductUiModel,
        position: Int,
        isEntryPoint: Boolean
    ) {
        /** not applicable for Play Shorts */
    }

    override fun clickSearchBar(source: ProductTagSource) {
        /** not applicable for Play Shorts */
    }

    override fun clickGlobalSearchTab(tabName: String) {
        /** not applicable for Play Shorts */
    }

    override fun clickBackButton(source: ProductTagSource) {
        /** not applicable for Play Shorts */
    }

    override fun impressShopCard(source: ProductTagSource, shops: List<Pair<ShopUiModel, Int>>) {
        /** not applicable for Play Shorts */
    }

    override fun clickShopCard(shop: ShopUiModel, position: Int) {
        /** not applicable for Play Shorts */
    }

    override fun clickSearchBarOnShop() {
        /** not applicable for Play Shorts */
    }

    override fun impressProductCardOnShop(products: List<Pair<ProductUiModel, Int>>) {
        /** not applicable for Play Shorts */
    }

    override fun clickProductCardOnShop(product: ProductUiModel, position: Int) {
        /** not applicable for Play Shorts */
    }

    override fun clickSaveProduct(source: ProductTagSource) {
        /** not applicable for Play Shorts */
    }

    override fun clickAdvancedProductFilter() {
        /** not applicable for Play Shorts */
    }

    override fun clickSaveAdvancedProductFilter() {
        /** not applicable for Play Shorts */
    }

    override fun clickProductFilterChips() {
        /** not applicable for Play Shorts */
    }

    override fun sendAll() {
        /** not applicable for Play Shorts */
    }
}
