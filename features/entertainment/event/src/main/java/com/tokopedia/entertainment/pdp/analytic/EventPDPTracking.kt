package com.tokopedia.entertainment.pdp.analytic

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.entertainment.common.util.CommonTrackingEvent
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.CATEGORY_ID
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.CHECKOUT_OPTION
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.CHECKOUT_STEP
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.EVENT_VALUE_ATC
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.Misc.EVENT_VALUE_CHECKOUT_PROGRESS
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
import com.tokopedia.entertainment.common.util.CommonTrackingEvent.addGeneralCheckoutProgress
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

    fun onViewCheckoutPage(productDetailData: ProductDetailData,items: List<ItemMapResponse>,
                           userId: String
    ){
        val title = productDetailData.category.firstOrNull()?.title
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "view checkout")
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s -  %s", title, productDetailData.displayName))
            putString(CHECKOUT_OPTION, "cart page loaded")
            putString(CHECKOUT_STEP, "1")
            putParcelableArrayList(ITEMS, getProductFromMetaData(items,title))
        }
        eventDataLayer.addGeneralCheckoutProgress(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_CHECKOUT_PROGRESS, eventDataLayer)
    }

    fun onClickCheckoutButton(productDetailData: ProductDetailData, items: List<ItemMapResponse>,
                              userId: String){
        val title = productDetailData.category.firstOrNull()?.title
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, "click proceed payment")
            putString(TrackAppUtils.EVENT_LABEL, String.format("%s -  %s", title, productDetailData.displayName))
            putString(CHECKOUT_OPTION, "click payment option button")
            putString(CHECKOUT_STEP, "2")
            putParcelableArrayList(ITEMS, getProductFromMetaData(items,title))
        }
        eventDataLayer.addGeneralCheckoutProgress(userId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VALUE_CHECKOUT_PROGRESS, eventDataLayer)
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

    private fun getProductFromMetaData(items: List<ItemMapResponse>, category: String?): ArrayList<Bundle>{
        var list = arrayListOf<Bundle>()
        items.forEachIndexed { index, it ->
            val itemBundle = Bundle().apply {
                putString(CATEGORY_ID, "")
                putString(ITEM_BRAND, "")
                putString(ITEM_CATEGORY, category)
                putString(ITEM_ID, it.productId)
                putString(ITEM_NAME, it.name)
                putString(ITEM_VARIANT, it.id)
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

}