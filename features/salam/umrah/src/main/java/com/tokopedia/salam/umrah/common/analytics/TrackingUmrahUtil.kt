package com.tokopedia.salam.umrah.common.analytics

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

}