package com.tokopedia.entertainment.pdp.analytic

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.entertainment.pdp.data.*
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
                Other.CURRENTSITE, "tokopediadigitalevents",
                Other.BUSINESSUNIT, "travel & entertainment",
                Other.CATEGORY, "events"
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
                Other.CURRENTSITE, "tokopediadigitalevents",
                Other.BUSINESSUNIT, "travel & entertainment",
                Other.CATEGORY, "events"
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
                Other.CURRENTSITE, "tokopediadigitalevents",
                Other.BUSINESSUNIT, "travel & entertainment",
                Other.CATEGORY, "events"
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
                Other.CURRENTSITE, "tokopediadigitalevents",
                Other.BUSINESSUNIT, "travel & entertainment",
                Other.CATEGORY, "events"
        ))
    }

    fun onClickPesanTiket(category: Category, product_name: String, product_id: String, product_price: String, qty: Int, package_id: String){
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "addToCart",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "click beli",
                Event.LABEL, String.format("%s -  %s", category.title, product_name),
                Other.SCREENNAME, "",
                Other.CLIENTID, userSession.deviceId,
                Other.SESSIONIRIS, irisSession.getSessionId(),
                Other.USERID, userSession.userId,
                Other.CURRENTSITE, "tokopediadigitalevents",
                Other.BUSINESSUNIT, "travel & entertainment",
                Other.CATEGORY, "events",
                Ecommerce.KEY, DataLayer.mapOf(
                Ecommerce.CURRENCY_CODE, "IDR",
                Ecommerce.ADD, DataLayer.mapOf(
                Product.KEY, DataLayer.listOf(
                DataLayer.mapOf(
                        Product.NAME, product_name,
                        Product.ID, product_id,
                        Product.PRICE, (product_price.toInt() * qty),
                        Product.BRAND, "",
                        Product.CATEGORY, category.title,
                        Product.VARIANT, package_id,
                        Product.QUANTITY, qty.toString(),
                        Product.SHOPID, "",
                        Product.SHOPTYPE, "",
                        Product.SHOPNAME, "",
                        Product.CATEGORY_ID, category.id
                ))))))
    }

    fun onViewCheckoutPage(mPackage: PackageV3, productDetailData: ProductDetailData, qty: Int){
        val title = productDetailData.category.firstOrNull()?.title
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "checkout",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "view checkout",
                Event.LABEL, String.format("%s -  %s", title , mPackage.name),
                Other.SCREENNAME, "digital/events/summary",
                Other.CLIENTID, userSession.deviceId,
                Other.SESSIONIRIS, irisSession.getSessionId(),
                Other.USERID, userSession.userId,
                Other.CURRENTSITE, "tokopediadigitalevents",
                Other.BUSINESSUNIT, "travel & entertainment",
                Other.CATEGORY, "events",
                Ecommerce.KEY, DataLayer.mapOf(
                Ecommerce.CHECKOUT, DataLayer.mapOf(
                ActionField.KEY,DataLayer.mapOf(
                ActionField.STEP, "1",
                ActionField.OPTION, "cart page loaded"
        ),
                Product.KEY, DataLayer.listOf(
                DataLayer.mapOf(
                        Product.NAME, mPackage.name,
                        Product.ID, mPackage.id,
                        Product.PRICE, (mPackage.salesPrice.toInt() * qty),
                        Product.BRAND, "",
                        Product.CATEGORY, "",
                        Product.VARIANT, mPackage.id,
                        Product.QUANTITY, qty.toString(),
                        Product.SHOPID, "",
                        Product.SHOPTYPE, "",
                        Product.SHOPNAME, ""
                ))))))
    }

    fun onClickCheckoutButton(mPackage: PackageV3, productDetailData: ProductDetailData, qty: Int){
        val title = productDetailData.category.firstOrNull()?.title
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                Event.KEY, "checkout",
                Event.CATEGORY, "digital - event",
                Event.ACTION, "clicked proceed payment",
                Event.LABEL, String.format("%s -  %s", title, mPackage.name),
                Other.SCREENNAME, "digital/events/summary",
                Other.CLIENTID, userSession.deviceId,
                Other.SESSIONIRIS, irisSession.getSessionId(),
                Other.USERID, userSession.userId,
                Other.CURRENTSITE, "tokopediadigitalevents",
                Other.BUSINESSUNIT, "travel & entertainment",
                Other.CATEGORY, "events",
                Ecommerce.KEY, DataLayer.mapOf(
                Ecommerce.CHECKOUT, DataLayer.mapOf(
                ActionField.KEY,DataLayer.mapOf(
                ActionField.STEP, "2",
                ActionField.OPTION, "click payment option button"
        ),
                Product.KEY, DataLayer.listOf(
                DataLayer.mapOf(
                        Product.NAME, mPackage.name,
                        Product.ID, mPackage.id,
                        Product.PRICE, (mPackage.salesPrice.toInt() * qty),
                        Product.BRAND, "",
                        Product.CATEGORY, title,
                        Product.VARIANT, mPackage.id,
                        Product.QUANTITY, qty.toString(),
                        Product.SHOPID, "",
                        Product.SHOPTYPE, "",
                        Product.SHOPNAME, ""
                ))))))
    }

    private fun getTracker() : Analytics {
        return TrackApp.getInstance().gtm
    }

}