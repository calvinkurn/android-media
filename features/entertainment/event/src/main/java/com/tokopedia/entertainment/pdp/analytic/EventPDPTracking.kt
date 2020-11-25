package com.tokopedia.entertainment.pdp.analytic

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.entertainment.pdp.data.*
import com.tokopedia.entertainment.pdp.data.pdp.ItemMap
import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.track.TrackApp
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
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "clickEvent",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click lanjutkan",
                Event.LABEL, String.format("%s", productDetailData.displayName),
                Other.SCREENNAME, "",
                Other.CLIENTID, userSession.deviceId,
                Other.SESSIONIRIS, irisSession.getSessionId(),
                Other.USERID, userSession.userId,
                Other.CURRENTSITE, Other.CURRENTSITEDATA,
                Other.BUSINESSUNIT, Other.BUSINESSUNITDATA,
                Other.CATEGORY, Other.CATEGORYDATA
        ))
    }

    fun onClickPickDate(){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "clickEvent",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "pick date",
                Event.LABEL, "",
                Other.SCREENNAME, "",
                Other.CLIENTID, userSession.deviceId,
                Other.SESSIONIRIS, irisSession.getSessionId(),
                Other.USERID, userSession.userId,
                Other.CURRENTSITE, Other.CURRENTSITEDATA,
                Other.BUSINESSUNIT, Other.BUSINESSUNITDATA,
                Other.CATEGORY, Other.CATEGORYDATA
        ))
    }

    fun onClickPackage(mPackage: PackageItem, qty: Int){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "clickEvent",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click package",
                Event.LABEL, String.format("%s - %s", mPackage.name, qty.toString()),
                Other.SCREENNAME, "",
                Other.CLIENTID, userSession.deviceId,
                Other.SESSIONIRIS, irisSession.getSessionId(),
                Other.USERID, userSession.userId,
                Other.CURRENTSITE, Other.CURRENTSITEDATA,
                Other.BUSINESSUNIT, Other.BUSINESSUNITDATA,
                Other.CATEGORY, Other.CATEGORYDATA
        ))
    }

    fun onClickQuantity(){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "clickEvent",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click quantity",
                Event.LABEL, "",
                Other.SCREENNAME, "",
                Other.CLIENTID, userSession.deviceId,
                Other.SESSIONIRIS, irisSession.getSessionId(),
                Other.USERID, userSession.userId,
                Other.CURRENTSITE, Other.CURRENTSITEDATA,
                Other.BUSINESSUNIT, Other.BUSINESSUNITDATA,
                Other.CATEGORY, Other.CATEGORYDATA
        ))
    }

    fun onClickPesanTiket(category: Category, package_id: String, listItems:List<ItemMap>){
        val product_name = listItems.firstOrNull()?.productName
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "addToCart",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click beli",
                Event.LABEL, String.format("%s -  %s", category.title, product_name),
                Other.SCREENNAME, "",
                Other.CLIENTID, userSession.deviceId,
                Other.SESSIONIRIS, irisSession.getSessionId(),
                Other.USERID, userSession.userId,
                Other.CURRENTSITE, Other.CURRENTSITEDATA,
                Other.BUSINESSUNIT, Other.BUSINESSUNITDATA,
                Other.CATEGORY, Other.CATEGORYDATA,
                Ecommerce.KEY, DataLayer.mapOf(
                Ecommerce.CURRENCY_CODE, "IDR",
                Ecommerce.ADD, DataLayer.mapOf(
                Product.KEY, getProductsFromItemMap(listItems,category,package_id)))))
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

    private fun getProductsFromItemMap(items: List<ItemMap>, category:Category, packageID:String): Any? {
        var list = mutableListOf<Any>()
        items.forEachIndexed { index, it ->
            list.add(DataLayer.mapOf(
                    Product.NAME, it.name,
                    Product.ID, it.productId,
                    Product.PRICE, (it.price * it.quantity),
                    Product.BRAND, "",
                    Product.CATEGORY, category.title,
                    Product.VARIANT, packageID,
                    Product.QUANTITY, it.quantity.toString(),
                    Product.SHOPID, "",
                    Product.SHOPTYPE, "",
                    Product.SHOPNAME, "",
                    Product.CATEGORY_ID, category.id
            ))
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