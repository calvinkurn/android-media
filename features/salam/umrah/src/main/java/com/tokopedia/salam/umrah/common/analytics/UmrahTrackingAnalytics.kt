package com.tokopedia.salam.umrah.common.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.homepage.data.Products
import com.tokopedia.salam.umrah.homepage.data.UmrahCategories
import com.tokopedia.salam.umrah.homepage.data.UmrahCategoriesFeatured
import com.tokopedia.salam.umrah.pdp.data.ParamPurchase
import com.tokopedia.salam.umrah.search.data.UmrahSearchProduct
import com.tokopedia.salam.umrah.search.data.model.ParamFilter
import com.tokopedia.salam.umrah.search.util.SearchOrCategory
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils


/**
 * @author by firman on 03/12/2019
 */

class UmrahTrackingAnalytics {

    fun umrahOrderDetailInvoice() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_CLICK_EVENT
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_ACTION_INVOICE
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }


    fun umrahOrderDetailLihatDetail() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_CLICK_EVENT
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailUmrahSaya(state: String) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_CLICK_EVENT
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_WIDGET_UMROH_SAYA
        map[TrackAppUtils.EVENT_LABEL] = state

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailDetailPDP() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_CLICK_EVENT
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_PDP
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailDetailEVoucher() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_CLICK_EVENT
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_E_VOUCHER
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailKebijakanPembatalan() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_CLICK_EVENT
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_KEBIJAKAN_PEMBATALAN
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailBatalkanPesanan() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_CLICK_EVENT
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_BATALKAN_PESANAN
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailBantuan() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_CLICK_EVENT
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_BANTUAN
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailBack() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_CLICK_EVENT
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_BACK
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahSearchProductTracker(departPeriod: String, departCity: String, departPrice: String) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_CLICK_UMROH
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_SEARCH_PACKET_UMRAH
        map[TrackAppUtils.EVENT_LABEL] = "$DEPART_MONTH$departPeriod$PRICE_TRACKING$departPrice$DEPART_CITY$departCity"

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahImpressionUmrahSayaTracker(headerTitle: String, myUmrahEntity: MyUmrahEntity, position: Int) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_PROMO_VIEW
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_IMPRESSION_WIDGET_MY_UMRAH
        map[TrackAppUtils.EVENT_LABEL] = headerTitle
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PROMO_VIEW, DataLayer.mapOf(
                PROMOTIONS_LABEL, getListMyUmrahEntity(myUmrahEntity, position)
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahImpressionDanaImpianTracker() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_PROMO_VIEW
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_IMPRESSION_DREAM_FUND
        map[TrackAppUtils.EVENT_LABEL] = ""
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PROMO_VIEW, DataLayer.mapOf(
                PROMOTIONS_LABEL, getListDanaImpian()
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahClickDanaImpianTracker() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_PROMO_CLICK
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_DREAM_FUND
        map[TrackAppUtils.EVENT_LABEL] = ""
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PROMO_CLICK, DataLayer.mapOf(
                PROMOTIONS_LABEL, getListDanaImpian()
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)

    }

    fun umrahClickUmrahSayaTracker(state: String, myUmrahEntity: MyUmrahEntity, position: Int) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_PROMO_CLICK
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_WIDGET_MY_UMRAH
        map[TrackAppUtils.EVENT_LABEL] = state
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PROMO_CLICK, DataLayer.mapOf(
                PROMOTIONS_LABEL, getListMyUmrahEntity(myUmrahEntity, position)
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahImpressionCategoryTracker(umrahCategories: UmrahCategories, position: Int) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_PROMO_VIEW
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_IMPRESSION_CATEGORY
        map[TrackAppUtils.EVENT_LABEL] = ""
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PRODUCT_VIEW, DataLayer.mapOf(
                PROMOTIONS_LABEL, getListCategoriEntity(umrahCategories, position)
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahClickCategoryTracker(umrahCategories: UmrahCategories, position: Int) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_PROMO_CLICK
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_CATEGORY
        map[TrackAppUtils.EVENT_LABEL] = ""
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PROMO_CLICK, DataLayer.mapOf(
                PROMOTIONS_LABEL, getListCategoriEntity(umrahCategories, position)
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahImpressionFeaturedCategoryTracker(headerTitle: String, product: Products, position: Int, positionDC: Int) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_PRODUCT_VIEW
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_IMPRESSION_DYNAMIC_CONTENT
        map[TrackAppUtils.EVENT_LABEL] = headerTitle
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_CODE, CURRENCY,
                IMPRESSION, getListCategoryFeaturedImpression("", product, position, positionDC,headerTitle)
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahClickFeaturedCategoryTracker(headerTitle: String, positionDC: Int, products: Products, position: Int) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_PRODUCT_CLICK
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_DYNAMIC_CONTENT
        map[TrackAppUtils.EVENT_LABEL] = headerTitle
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CLICK, DataLayer.mapOf(
                ACTION_FIELD, DataLayer.mapOf(
                LIST, "$UMRAH_CATEGORY_HOME_PAGE - $DYNAMIC_CONTENT ${positionDC+1} - $headerTitle"
        ),
                PRODUCTS_LABEL, DataLayer.mapOf(
                NAME, products.title,
                ID, products.id,
                PRICE, products.originalPrice,
                CATEGORY, "",
                POSITION, position + 1
        )
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getListMyUmrahEntity(myUmrahEntity: MyUmrahEntity, position: Int): List<Any> {
        val list = ArrayList<Map<String, Any>>()

        val map = HashMap<String, Any>()
        map[ID] = position + 1
        map[NAME] = "$UMRAH_CATEGORY_HOME_PAGE - $WIDGET_MY_UMRAH"
        map[CREATIVE] = myUmrahEntity.header
        map[POSITION] = position + 1

        list.add(map)

        return DataLayer.listOf(*list.toTypedArray<Any>())

    }

    private fun getListDanaImpian(): List<Any> {
        val list = ArrayList<Map<String, Any>>()

        val map = HashMap<String, Any>()
        map[ID] = POSITION_DEFAULT
        map[NAME] = "$UMRAH_CATEGORY_HOME_PAGE - $DREAM_FUND"
        map[CREATIVE] = MY_UMRAH_AND_DREAM_FUND_CREATIVE
        map[POSITION] = POSITION_DEFAULT

        list.add(map)

        return DataLayer.listOf(*list.toTypedArray<Any>())

    }

    private fun getListCategoriEntity(umrahCategories: UmrahCategories, position: Int): List<Any> {
        val list = ArrayList<Map<String, Any>>()

        val map = HashMap<String, Any>()

        map[ID] = umrahCategories.id
        map[NAME] = "$UMRAH_CATEGORY_HOME_PAGE - $KATEGORI"
        map[CREATIVE] = umrahCategories.coverImageURL
        map[POSITION] = position + 1

        list.add(map)

        return DataLayer.listOf(*list.toTypedArray<Any>())

    }

    private fun getListCategoryFeaturedImpression(categoryName: String, product: Products, position: Int,
                                                  positionDC: Int, headerTitle: String): List<Any> {
        val list = ArrayList<Map<String, Any>>()

        val map = HashMap<String, Any>()
        map[ID] = product.id
        map[NAME] = product.title
        map[PRICE] = product.originalPrice
        map[POSITION] = position + 1
        map[CATEGORY] = categoryName
        map[LIST] = "$UMRAH_CATEGORY_HOME_PAGE - $DYNAMIC_CONTENT ${positionDC + 1 } - $headerTitle"

        list.add(map)

        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun getListDetailPilgrimsCheckoutTracker() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_CLICK_UMROH
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CHECKOUT_CATEGORY
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_CHECKOUT_LIST_DETAIL_PILGRIMS
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun getContactCustomerCheckoutTracker() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_CLICK_UMROH
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CHECKOUT_CATEGORY
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_CHECKOUT_CUSTOMER_CONTACT
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun getListPilgrimsCheckoutTracker() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_CLICK_UMROH
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CHECKOUT_CATEGORY
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_CHECKOUT_LIST_PILGRIMS
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun getPaymentTypeCheckoutTracker(paymentType: String) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_CLICK_UMROH
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CHECKOUT_CATEGORY
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_CHECKOUT_PAYMENT_TYPE
        map[TrackAppUtils.EVENT_LABEL] = paymentType

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun getInstallmentTypeCheckoutTracker(title: String) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_CLICK_UMROH
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CHECKOUT_CATEGORY
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_CHECKOUT_INSTALLMENT_TYPE
        map[TrackAppUtils.EVENT_LABEL] = title

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun getClickBackCheckoutTracker() {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = UMRAH_EVENT_CLICK_UMROH
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CHECKOUT_CATEGORY
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_BACK
        map[TrackAppUtils.EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun getCheckoutTracker(position: Int, product: UmrahProductModel.UmrahProduct, quantity: Int) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = CHECKOUT
        map[TrackAppUtils.EVENT_CATEGORY] = UMRAH_CLICK_CHECKOUT_CATEGORY
        map[TrackAppUtils.EVENT_ACTION] = UMRAH_CLICK_CHECKOUT_CHOOSE_PAYMENT
        map[TrackAppUtils.EVENT_LABEL] = ""
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CHECKOUT, DataLayer.mapOf(
                ACTION_FIELD, DataLayer.mapOf(
                STEP, position + 1,
                OPTION, UMRAH_CLICK_CHECKOUT_CHOOSE_PAYMENT),
                PRODUCTS, getListCheckoutProduct(product, quantity)
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getListCheckoutProduct(product: UmrahProductModel.UmrahProduct, quantity: Int): List<Any> {
        val list = ArrayList<Map<String, Any>>()

        val map = HashMap<String, Any>()
        product.apply {
            map[ID] = id
            map[NAME] = title
            map[PRICE] = originalPrice
            map[QUANTITY] = quantity
        }

        list.add(map)

        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    private fun getSearchNCategoryProductMap(product: UmrahSearchProduct, index: Int, sort: String, filter: ParamFilter): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[NAME] = product.title
        map[ID] = product.id
        map[PRICE] = product.originalPrice
        map[CATEGORY] = product.slugName
        map[POSITION] = index + 1
        map[DIMENSION110] = "$SORT: ${sort}_" +
                "$FILTER: " +
                getFilter(filter)
        return map
    }

    private fun getFilter(filter: ParamFilter): Any? {
        return "$BULAN_KEBERANGKATAN - ${filter.departurePeriod}_" +
                "$KOTA_KEBERANGKATAN - ${filter.departureCity}_" +
                "$DURASI_PERJALANAN - ${filter.durationDaysMinimum}-${filter.durationDaysMaximum}_" +
                "$HARGA_PAKET - ${filter.priceMinimum}-${filter.priceMaximum}"
    }

    private fun getUmrahSearchNCategoryProductLists(product: UmrahSearchProduct, index: Int, sort: String, filter: ParamFilter, searchOrCategory: SearchOrCategory): Any? {
        val list = ArrayList<Map<String, Any>>()
        val map = getSearchNCategoryProductMap(product, index, sort, filter)
        map[LIST] = searchOrCategory.getEventCategory()
        list.add(map)
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun umrahSearchNCategoryProductListImpression(products: UmrahSearchProduct, index: Int, sort: String, filter: ParamFilter, searchOrCategory: SearchOrCategory) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = PRODUCT_VIEW
        map[TrackAppUtils.EVENT_CATEGORY] = searchOrCategory.getEventCategory()
        map[TrackAppUtils.EVENT_ACTION] = IMPRESSION_PRODUCT
        map[TrackAppUtils.EVENT_LABEL] = EMPTY
        map[ECOMMERCE] = DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS, getUmrahSearchNCategoryProductLists(products, index, sort, filter, searchOrCategory)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getSearchNCategoryClickedProduct(product: UmrahSearchProduct, index: Int, sort: String, filter: ParamFilter): Any? {
        val list = ArrayList<Map<String, Any>>()
        val map = getSearchNCategoryProductMap(product, index, sort, filter)
        list.add(map)
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun umrahSearchNCategoryProductClick(product: UmrahSearchProduct, index: Int, sort: String, filter: ParamFilter, searchOrCategory: SearchOrCategory) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = PRODUCT_CLICK
        map[TrackAppUtils.EVENT_CATEGORY] = searchOrCategory.getEventCategory()
        map[TrackAppUtils.EVENT_ACTION] = CLICK_PRODUCT
        map[TrackAppUtils.EVENT_LABEL] = EMPTY
        map[ECOMMERCE] = DataLayer.mapOf(
                CLICK, DataLayer.mapOf(
                ACTION_FIELD, DataLayer.mapOf(LIST, searchOrCategory.getEventCategory()),
                PRODUCTS, getSearchNCategoryClickedProduct(product, index, sort, filter))
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahSearchNCategorySortClick(sort: String, searchOrCategory: SearchOrCategory) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = CLICK_UMROH
        map[TrackAppUtils.EVENT_CATEGORY] = searchOrCategory.getEventCategory()
        map[TrackAppUtils.EVENT_ACTION] = CLICK_SORT
        map[TrackAppUtils.EVENT_LABEL] = sort
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    @Suppress("UNUSED_PARAMETER")
    fun umrahSearchNCategoryFilterClick(paramFilter: ParamFilter, searchOrCategory: SearchOrCategory) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = CLICK_UMROH
        map[TrackAppUtils.EVENT_CATEGORY] = searchOrCategory.getEventCategory()
        map[TrackAppUtils.EVENT_ACTION] = CLICK_FILTER
        map[TrackAppUtils.EVENT_LABEL] = getFilter(paramFilter)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahSearchNCategoryBackClick(searchOrCategory: SearchOrCategory) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = CLICK_UMROH
        map[TrackAppUtils.EVENT_CATEGORY] = searchOrCategory.getEventCategory()
        map[TrackAppUtils.EVENT_ACTION] = CLICK_BACK_CONST
        map[TrackAppUtils.EVENT_LABEL] = EMPTY
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahPdpAllClick(userAction: UmrahPdpTrackingUserAction) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = CLICK_UMROH
        map[TrackAppUtils.EVENT_CATEGORY] = PRODUCT_DETAIL_PAGE_UMROH
        map[TrackAppUtils.EVENT_ACTION] = userAction.getEventAction()
        map[TrackAppUtils.EVENT_LABEL] = EMPTY
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getPurchasedProductsNext(paramPurchase: ParamPurchase): Any? {
        val list = ArrayList<Map<String, Any>>()
        val map = HashMap<String, Any>()
        map[NAME] = paramPurchase.name
        map[ID] = paramPurchase.id
        map[PRICE] = paramPurchase.price
        map[CATEGORY] = paramPurchase.slugName
        map[VARIANT] = paramPurchase.variant
        map[QUANTITY] = paramPurchase.totalPassenger
        map[SHOP_ID] = paramPurchase.shopId
        map[SHOP_NAME] = paramPurchase.shopName
        map[CATEGORY_ID] = paramPurchase.categoryId
        list.add(map)
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun umrahPdpPurchaseProductsNext(paramPurchase: ParamPurchase) {
        val map = mutableMapOf<String, Any?>()
        map[TrackAppUtils.EVENT] = ADD_TO_CART
        map[TrackAppUtils.EVENT_CATEGORY] = PRODUCT_DETAIL_PAGE_UMROH
        map[TrackAppUtils.EVENT_ACTION] = CLICK_LANJUTKAN_BELI_PAKET
        map[TrackAppUtils.EVENT_LABEL] = EMPTY
        map[ECOMMERCE] = DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                ADD, DataLayer.mapOf(
                PRODUCTS, getPurchasedProductsNext(paramPurchase))
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }
}