package com.tokopedia.gamification.pdp.data

import com.tokopedia.track.builder.Tracker

object GamificationAnalytics {

    fun sendDirectRewardLandingPageEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("viewLGIris")
            .setEventAction("direct reward landing page")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49399")
            .setBusinessUnit("gamification")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickBackOnNavBarEvent(eventLabel: String, businessUnit: String, currentSite: String) {
        Tracker.Builder()
            .setEvent("clickLG")
            .setEventAction("click back on nav bar")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49400")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .build()
            .send()
    }

    fun sendClickShareOnNavBarEvent(eventLabel: String, businessUnit: String, currentSite: String) {
        Tracker.Builder()
            .setEvent("clickCommunication")
            .setEventAction("click - share button")
            .setEventCategory("thr ketupat")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49192")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .build()
            .send()
    }

    fun sendImpressHeaderSectionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("viewLGIris")
            .setEventAction("impress header section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49402")
            .setBusinessUnit("gamification")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendImpressManualClaimSectionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("viewLGIris")
            .setEventAction("impress manual claim section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49403")
            .setBusinessUnit("gamification")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickClaimButtonEvent(eventLabel: String, businessUnit: String, currentSite: String) {
        Tracker.Builder()
            .setEvent("clickLG")
            .setEventAction("click claim button")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49404")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .build()
            .send()
    }

    fun sendClickCaraMainEvent(eventLabel: String, businessUnit: String, currentSite: String) {
        Tracker.Builder()
            .setEvent("clickLG")
            .setEventAction("click cara main")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49405")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .build()
            .send()
    }

    fun sendImpressReferralSectionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("viewLGIris")
            .setEventAction("impress referral section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49406")
            .setBusinessUnit("gamification")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickReferralSectionEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String
    ) {
        Tracker.Builder()
            .setEvent("clickLG")
            .setEventAction("click referral section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49407")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .build()
            .send()
    }

    fun sendImpressCouponWidgetByCategorySectionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("viewLGIris")
            .setEventAction("impress coupon widget by category section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49408")
            .setBusinessUnit("gamification")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickLihatSemuaInCouponWidgetByCategorySectionEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String
    ) {
        Tracker.Builder()
            .setEvent("clickLG")
            .setEventAction("click lihat semua in coupon widget by category section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49409")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .build()
            .send()
    }

    fun sendClickCouponInCouponWidgetByCategorySectionEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String
    ) {
        Tracker.Builder()
            .setEvent("clickLG")
            .setEventAction("click coupon in coupon widget by category section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49410")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .build()
            .send()
    }

    fun sendImpressCouponWidgetByCatalogSectionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("viewLGIris")
            .setEventAction("impress coupon widget by catalog section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49411")
            .setBusinessUnit("gamification")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickLihatSemuaInCouponWidgetByCatalogSectionEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String
    ) {
        Tracker.Builder()
            .setEvent("clickLG")
            .setEventAction("click lihat semua in coupon widget by catalog section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49412")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .build()
            .send()
    }

    fun sendClickCouponInCouponWidgetByCatalogSectionEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String
    ) {
        Tracker.Builder()
            .setEvent("clickLG")
            .setEventAction("click coupon in coupon widget by catalog section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49413")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .build()
            .send()
    }

    fun sendImpressFooterBannerSectionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("viewLGIris")
            .setEventAction("impress footer banner section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49414")
            .setBusinessUnit("gamification")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickBannersInFooterBannerSectionEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String
    ) {
        Tracker.Builder()
            .setEvent("clickLG")
            .setEventAction("click banners in footer banner section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49415")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .build()
            .send()
    }

    fun sendImpressProductRecommendationSectionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("viewLGIris")
            .setEventAction("impress product recommendation section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49416")
            .setBusinessUnit("gamification")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickProductInProductRecommendationSectionEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String
    ) {
        Tracker.Builder()
            .setEvent("clickLG")
            .setEventAction("click product in product recommendation section")
            .setEventCategory("direct reward landing page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "49417")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .build()
            .send()
    }
}
