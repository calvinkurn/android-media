package com.tokopedia.shop.score.common.analytics

import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.BROADCAST_CHAT
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.BROADCAST_CHAT_IDENTIFIER
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.BUSSINESS_UNIT
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_CHECK_PENALTY
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_COMPLETE_INFO
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_CONTACT_HELP_CENTER
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_HELP_CENTER
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_LEARN_MORE
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_LEARN_SHOP_PERFORMANCE
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_MERCHANT_TOOLS
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_SEE_ALL_BENEFIT
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_SEE_DETAIL_PENALTY
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_SHOP_SCORE
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_WATCH_VIDEO
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CLICK_YOUR_SHOP_GET_PM
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.CURRENT_SITE
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.FREE_SHIPPING
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.FREE_SHIPPING_IDENTIFIER
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.IMPRESSION_CALL_HELP_CENTER
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.IMPRESSION_CHECK_PENALTY
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.IMPRESSION_COMPLETE_INFO
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.IMPRESSION_GET_PM
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.IMPRESSION_HELP_CENTER
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.IMPRESSION_LEARN_MORE
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.IMPRESSION_LEARN_SHOP_PERFORMANCE
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.IMPRESSION_MERCHANT_TOOLS
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.IMPRESSION_SEE_ALL_BENEFIT
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.IMPRESSION_SEE_PENALTY_DETAIL
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.IMPRESSION_WATCH_VIDEO
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.OLD_PERFORMA_TOKO_PAGE
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.PHYSICAL_GOODS
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.SHOP_SCORE_PAGE
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.SHOP_TYPE_OS
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.SHOP_TYPE_PM
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.SHOP_TYPE_RM
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.TOKOPEDIA_SELLER
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.TRANSITION_PERIOD_PENALTY_PAGE
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.TRANSITION_PERIOD_SHOP_SCORE
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.USER_ID
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.VIEW_SHOP_SCORE_IRIS
import com.tokopedia.shop.score.common.analytics.ShopScoreTrackingConstant.VOUCHER_RECOMMENDATION_IDENTIFIER
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * SHOP SCORE REVAMP
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/804
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/949
 */
class ShopScorePenaltyTracking @Inject constructor(private val userSession: UserSessionInterface) {
    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    private val typeShop = when {
        userSession.isShopOfficialStore -> {
            SHOP_TYPE_OS
        }
        userSession.isGoldMerchant -> {
            SHOP_TYPE_PM
        }
        else -> {
            SHOP_TYPE_RM
        }
    }

    fun clickHereTickerPenalty() {
        sendShopScoreTransitionItemEvent(CLICK_SEE_DETAIL_PENALTY)
    }

    fun clickMenuPenalty() {
        sendShopScoreTransitionItemEvent(CLICK_CHECK_PENALTY)
    }

    fun clickMenuCompleteInfo() {
        sendShopScoreTransitionItemEvent(CLICK_COMPLETE_INFO)
    }

    fun clickPowerMerchantSection() {
        sendShopScoreTransitionItemEvent(CLICK_YOUR_SHOP_GET_PM)
    }

    fun clickSeeAllBenefitInRM(isNewSeller: Boolean) {
        sendShopScoreTransitionItemEvent(CLICK_SEE_ALL_BENEFIT, isNewSeller)
    }

    fun clickLearMorePenaltyPage() {
        sendShopScoreTransitionItemEvent(CLICK_LEARN_MORE)
    }

    fun clickLearMoreHelpCenterPenaltyDetail() {
        sendShopScoreTransitionItemEvent(CLICK_CONTACT_HELP_CENTER)
    }

