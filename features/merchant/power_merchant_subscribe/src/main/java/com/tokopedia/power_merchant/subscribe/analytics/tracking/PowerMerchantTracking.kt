package com.tokopedia.power_merchant.subscribe.analytics.tracking

import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 24/05/21
 */

/**
 * Data Layer :
 * SA : https://mynakama.tokopedia.com/datatracker/product/requestdetail/1358
 * MA : https://mynakama.tokopedia.com/datatracker/product/requestdetail/1159
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

    fun sendEventClickUpgradePowerMerchantPro() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_UPGRADE_POWER_MERCHANT_PRO,
            label = getShopStatus()
        )

        sendEvent(event)
    }

    fun sendEventClickInterestedToRegister() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_INTERESTED_TO_REGISTER,
            label = getShopStatus()
        )

        sendEvent(event)
    }

    fun sendEventClickLearnMoreShopPerformancePopUp() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_LEARN_MORE_SHOP_PERFORMANCE_POP_UP,
            label = getShopStatus()
        )

        sendEvent(event)
    }

    fun sendEventClickLearnPopUpImproveNumberOfOrder() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_POP_UP_IMPROVE_NUMBER_OF_ORDER,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickLearnPopUpImproveNiv() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_POP_UP_IMPROVE_NIV,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickStopPmBecomeRm() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_STOP_PM_BECOME_RM,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickStopPmBecomePm() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_STOP_PM_BECOME_PM,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickAddOneProductPopUp() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_ADD_ONE_PRODUCT_POP_UP,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickAddProduct() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_ADD_PRODUCT,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickLearnMoreShopPerformance() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_LEARN_MORE_SHOP_PERFORMANCE,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickKycDataVerification() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_DATA_VERIFICATION,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickStopPowerMerchant() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_STOP_POWER_MERCHANT,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickConfirmToStopPowerMerchant() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_CONFIRM_TO_STOP_POWER_MERCHANT,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickSubmitQuestionnaire() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.CATEGORY_POWER_MERCHANT_OPT_OUT,
            action = TrackingConstant.ACTION_CLICK_SUBMIT_QUESTIONNAIRE,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickCancelOptOutPowerMerchant(isPmPro: Boolean) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_CANCEL_OPT_OUT_PROCESS,
            label = if (isPmPro) {
                TrackingConstant.POWER_MERCHANT_PRO
            } else {
                TrackingConstant.POWER_MERCHANT
            }
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

    fun sendEventClickTipsToImproveShopScore(shopScore: String) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_TIPS_TO_IMPROVE_SHOP_SCORE,
            label = shopScore
        )

        sendEvent(event)
    }

    fun sendEventShowPopupSuccessRegister() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_POPUP_SUCCESS_REGISTER,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventShowPopupImproveShopPerformance() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_LEARN_MORE_SHOP_PERFORMANCE_POP_UP,
            label = getShopStatus()
        )

        sendEvent(event)
    }

    fun sendEventShowPopupAddNewProduct(shopScore: String) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_PG,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_POPUP_ADD_NEW_PRODUCT,
            label = getLabelWithShopStatusAndShopScore(shopScore)
        )

        sendEvent(event)
    }

    fun sendEventClickTabPowerMerchantPro(tabLabel: String) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_PG,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_TAB_POWER_MERCHANT,
            label = tabLabel
        )

        sendEvent(event)
    }

    fun sendEventPopupUnableToRegisterShopModeration() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_POPUP_UNABLE_TO_REGISTER_SHOP_MODERATION,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickAcknowledgeShopModeration() {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_ACKNOWLEDGE_SHOP_MODERATION,
            label = String.EMPTY
        )

        sendEvent(event)
    }

    fun sendEventClickNextPmProDeactivation(shopTire: Int) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_STOP_PM_CONFIRM_TO_STOP,
            label = if (shopTire == PMConstant.ShopTierType.REGULAR_MERCHANT) {
                TrackingConstant.BECOME_RM
            } else {
                TrackingConstant.BECOME_PM
            }
        )

        sendEvent(event)
    }

    fun sendEventClickTooltipShopLevel(shopLevel: String) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_PG,
            action = TrackingConstant.ACTION_CLICK_TOOLTIP_SHOP_LEVEL,
            category = TrackingConstant.getPowerMerchantCategory(),
            label = shopLevel
        )

        sendEvent(event)
    }

    fun sendEventClickCTAPmUpgradeLearnMore(shopScore: String) {
        val event = createEventMapPmPro(
            event = TrackingConstant.EVENT_CLICK_POWER_MERCHANT,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.CLICK_LEARN_MORE_PM_PRO,
            label = getLabelWithShopStatusAndShopScore(shopScore)
        )

        event[TrackingConstant.KEY_PAGE_SOURCE] = TrackingConstant.PM_PRO_ACTIVATION_PAGE
        sendEvent(event)
    }

    fun sendEventClickDetailTermPM(shopScore: String) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_PG,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_DETAIL_TERM_MEMBERSHIP,
            label = getLabelWithShopStatusAndShopScore(shopScore)
        )

        sendEvent(event)
    }

    fun sendEventClickLearnMorePMBenefit(shopScore: String) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_PG,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_LEARN_MORE_PM_BENEFIT,
            label = getLabelWithShopStatusAndShopScore(shopScore)
        )

        sendEvent(event)
    }

    fun sendEventClickLearnMorePM(shopScore: String) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_PG,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_LEARN_MORE_PM,
            label = getLabelWithShopStatusAndShopScore(shopScore)
        )

        sendEvent(event)
    }

    fun sendEventClickSeeCategory(shopScore: String) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_PG,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.ACTION_CLICK_SEE_CATEGORY,
            label = getLabelWithShopStatusAndShopScore(shopScore)
        )

        sendEvent(event)
    }


    fun sendEventClickProgressBar(currentGrade: String) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_PG,
            action = TrackingConstant.ACTION_CLICK_PROGRESS_BAR,
            category = TrackingConstant.getPowerMerchantCategory(),
            label = currentGrade
        )

        sendEvent(event)
    }

    fun sendEventClickFeeService(shopScore: String) {
        val event = createEvent(
            event = TrackingConstant.EVENT_CLICK_PG,
            action = TrackingConstant.ACTION_CLICK_FEE_SERVICE,
            category = TrackingConstant.getPowerMerchantCategory(),
            label = getLabelWithShopStatusAndShopScore(shopScore)
        )

        sendEvent(event)
    }

    fun sendEventImpressFeeService(shopScore: String) {
        val event = createEventMapPmPro(
            event = TrackingConstant.VIEW_PG_IRIS,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.IMPRESSION_FEE_SERVICE,
            label = getLabelWithShopStatusAndShopScore(shopScore)
        )

        event[TrackingConstant.KEY_PAGE_SOURCE] = TrackingConstant.PM_PRO_ACTIVATION_PAGE
        sendEvent(event)
    }

    fun sendEventImpressUpliftPmPro(shopScore: String) {
        val event = createEventMapPmPro(
            event = TrackingConstant.VIEW_PG_IRIS,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.IMPRESSION_PM_PRO_LEARN_MORE,
            label = getLabelWithShopStatusAndShopScore(shopScore)
        )

        event[TrackingConstant.KEY_PAGE_SOURCE] = TrackingConstant.PM_PRO_ACTIVATION_PAGE
        sendEvent(event)
    }

    fun sendEventImpressUpsellPmPro(shopScore: String) {
        val event = createEventMapPmPro(
            event = TrackingConstant.VIEW_POWER_MERCHANT_IRIS,
            category = TrackingConstant.getPowerMerchantCategory(),
            action = TrackingConstant.IMPRESSION_PM_PRO_LEARN_MORE,
            label = getLabelWithShopStatusAndShopScore(shopScore)
        )

        event[TrackingConstant.KEY_PAGE_SOURCE] = TrackingConstant.PM_PRO_ACTIVATION_PAGE
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

    private fun getLabelWithShopStatusAndShopScore(shopScore: String) =
        "${TrackingConstant.SHOP_TYPE}: ${getShopStatus()} - ${TrackingConstant.SHOP_SCORE}: $shopScore"

    private fun createEventMapPmPro(
        event: String, category: String,
        action: String, label: String
    ): MutableMap<String, Any> {
        val map = TrackAppUtils.gtmData(
            event, category, action, label
        )
        map[TrackingConstant.KEY_BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS_NEW
        map[TrackingConstant.KEY_CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER
        map[TrackingConstant.KEY_SHOP_ID] = userSession.shopId
        map[TrackingConstant.KEY_USER_ID] = userSession.userId

        return map
    }

    private fun createEvent(
        event: String, category: String,
        action: String, label: String
    ): MutableMap<String, Any> {
        return TrackerUtils.createEvent(userSession, event, category, action, label)
    }

    private fun sendEvent(map: Map<String, Any>) {
        TrackerUtils.sendEvent(map)
    }
}