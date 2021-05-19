package com.tokopedia.entertainment.pdp.analytic

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.entertainment.common.util.CommonTrackingEvent
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.CATEGORY_ID
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.EVENT_VALUE_ATC
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEMS
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEM_BRAND
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEM_CATEGORY
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEM_ID
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEM_NAME
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.ITEM_VARIANT
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.PRICE
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.QUANTITY
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.SHOP_ID
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.SHOP_NAME
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.SHOP_TYPE
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.addGeneralATCBundle
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.addGeneralClick
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.addGeneralImpression
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking
import com.tokopedia.entertainment.pdp.data.*
import com.tokopedia.entertainment.pdp.data.pdp.ItemMap
import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class EventPDPTracking constructor(val userSession: UserSessionInterface, val irisSession: IrisSession) {

    private object Event {
        val KEY = "event"
        val CATEGORY = "eventCategory"
        val ACTION = "eventAction"
        val LABEL = "eventLabel"
    }

    private object Other{
        val SCREENNAME = "screenName"
        val CLIENTID = "clientId"
        val SESSIONIRIS = "sessionIris"
        val USERID = "userId"
        val CURRENTSITE = "currentSite"
        val BUSINESSUNIT = "businessUnit"
        val CATEGORY = "category"

        val CURRENTSITEDATA =  "tokopediadigitalevents"
        val BUSINESSUNITDATA = "travel & entertainment"
        val CATEGORYDATA = "events"
    }

    private object Ecommerce {
        val KEY = "ecommerce"
        val CURRENCY_CODE = "currencyCode"
        val ADD = "add"
        val CHECKOUT = "checkout"
    }

    private object Product {
        val KEY = "products"
        val NAME = "name"
        val ID = "id"
        val PRICE = "price"
        val BRAND = "brand"
        val CATEGORY = "category"
        val VARIANT = "variant"
        val QUANTITY = "quantity"
        val SHOPID = "shop_id"
        val SHOPTYPE = "shop_type"
        val SHOPNAME = "shop_name"
        val CATEGORY_ID = "category_id"
    }

    private object ActionField{
        val KEY = "actionField"
        val STEP = "step"
        val OPTION = "option"
    }

    fun onClickCariTicket(productDetailData: ProductDetailData){
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click lanjutkan",
                TrackAppUtils.EVENT_LABEL, String.format("%s", productDetailData.displayName)
        )
        data.addGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun onClickPickDate(){
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "pick date",
                TrackAppUtils.EVENT_LABEL, ""
        )
        data.addGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun onClickPackage(mPackage: PackageItem, qty: Int){
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click package",
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", mPackage.name, qty.toString())
        )
        data.addGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun onClickQuantity(){
        val data = DataLayer.mapOf(
                TrackAppUtils.EVENT_ACTION, "click quantity",
                TrackAppUtils.EVENT_LABEL, ""
        )
        data.addGeneralClick()
        getTracker().sendGeneralEvent(data)
    }

    fun onClickPesanTiket(category: Category, package_id: String,
                          listItems:List<ItemMap>, userId: String){
        val product_name = listItems.firstOrNull()?.productName
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "click beli")
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s -  %s", category.title, product_name))
            putParcelableArrayList(ITEMS, getProductsFromItemMap(listItems,category,package_id))
        }
        eventDataLayer.addGeneralATCBundle(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_ATC, eventDataLayer)
    }

    fun onViewCheckoutPage(productDetailData: ProductDetailData,items: List<ItemMapResponse>){
        val title = productDetailData.category.firstOrNull()?.title
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "checkout",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "view checkout",
                Event.LABEL, String.format("%s -  %s", title, productDetailData.displayName),
                Other.SCREENNAME, "digital/events/summary",
                Other.CLIENTID, userSession.deviceId,
                Other.SESSIONIRIS, irisSession.getSessionId(),
                Other.USERID, userSession.userId,
                Other.CURRENTSITE, Other.CURRENTSITEDATA,
                Other.BUSINESSUNIT, Other.BUSINESSUNITDATA,
                Other.CATEGORY, Other.CATEGORYDATA,
                Ecommerce.KEY, DataLayer.mapOf(
                Ecommerce.CHECKOUT, DataLayer.mapOf(
                ActionField.KEY,DataLayer.mapOf(
                ActionField.STEP, "1",
                ActionField.OPTION, "cart page loaded"
        ),
                Product.KEY, getProductFromMetaData(items,title)))))
    }

    fun onClickCheckoutButton(productDetailData: ProductDetailData, items: List<ItemMapResponse>){
        val title = productDetailData.category.firstOrNull()?.title
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "checkout",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "clicked proceed payment",
                Event.LABEL, String.format("%s -  %s", title, productDetailData.displayName),
                Other.SCREENNAME, "digital/events/summary",
                Other.CLIENTID, userSession.deviceId,
                Other.SESSIONIRIS, irisSession.getSessionId(),
                Other.USERID, userSession.userId,
                Other.CURRENTSITE, Other.CURRENTSITEDATA,
                Other.BUSINESSUNIT, Other.BUSINESSUNITDATA,
                Other.CATEGORY, Other.CATEGORYDATA,
                Ecommerce.KEY, DataLayer.mapOf(
                Ecommerce.CHECKOUT, DataLayer.mapOf(
                ActionField.KEY,DataLayer.mapOf(
                ActionField.STEP, "2",
                ActionField.OPTION, "click payment option button"
        ),
                Product.KEY, getProductFromMetaData(items, title)))))
    }

    private fun getTracker() : Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun getProductsFromItemMap(items: List<ItemMap>, category:Category, packageID:String): ArrayList<Bundle> {
        val list = arrayListOf<Bundle>()
        items.forEachIndexed { index, it ->
            val itemBundle = Bundle().apply {
                putString(CATEGORY_ID, category.id)
                putString(ITEM_BRAND, "")
                putString(ITEM_CATEGORY, category.title)
                putString(ITEM_ID, it.productId)
                putString(ITEM_NAME, it.name)
                putString(ITEM_NAME, it.name)
                putString(ITEM_VARIANT, packageID)
                putString(PRICE,  (it.price * it.quantity).toString())
                putString(QUANTITY, it.quantity.toString())
                putString(SHOP_ID, "")
                putString(SHOP_NAME, "")
                putString(SHOP_TYPE, "")

            }
            list.add(itemBundle)
        }
        return list
    }

    private fun getProductFromMetaData(items: List<ItemMapResponse>, category: String?): Any?{
        var list = mutableListOf<Any>()
        items.forEachIndexed { index, it ->
            list.add(DataLayer.mapOf(
                    Product.NAME, it.name,
                    Product.ID, it.id,
                    Product.PRICE, (it.price * it.quantity),
                    Product.BRAND, "",
                    Product.CATEGORY, category,
                    Product.VARIANT, it.id,
                    Product.QUANTITY, it.quantity.toString(),
                    Product.SHOPID, "",
                    Product.SHOPTYPE, "",
                    Product.SHOPNAME, ""
            ))
        }
        return list
    }

}