    fun clickMerchantToolsRecommendation(identifier: String) {
        val toolsLabel = when (identifier) {
            FREE_SHIPPING_IDENTIFIER -> FREE_SHIPPING
            VOUCHER_RECOMMENDATION_IDENTIFIER -> FREE_SHIPPING
            BROADCAST_CHAT_IDENTIFIER -> BROADCAST_CHAT
            else -> ""
        }
        val mapData = mapOf(
                TrackAppUtils.EVENT to CLICK_SHOP_SCORE,
                TrackAppUtils.EVENT_CATEGORY to TRANSITION_PERIOD_SHOP_SCORE,
                TrackAppUtils.EVENT_ACTION to CLICK_MERCHANT_TOOLS,
                TrackAppUtils.EVENT_LABEL to toolsLabel,
                BUSSINESS_UNIT to PHYSICAL_GOODS,
                CURRENT_SITE to TOKOPEDIA_SELLER,
                USER_ID to userSession.userId
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun impressTickerPenaltyShopScore() {
        impressShopScoreItemEvent(TRANSITION_PERIOD_SHOP_SCORE, IMPRESSION_SEE_PENALTY_DETAIL)
    }

    fun impressMenuPenalty() {
        impressShopScoreItemEvent(TRANSITION_PERIOD_SHOP_SCORE, IMPRESSION_CHECK_PENALTY)
    }

    fun impressMenuInfoPage() {
        impressShopScoreItemEvent(TRANSITION_PERIOD_SHOP_SCORE, IMPRESSION_COMPLETE_INFO)
    }

    fun impressPotentialPowerMerchant(isNewSeller: Boolean) {
        impressShopScoreItemEvent(TRANSITION_PERIOD_SHOP_SCORE, IMPRESSION_GET_PM, isNewSeller)
    }

    fun impressSeeAllBenefitPowerMerchant(isNewSeller: Boolean) {
        impressShopScoreItemEvent(TRANSITION_PERIOD_SHOP_SCORE, IMPRESSION_SEE_ALL_BENEFIT, isNewSeller)
    }

    fun impressLearnMorePenaltyPage() {
        impressShopScoreItemEvent(TRANSITION_PERIOD_PENALTY_PAGE, IMPRESSION_LEARN_MORE)
    }

    fun impressHelpCenterPenaltyDetail() {
        impressShopScoreItemEvent(TRANSITION_PERIOD_PENALTY_PAGE, IMPRESSION_CALL_HELP_CENTER)
    }

    fun impressWatchVideoNewSeller(isNewSeller: Boolean) {
        impressShopScoreItemEvent(SHOP_SCORE_PAGE, IMPRESSION_WATCH_VIDEO, isNewSeller)
    }

    fun clickWatchVideoNewSeller() {
        sendShopScoreItemEventNewSeller(CLICK_WATCH_VIDEO)
    }

    fun clickLearnShopPerformanceNewSeller() {
        sendShopScoreItemEventNewSeller(CLICK_LEARN_SHOP_PERFORMANCE)
    }

    fun impressLearnShopPerformanceNewSeller(isNewSeller: Boolean) {
        impressShopScoreItemEvent(SHOP_SCORE_PAGE, IMPRESSION_LEARN_SHOP_PERFORMANCE, isNewSeller)
    }

    fun clickHelpCenterFaqNewSeller() {
        sendShopScoreItemEventNewSeller(CLICK_HELP_CENTER)
    }

    fun impressHelpCenterFaqNewSeller(isNewSeller: Boolean) {
        impressShopScoreItemEvent(SHOP_SCORE_PAGE, IMPRESSION_HELP_CENTER, isNewSeller)
    }

    fun impressMerchantToolsRecommendation() {
        val toolsLabel = "" //to be confirm
        val mapData = mapOf(
                TrackAppUtils.EVENT to VIEW_SHOP_SCORE_IRIS,
                TrackAppUtils.EVENT_CATEGORY to TRANSITION_PERIOD_SHOP_SCORE,
                TrackAppUtils.EVENT_ACTION to IMPRESSION_MERCHANT_TOOLS,
                TrackAppUtils.EVENT_LABEL to toolsLabel,
                BUSSINESS_UNIT to PHYSICAL_GOODS,
                CURRENT_SITE to TOKOPEDIA_SELLER,
                USER_ID to userSession.userId
        )
        tracker.sendGeneralEvent(mapData)
    }

    private fun impressShopScoreItemEvent(categoryName: String, actionName: String, isNewSeller: Boolean = false) {
        val mapData = mapOf(
                TrackAppUtils.EVENT to VIEW_SHOP_SCORE_IRIS,
                TrackAppUtils.EVENT_CATEGORY to categoryName,
                TrackAppUtils.EVENT_ACTION to actionName,
                TrackAppUtils.EVENT_LABEL to  if (isNewSeller) "${ShopScoreTrackingConstant.NEW_SELLER} $typeShop" else typeShop,
                BUSSINESS_UNIT to PHYSICAL_GOODS,
                CURRENT_SITE to TOKOPEDIA_SELLER,
                USER_ID to userSession.userId
        )
        tracker.sendGeneralEvent(mapData)
    }

    private fun sendShopScoreTransitionItemEvent(actionName: String, isNewSeller: Boolean = false) {
        val mapData = mapOf(
                TrackAppUtils.EVENT to CLICK_SHOP_SCORE,
                TrackAppUtils.EVENT_CATEGORY to TRANSITION_PERIOD_SHOP_SCORE,
                TrackAppUtils.EVENT_ACTION to actionName,
                TrackAppUtils.EVENT_LABEL to  if (isNewSeller) "${ShopScoreTrackingConstant.NEW_SELLER} $typeShop" else typeShop,
                BUSSINESS_UNIT to PHYSICAL_GOODS,
                CURRENT_SITE to TOKOPEDIA_SELLER,
                USER_ID to userSession.userId
        )
        tracker.sendGeneralEvent(mapData)
    }

    private fun sendShopScoreItemEventNewSeller(actionName: String) {
        val mapData = mapOf(
                TrackAppUtils.EVENT to CLICK_SHOP_SCORE,
                TrackAppUtils.EVENT_CATEGORY to SHOP_SCORE_PAGE,
                TrackAppUtils.EVENT_ACTION to actionName,
                TrackAppUtils.EVENT_LABEL to "${ShopScoreTrackingConstant.NEW_SELLER} $typeShop",
                BUSSINESS_UNIT to PHYSICAL_GOODS,
                CURRENT_SITE to TOKOPEDIA_SELLER,
                USER_ID to userSession.userId
        )
        tracker.sendGeneralEvent(mapData)
    }
}