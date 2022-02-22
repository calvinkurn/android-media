package com.tokopedia.vouchercreation.common.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import javax.inject.Inject


class CouponPreviewTracker @Inject constructor() {
    
    companion object {
        private const val EMPTY_STRING = ""
    }
    
    fun sendCreationPageImpression() {
        sendGeneralTracking(
            event = "viewPGIris",
            action = "impression creation page",
            category = "mvc locked product - creation",
            label = EMPTY_STRING
        )
    }

    fun sendChangeCouponInformationClickEvent() {
        sendGeneralTracking(
            event = "clickPG",
            action = "click ubah informasi kupon",
            category = "mvc locked product - creation",
            label = EMPTY_STRING
        )
    }

    fun sendChangeCouponSettingClickEvent() {
        sendGeneralTracking(
            event = "clickPG",
            action = "click ubah pengaturan kupon",
            category = "mvc locked product - creation",
            label = EMPTY_STRING
        )
    }

    fun sendAddProductClickEvent() {
        sendGeneralTracking(
            event = "clickPG",
            action = "click tambah produk",
            category = "mvc locked product - creation",
            label = EMPTY_STRING
        )
    }

    fun sendBackToPreviousPageEvent() {
        sendGeneralTracking(
            event = "clickPG",
            action = "click back",
            category = "mvc locked product - creation",
            label = EMPTY_STRING
        )
    }

    fun sendCreateCouponEvent() {
        sendGeneralTracking(
            event = "clickPG",
            action = "click buat kupon",
            category = "mvc locked product - creation",
            label = EMPTY_STRING
        )
    }
    
    private fun sendGeneralTracking(
        event: String,
        action: String,
        category: String,
        label: String,
        businessUnit: String = VoucherCreationAnalyticConstant.Values.PHYSICAL_GOODS,
        currentSite: String = VoucherCreationAnalyticConstant.Values.TOKOPEDIA_SELLER
    ) {

        val payload = mapOf(
            VoucherCreationAnalyticConstant.Key.EVENT to event,
            VoucherCreationAnalyticConstant.Key.EVENT_ACTION to action,
            VoucherCreationAnalyticConstant.Key.EVENT_CATEGORY to category,
            VoucherCreationAnalyticConstant.Key.EVENT_LABEL to label,
            VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to businessUnit,
            VoucherCreationAnalyticConstant.Key.CURRENT_SITE to currentSite
        )


        TrackApp.getInstance().gtm.sendGeneralEvent(payload)
    }

}