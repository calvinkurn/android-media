package com.tokopedia.play.broadcaster.analytic.ugc

import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.SearchHeaderUiModel
import com.tokopedia.content.common.producttag.view.uimodel.SearchParamUiModel
import com.tokopedia.content.common.producttag.view.uimodel.ShopUiModel
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY_PLAY
import com.tokopedia.play.broadcaster.analytic.currentSite
import com.tokopedia.play.broadcaster.analytic.sessionIris
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 12/09/22
 */
class ProductPickerUGCAnalytic @Inject constructor(
    private val userSession: UserSessionInterface
) : ContentProductTagAnalytic {

    private val userId: String
        get() = userSession.userId.orEmpty()

    override fun trackGlobalSearchProduct(header: SearchHeaderUiModel, param: SearchParamUiModel) {
    }

    override fun trackGlobalSearchShop(header: SearchHeaderUiModel, param: SearchParamUiModel) {
    }

    override fun clickBreadcrumb(isOnShop: Boolean) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - product tagging source")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$userId - user")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickProductTagSource(
        source: ProductTagSource,
        authorId: String,
        authorType: String
    ) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - product source ${source.labelAnalytic}")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$userId - user")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressProductCard(
        source: ProductTagSource,
        products: List<Pair<ProductUiModel, Int>>,
        isEntryPoint: Boolean
    ) {
        products.forEach { (product, _) ->
            Tracker.Builder()
                .setEvent(Event.viewContentIris)
                .setEventAction("impression - product card ${source.labelAnalytic}")
                .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
                .setEventLabel("$userId - user - ${product.id}")
                .setBusinessUnit("content")
                .setCurrentSite(currentSite)
                .setCustomProperty(Key.sessionIris, sessionIris)
                .setUserId(userId)
                .build()
                .send()
        }
    }

    override fun clickProductCard(
        source: ProductTagSource,
        product: ProductUiModel,
        position: Int,
        isEntryPoint: Boolean
    ) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - product card ${source.labelAnalytic}")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$userId - user - ${product.id}")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickSearchBar(source: ProductTagSource) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - cari di ${source.labelAnalytic}")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$userId - user")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickGlobalSearchTab(tabName: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - barang atau toko tab")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$userId - user")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickBackButton(source: ProductTagSource) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - back pilih produk dari ${source.labelAnalytic}")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$userId - user")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressShopCard(source: ProductTagSource, shops: List<Pair<ShopUiModel, Int>>) {
        shops.forEach { (shop, _) ->
            Tracker.Builder()
                .setEvent(Event.viewContentIris)
                .setEventAction("impression - toko")
                .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
                .setEventLabel("$userId - user - ${shop.shopId}")
                .setBusinessUnit("content")
                .setCurrentSite(currentSite)
                .setCustomProperty(Key.sessionIris, sessionIris)
                .setUserId(userId)
                .build()
                .send()
        }
    }

    override fun clickShopCard(shop: ShopUiModel, position: Int) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - toko")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$userId - user - ${shop.shopId}")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickSearchBarOnShop() {
        clickSearchBar(ProductTagSource.Shop)
    }

    override fun impressProductCardOnShop(products: List<Pair<ProductUiModel, Int>>) {
        impressProductCard(ProductTagSource.Shop, products, false)
    }

    override fun clickProductCardOnShop(product: ProductUiModel, position: Int) {
        clickProductCard(ProductTagSource.Shop, product, position, false)
    }

    override fun clickSaveProduct(source: ProductTagSource) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - simpan produk ${source.labelAnalytic}")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$userId - user")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickAdvancedProductFilter() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - filter produk")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$userId - user")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickSaveAdvancedProductFilter() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - simpan filter produk")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$userId - user")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickProductFilterChips() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - chips di filter produk")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$userId - user")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun viewProductTagSourceBottomSheet(authorId: String, authorType: String) {
        /** not applicable for Play Broadcaster */
    }

    override fun clickCloseOnProductTagSourceBottomSheet(authorId: String, authorType: String) {
        /** not applicable for Play Broadcaster */
    }

    override fun sendAll() {
    }
}
