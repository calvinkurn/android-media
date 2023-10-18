package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class VoucherSettingTracker @Inject constructor(private val userSession: UserSessionInterface) {

    companion object {
        private const val ZERO: Long = 0
    }

    fun sendClickPromoTypeEvent(promoType: PromoType, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click tipe promo")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(promoType.toEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39407")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickTipePotonganEvent(benefitType: BenefitType, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click tipe potongan")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(benefitType.toEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39408")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldPersentaseCashbackEvent(promoType: String, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field persentase $promoType")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(promoType.asPromoTypeEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39409")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldMaximumCashbackEvent(promoType: String, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field maksimum $promoType")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(promoType.asPromoTypeEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39410")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldNominalCashbackEvent(promoType: String, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field atur nominal")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(promoType.asPromoTypeEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39411")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldMinimumCashbackEvent(promoType: String, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field minimum $promoType")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(promoType.asPromoTypeEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39412")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFieldQuotaCashbackEvent(promoType: String, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click field kuota")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(promoType.asPromoTypeEventLabel(voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39413")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickTargetPembeliEvent(targetBuyer: VoucherTargetBuyer, promoType: PromoType, voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click target pembeli")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(targetBuyer.toEventLabel(promoType, voucherId))
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39414")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickLanjutEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lanjut - third step")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39415")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickKembaliButtonEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali button - third step")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39416")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickKembaliArrowEvent(voucherId: Long) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kembali arrow - third step")
            .setEventCategory(TrackerConstant.CreationVoucherSetting.event)
            .setEventLabel(voucherId.asEventLabel())
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39417")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    private fun Long.asEventLabel(): String {
        return if (this == ZERO) {
            "voucher_step: create - voucher_id: "
        } else {
            "voucher_step: edit - voucher_id: $this"
        }
    }

    private fun PromoType.toEventLabel(voucherId: Long): String {
        return when (this) {
            PromoType.DISCOUNT -> "diskon".asPromoTypeEventLabel(voucherId)
            PromoType.FREE_SHIPPING -> "gratis ongkir".asPromoTypeEventLabel(voucherId)
            PromoType.CASHBACK -> "cashback".asPromoTypeEventLabel(voucherId)
        }
    }

    private fun PromoType.toEventLabel(): String {
        return when (this) {
            PromoType.DISCOUNT -> "diskon"
            PromoType.FREE_SHIPPING -> "gratis ongkir"
            PromoType.CASHBACK -> "cashback"
        }
    }

    private fun BenefitType.toEventLabel(voucherId: Long): String {
        return if (this == BenefitType.NOMINAL) {
            "nominal".asTipePotonganEventLabel(voucherId)
        } else {
            "persentase".asTipePotonganEventLabel(voucherId)
        }
    }

    private fun String.asPromoTypeEventLabel(voucherId: Long): String {
        return if (voucherId == ZERO) {
            "voucher_step: create - voucher_id: - tipe_promo: $this"
        } else {
            "voucher_step: edit - voucher_id: $voucherId - tipe_promo: $this"
        }
    }

    private fun String.asTipePotonganEventLabel(voucherId: Long): String {
        return if (voucherId == ZERO) {
            "voucher_step: create - voucher_id: - tipe_potongan: $this"
        } else {
            "voucher_step: edit - voucher_id: $voucherId - tipe_potongan: $this"
        }
    }

    private fun String.asTargetBuyerEventLabel(voucherId: Long): String {
        return if (voucherId == ZERO) {
            "voucher_step: create - voucher_id: - $this"
        } else {
            "voucher_step: edit - voucher_id: $voucherId - $this"
        }
    }

    private fun VoucherTargetBuyer.toEventLabel(promoType: PromoType, voucherId: Long): String {
        return when (this) {
            VoucherTargetBuyer.ALL_BUYER -> {
                "target pembeli: semua pembeli - tipe promo: ${promoType.toEventLabel()}".asTargetBuyerEventLabel(voucherId)
            }
            else -> {
                "target pembeli: follower baru - tipe promo: ${promoType.toEventLabel()}".asTargetBuyerEventLabel(voucherId)
            }
        }
    }
}
