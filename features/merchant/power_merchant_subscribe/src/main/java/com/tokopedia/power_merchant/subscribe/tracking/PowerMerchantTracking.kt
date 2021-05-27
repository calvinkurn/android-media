package com.tokopedia.power_merchant.subscribe.tracking

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 24/05/21
 */

/**
 * Data Layer : https://mynakama.tokopedia.com/datatracker/requestdetail/1389
 */
class PowerMerchantTracking @Inject constructor(
        private val userSession: UserSessionInterface
) {

    fun sendEventClickTickBox() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_TICKBOX_TNC,
                label = getShopStatus()
        )

        sendEvent(event)
    }

    fun sendEventClickUpgradePowerMerchant() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_UPGRADE_POWER_MERCHANT,
                label = getShopStatus()
        )

        sendEvent(event)
    }

    fun sendEventClickInterestedToRegister() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_INTERESTED_TO_REGISTER,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickLearnMoreShopPerformancePopUp() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_LEARN_MORE_SHOP_PERFORMANCE_POP_UP,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickAddOneProductPopUp() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_ADD_ONE_PRODUCT_POP_UP,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickAddProduct() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_ADD_PRODUCT,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickLearnMoreShopPerformance() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_LEARN_MORE_SHOP_PERFORMANCE,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickKycDataVerification() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_DATA_VERIFICATION,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickStopPowerMerchant() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_STOP_POWER_MERCHANT,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickConfirmToStopPowerMerchant() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_CONFIRM_TO_STOP_POWER_MERCHANT,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickSubmitQuestionnaire() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.CATEGORY_POWER_MERCHANT_OPT_OUT,
                action = TrackingConstant.ACTION_CLICK_SUBMIT_QUESTIONNAIRE,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickCancelOptOutPowerMerchant() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_CANCEL_OPT_OUT_POWER_MERCHANT,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickPowerMerchantBenefitItem(benefit: String) {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_ON_POWER_MERCHANT_BENEFIT,
                label = benefit
        )

        sendEvent(event)
    }

    fun sendEventClickTipsToImproveShopScore() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_TIPS_TO_IMPROVE_SHOP_SCORE,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventShowPopupSuccessRegister() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_POPUP_SUCCESS_REGISTER,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventShowPopupImproveShopPerformance() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_POPUP_IMPROVE_SHOP_PERFORMANCE,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventShowPopupAddNewProduct() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_POPUP_ADD_NEW_PRODUCT,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickTabPowerMerchant() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_TAB_POWER_MERCHANT,
                label = getShopStatus()
        )

        sendEvent(event)
    }

    fun sendEventPopupUnableToRegisterShopModeration() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_POPUP_UNABLE_TO_REGISTER_SHOP_MODERATION,
                label = ""
        )

        sendEvent(event)
    }

    fun sendEventClickAcknowledgeShopModeration() {
        val event = createEvent(
                event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
                category = TrackingConstant.getPowerMerchantCategory(),
                action = TrackingConstant.ACTION_CLICK_ACKNOWLEDGE_SHOP_MODERATION,
                label = ""
        )

        sendEvent(event)
    }

    private fun getShopStatus(): String {
        val isOfficialStore = userSession.isShopOfficialStore
        val isPowerMerchant = userSession.isPowerMerchantIdle || userSession.isGoldMerchant
        return when {
            isOfficialStore -> TrackingConstant.OS
            isPowerMerchant -> TrackingConstant.PM
            else -> TrackingConstant.RM
        }
    }

    private fun createEvent(
            event: String, category: String,
            action: String, label: String
    ): MutableMap<String, Any> {
        val map = TrackAppUtils.gtmData(
                event, category, action, label
        )
        map[TrackingConstant.KEY_BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        map[TrackingConstant.KEY_CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER
        map[TrackingConstant.KEY_SHOP_ID] = userSession.shopId
        map[TrackingConstant.KEY_USER_ID] = userSession.userId

        return map
    }

    private fun sendEvent(map: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }
}