package com.tokopedia.promousage.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.tokopedia.promousage.analytics.PromoAnalytics
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import javax.inject.Inject

class PromoUsageEntryPointAnalytics @Inject constructor() : PromoAnalytics() {

    companion object {
        private const val PROMO_TYPE_BEBAS_ONGKIR = "bebas_ongkir"
    }

    // region Cart
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4175
    // Tracker ID: 46339
    fun sendImpressionPromoEntryPointEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel,
        recommendedPromoCodes: List<String>
    ) {
        sendEnhancedEcommerceEvent(
            event = EventName.VIEW_ITEM,
            eventAction = EventAction.IMPRESSION_CART_PROMO_ENTRY_POINT,
            eventCategory = generateEntryPoint(entryPoint),
            eventLabel = generateEntryPointCartEventLabel(entryPointMessages, entryPointInfo, lastApply, recommendedPromoCodes),
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.IMPRESSION_CART_PROMO_ENTRY_POINT,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMO_CODE to null,
                ExtraKey.PROMOTIONS to generateEntryPointPromotions(lastApply)
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4175
    // Tracker ID: 46340
    fun sendClickPromoEntryPointEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel,
        recommendedPromoCodes: List<String>
    ) {
        sendEnhancedEcommerceEvent(
            event = EventName.SELECT_CONTENT,
            eventAction = EventAction.CLICK_CART_PROMO_ENTRY_POINT,
            eventCategory = generateEntryPoint(entryPoint),
            eventLabel = generateEntryPointCartEventLabel(entryPointMessages, entryPointInfo, lastApply, recommendedPromoCodes),
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_CART_PROMO_ENTRY_POINT,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMO_CODE to null,
                ExtraKey.PROMOTIONS to generateEntryPointPromotions(lastApply)
            )
        )
    }

    private fun generateEntryPointCartEventLabel(
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel,
        recommendedPromoCodes: List<String>
    ): String {
        val entryPointMessage = entryPointMessages.joinToString(separator = ",")
        val totalAvailablePromo = if (lastApply.additionalInfo.usageSummaries.isNotEmpty()) {
            lastApply.additionalInfo.usageSummaries.count()
        } else {
            recommendedPromoCodes.count()
        }
        val totalBenefit = lastApply.benefitSummaryInfo.finalBenefitAmount
        val promoTypes =
            lastApply.additionalInfo.usageSummaries.joinToString(separator = ",") { it.type }
        val isGreyscale =
            entryPointInfo != null && entryPointInfo.color == PromoEntryPointInfo.COLOR_GREY
        val boPromo =
            lastApply.additionalInfo.usageSummaries.firstOrNull { it.type == PROMO_TYPE_BEBAS_ONGKIR }
        val isAppliedBo = boPromo != null
        return "$entryPointMessage - $totalAvailablePromo - $totalBenefit - $promoTypes - $isGreyscale - $isAppliedBo"
    }
    // endregion

    // region Checkout/OCC
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4190
    // Tracker ID: 46628
    fun sendImpressionUserSavingTotalSubsidyEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel
    ) {
        sendEnhancedEcommerceEvent(
            event = EventName.VIEW_ITEM,
            eventAction = EventAction.IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT,
            eventCategory = generateEntryPoint(entryPoint),
            eventLabel = generateEntryPointCheckoutOccEventLabel(entryPoint, entryPointMessages, entryPointInfo),
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PURCHASE_PLATFORM,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMO_CODE to null,
                ExtraKey.PROMOTIONS to generateEntryPointPromotions(lastApply)
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4190
    // Tracker ID: 46629
    fun sendClickUserSavingAndPromoEntryPointEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel
    ) {
        sendEnhancedEcommerceEvent(
            event = EventName.SELECT_CONTENT,
            eventAction = EventAction.CLICK_CHECKOUT_PROMO_ENTRY_POINT,
            eventCategory = generateEntryPoint(entryPoint),
            eventLabel = generateEntryPointCheckoutOccEventLabel(entryPoint, entryPointMessages, entryPointInfo),
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_CHECKOUT_PROMO_ENTRY_POINT,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PURCHASE_PLATFORM,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMO_CODE to null,
                ExtraKey.PROMOTIONS to generateEntryPointPromotions(lastApply)
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4190
    // Tracker ID: 46630
    fun sendImpressionUserSavingDetailTotalSubsidyEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel
    ) {
        sendEnhancedEcommerceEvent(
            event = EventName.VIEW_ITEM,
            eventAction = EventAction.IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT_DETAIL,
            eventCategory = generateEntryPoint(entryPoint),
            eventLabel = generateEntryPointCheckoutOccEventLabel(entryPoint, entryPointMessages, entryPointInfo),
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT_DETAIL,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PURCHASE_PLATFORM,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMO_CODE to null,
                ExtraKey.PROMOTIONS to generateEntryPointPromotions(lastApply)
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4190
    // Tracker ID: 46631
    fun sendClickUserSavingDetailTotalSubsidyEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel
    ) {
        sendEnhancedEcommerceEvent(
            event = EventName.SELECT_CONTENT,
            eventAction = EventAction.CLICK_CHECKOUT_PROMO_ENTRY_POINT_DETAIL,
            eventCategory = generateEntryPoint(entryPoint),
            eventLabel = generateEntryPointCheckoutOccEventLabel(entryPoint, entryPointMessages, entryPointInfo),
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_CHECKOUT_PROMO_ENTRY_POINT_DETAIL,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PURCHASE_PLATFORM,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMO_CODE to null,
                ExtraKey.PROMOTIONS to generateEntryPointPromotions(lastApply)
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4190
    // Tracker ID: 46650
    fun sendImpressionPromoEntryPointErrorEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        errorMessage: String,
        lastApply: LastApplyUiModel
    ) {
        sendEnhancedEcommerceEvent(
            event = EventName.VIEW_ITEM,
            eventAction = EventAction.IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT_ERROR,
            eventCategory = generateEntryPoint(entryPoint),
            eventLabel = errorMessage,
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.IMPRESSION_CHECKOUT_PROMO_ENTRY_POINT_ERROR,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMO_CODE to null,
                ExtraKey.PROMOTIONS to generateEntryPointPromotions(lastApply)
            )
        )
    }

    private fun generateEntryPointCheckoutOccEventLabel(
        entryPoint: PromoPageEntryPoint,
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?
    ): String {
        val entryPointMessage = entryPointMessages.joinToString(separator = ",")
        val isOcc = entryPoint == PromoPageEntryPoint.OCC_PAGE
        val isGreyscale =
            entryPointInfo != null && entryPointInfo.color == PromoEntryPointInfo.COLOR_GREY
        return "$isOcc-$isGreyscale- $entryPointMessage"
    }
    // endregion

    // region Helper
    private fun generateEntryPoint(entryPoint: PromoPageEntryPoint): String {
        return when (entryPoint) {
            PromoPageEntryPoint.CART_PAGE -> EventCategory.CART
            PromoPageEntryPoint.CHECKOUT_PAGE -> EventCategory.CHECKOUT
            PromoPageEntryPoint.OCC_PAGE -> EventCategory.OCC
        }
    }

    private fun generateEntryPointPromotions(lastApply: LastApplyUiModel): List<Bundle> {
        return lastApply.additionalInfo.usageSummaries.mapIndexed { index, summary ->
            val itemName = "${summary.type}-${summary.amountStr}"
            return@mapIndexed bundleOf(
                ExtraKey.CREATIVE_NAME to summary.description,
                ExtraKey.CREATIVE_SLOT to index + 1,
                CustomDimension.DIMENSION_45 to null,
                ExtraKey.ITEM_ID to null,
                ExtraKey.ITEM_NAME to itemName
            )
        }
    }
    // endregion
}
