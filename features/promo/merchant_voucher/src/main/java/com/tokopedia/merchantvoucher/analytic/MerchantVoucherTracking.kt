package com.tokopedia.merchantvoucher.analytic

import java.util.HashMap
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.util.BaseTrackerConst


class MerchantVoucherTracking : BaseTrackerConst(){
    companion object{
        private const val CLICK_PDP = "clickPDP"
        private const val PDP = "product detail page"
        private const val CLICK_MVC_DETAIL = "click - mvc list - mvc detail"
        private const val CLICK_MVC_USE_VOUCHER = "click - mvc list - use voucher"
        private const val CLICK_MVC_SHARE = "click - mvc list - share - whatsapp"
        private const val CLICK_USE_VOUCHER = "click - mvc list - mvc detail - use voucher"
        private const val PROMO_ID = "promoId"
    }

    private fun createMap(event : String = "", category: String = "", action : String = "", label:String="") : HashMap<String , Any>{
        val eventMap: HashMap<String, Any> = HashMap()
        eventMap[Event.KEY]=event
        eventMap[Category.KEY]= category
        eventMap[Action.KEY]=action
        eventMap[Label.KEY]=label
        return eventMap
    }

    fun clickMvcDetailFromList(voucherId: String="") {
        val eventMap: HashMap<String, Any> =
            createMap(CLICK_PDP, PDP, CLICK_MVC_DETAIL, "")
        eventMap[PROMO_ID] = voucherId
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    fun clickUseVoucherFromList(voucherId: String="") {
        val eventMap: HashMap<String, Any> =
            createMap(CLICK_PDP, PDP, CLICK_MVC_USE_VOUCHER, "")
        eventMap[PROMO_ID] = voucherId
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }


    fun clickUseVoucherFromDetail(voucherId: String = "") {
        val eventMap:HashMap<String, Any> =
            createMap(CLICK_PDP, PDP, CLICK_USE_VOUCHER, "")
        eventMap[PROMO_ID] = voucherId
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    fun clickShare() {
        val eventMap: HashMap<String, Any> =
            createMap(CLICK_PDP, PDP, CLICK_MVC_SHARE, "")
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

}
