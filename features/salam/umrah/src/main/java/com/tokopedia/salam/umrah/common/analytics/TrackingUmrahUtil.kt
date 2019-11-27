package com.tokopedia.salam.umrah.common.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.homepage.data.Products
import com.tokopedia.salam.umrah.homepage.data.UmrahCategories
import com.tokopedia.salam.umrah.homepage.data.UmrahCategoriesFeatured
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*

/**
 * @author by furqan on 08/10/2019
 */
class TrackingUmrahUtil {

    fun umrahOrderDetailInvoice() {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_CLICK_EVENT
        map[EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[EVENT_ACTION] = UMRAH_CLICK_ACTION_INVOICE
        map[EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }


    fun umrahOrderDetailLihatDetail() {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_CLICK_EVENT
        map[EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL
        map[EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailUmrahSaya(state: String){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_CLICK_EVENT
        map[EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_WIDGET_UMROH_SAYA
        map[EVENT_LABEL] = state

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailDetailPDP(){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_CLICK_EVENT
        map[EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_PDP
        map[EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailDetailEVoucher(){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_CLICK_EVENT
        map[EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_E_VOUCHER
        map[EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailKebijakanPembatalan(){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_CLICK_EVENT
        map[EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_KEBIJAKAN_PEMBATALAN
        map[EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailBatalkanPesanan(){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_CLICK_EVENT
        map[EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_BATALKAN_PESANAN
        map[EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailBantuan(){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_CLICK_EVENT
        map[EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_BANTUAN
        map[EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahOrderDetailBack(){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_CLICK_EVENT
        map[EVENT_CATEGORY] = UMRAH_CLICK_CATEGORY_ORDER_DETAIL
        map[EVENT_ACTION] = UMRAH_CLICK_ACTION_ORDER_DETAIL_BACK
        map[EVENT_LABEL] = ""

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahSearchProductTracker(departPeriod: String, departCity: String, departPrice: String) {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_EVENT_CLICK_UMROH
        map[EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[EVENT_ACTION] = UMRAH_CLICK_SEARCH_PACKET_UMRAH
        map[EVENT_LABEL] = "$DEPART_MONTH$departPeriod$PRICE_TRACKING$departPrice$DEPART_CITY$departCity"

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahImpressionUmrahSayaTracker(headerTitle: String, myUmrahEntity: MyUmrahEntity, position: Int){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_EVENT_PROMO_VIEW
        map[EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[EVENT_ACTION] = UMRAH_IMPRESSION_WIDGET_MY_UMRAH
        map[EVENT_LABEL] = headerTitle
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PROMO_VIEW, DataLayer.mapOf(
                PROMOTIONS_LABEL, getListMyUmrahEntity(myUmrahEntity, position)
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahImpressionDanaImpianTracker(){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_EVENT_PROMO_VIEW
        map[EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[EVENT_ACTION] = UMRAH_IMPRESSION_DREAM_FUND
        map[EVENT_LABEL] = ""
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PROMO_VIEW, DataLayer.mapOf(
                PROMOTIONS_LABEL,getListDanaImpian()
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahClickDanaImpianTracker(){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_EVENT_PROMO_CLICK
        map[EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[EVENT_ACTION] = UMRAH_CLICK_DREAM_FUND
        map[EVENT_LABEL] = ""
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PROMO_CLICK, DataLayer.mapOf(
                PROMOTIONS_LABEL, getListDanaImpian()
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)

    }

    fun umrahClickUmrahSayaTracker(state: String, myUmrahEntity: MyUmrahEntity, position: Int){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_EVENT_PROMO_CLICK
        map[EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[EVENT_ACTION] = UMRAH_CLICK_WIDGET_MY_UMRAH
        map[EVENT_LABEL] = "$state"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PROMO_CLICK, DataLayer.mapOf(
                PROMOTIONS_LABEL, getListMyUmrahEntity(myUmrahEntity, position)
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahImpressionCategoryTracker(umrahCategories: UmrahCategories, position: Int){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_EVENT_PROMO_VIEW
        map[EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[EVENT_ACTION] = UMRAH_IMPRESSION_CATEGORY
        map[EVENT_LABEL] = ""
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PRODUCT_VIEW, DataLayer.mapOf(
                PROMOTIONS_LABEL,getListCategoriEntity(umrahCategories, position)
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahClickCategoryTracker(umrahCategories: UmrahCategories, position: Int){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_EVENT_PROMO_CLICK
        map[EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[EVENT_ACTION] = UMRAH_CLICK_CATEGORY
        map[EVENT_LABEL] = ""
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                UMRAH_EVENT_PROMO_CLICK, DataLayer.mapOf(
                PROMOTIONS_LABEL, getListCategoriEntity(umrahCategories, position)
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahImpressionFeaturedCategoryTracker(headerTitle : String, element: UmrahCategoriesFeatured){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_EVENT_PRODUCT_VIEW
        map[EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[EVENT_ACTION] = UMRAH_IMPRESSION_DYNAMIC_CONTENT
        map[EVENT_LABEL] = "$headerTitle"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_CODE, CURRENCY,
                IMPRESSION, getListCategoryFeaturedImpression("", element.products)
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun umrahClickFeaturedCategoryTracker(headerTitle: String, positionDC : Int, products: Products, position: Int){
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = UMRAH_EVENT_PRODUCT_CLICK
        map[EVENT_CATEGORY] = UMRAH_CATEGORY_HOME_PAGE
        map[EVENT_ACTION] = UMRAH_CLICK_DYNAMIC_CONTENT
        map[EVENT_LABEL] = "$headerTitle"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CLICK, DataLayer.mapOf(
                ACTION_FIELD, DataLayer.mapOf(
                LIST, "$UMRAH_CATEGORY_HOME_PAGE - $DYNAMIC_CONTENT $positionDC - $headerTitle"
        ),
                PRODUCTS_LABEL, DataLayer.mapOf(
                NAME, products.title,
                ID, products.id,
                PRICE, products.originalPrice,
                CATEGORY, "",
                POSITION, position
        )
        )
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getListMyUmrahEntity(myUmrahEntity: MyUmrahEntity, position: Int): List<Any>{
        val list = ArrayList<Map<String, Any>>()

        val map = HashMap<String, Any>()
        map[ID] = position
        map[NAME]  = "$UMRAH_CATEGORY_HOME_PAGE - $WIDGET_MY_UMRAH"
        map[CREATIVE] = myUmrahEntity.header
        map[POSITION] = position

        list.add(map)

        return DataLayer.listOf(*list.toTypedArray<Any>())

    }

    private fun getListDanaImpian(): List<Any>{
        val list = ArrayList<Map<String, Any>>()

        val map = HashMap<String, Any>()
        map[ID] = POSITION_DEFAULT
        map[NAME]  = "$UMRAH_CATEGORY_HOME_PAGE - $DREAM_FUND"
        map[CREATIVE] = ""
        map[POSITION] = POSITION_DEFAULT

        list.add(map)

        return DataLayer.listOf(*list.toTypedArray<Any>())

    }

    private fun getListCategoriEntity(umrahCategories: UmrahCategories, position: Int): List<Any>{
        val list = ArrayList<Map<String, Any>>()

        val map = HashMap<String, Any>()

        map[ID] = umrahCategories.id
        map[NAME]  = "$UMRAH_CATEGORY_HOME_PAGE - $KATEGORI"
        map[CREATIVE] = umrahCategories.coverImageURL
        map[POSITION] = position

        list.add(map)

        return DataLayer.listOf(*list.toTypedArray<Any>())

    }

    private fun getListCategoryFeaturedImpression(categoryName:String, items: List<Products>):List<Any>{
        val featuredCategories = mutableListOf<Any>()
        for ((index, item) in items.withIndex()) {
            featuredCategories.add(DataLayer.mapOf(
                    ID, item.id,
                    NAME,item.title ,
                    PRICE, item.originalPrice,
                    CATEGORY, categoryName,
                    POSITION, index
            ))
        }
        return featuredCategories
    }

}