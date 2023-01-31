package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class VoucherSettingTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickPromoTypeEvent(promoType: PromoType) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click tipe promo")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(promoType.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39407")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickTipePotonganEvent(benefitType: BenefitType) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click tipe potongan")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(benefitType.toEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39408")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldPersentaseCashbackEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field persentase cashback")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39409")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldMaximumCashbackEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field maksimum cashback")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39410")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldNominalCashbackEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field atur nominal")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39411")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldMinimumCashbackEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field minimum cashback")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39412")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldQuotaCashbackEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field kuota")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39413")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickTargetPembeliEvent(targetBuyer: VoucherTargetBuyer, promoType: PromoType) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click target pembeli")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(targetBuyer.toEventLabel(promoType))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39414")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickLanjutEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lanjut - third step")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39415")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickKembaliButtonEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali button - third step")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39416")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickKembaliArrowEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - third step")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39417")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}

private fun PromoType.toEventLabel(): String {
    return when (this) {
        PromoType.DISCOUNT -> "diskon"
        PromoType.FREE_SHIPPING -> "gratis ongkir"
        PromoType.CASHBACK -> "cashback"
    }
}

private fun BenefitType.toEventLabel(): String {
    return if (this == BenefitType.NOMINAL) {
        "nominal"
    } else {
        "persentase"
    }
}

private fun VoucherTargetBuyer.toEventLabel(promoType: PromoType): String {
    return when (this) {
        VoucherTargetBuyer.ALL_BUYER -> "target pembeli: semua pembeli - tipe promo: ${promoType.toEventLabel()}"
        else -> "target pembeli: pengikut baru - tipe promo: ${promoType.toEventLabel()}"
    }
}
