package com.tokopedia.promousage.util.analytics

import androidx.core.os.bundleOf
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import javax.inject.Inject

class PromoUsageEntryPointAnalytics @Inject constructor() : PromoAnalytics() {

    companion object {
        private const val PROMO_TYPE_BEBAS_ONGKIR = "bebas_ongkir"
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4175
    // Tracker ID: 46339
    fun sendImpressionPromoEntryPointEvent(
        userId: String,
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel,
        recommendedPromoCodes: List<String>
    ) {
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
        val isAppliedBo = lastApply.additionalInfo.usageSummaries
            .firstOrNull { it.type == PROMO_TYPE_BEBAS_ONGKIR } != null
        val eventLabel =
            "$entryPointMessage - $totalAvailablePromo - $totalBenefit - $promoTypes - $isGreyscale - $isAppliedBo"
        val promotions = lastApply.additionalInfo.usageSummaries.mapIndexed { index, summary ->
            val itemName = "${summary.type} - ${summary.amountStr}"
            return@mapIndexed bundleOf(
                ExtraKey.CREATIVE_NAME to summary.description,
                ExtraKey.CREATIVE_SLOT to index + 1,
                CustomDimension.DIMENSION_45 to null,
                ExtraKey.ITEM_ID to null,
                ExtraKey.ITEM_NAME to itemName
            )
        }
        sendEnhancedEcommerceEvent(
            eventName = EventName.VIEW_ITEM,
            eventAction = EventAction.IMPRESSION_PROMO_ENTRY_POINT,
            eventCategory = EventCategory.CART,
            eventLabel = eventLabel,
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.IMPRESSION_PROMO_ENTRY_POINT,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMO_CODE to null,
                ExtraKey.PROMOTIONS to promotions
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4175
    // Tracker ID: 46340
    fun sendClickPromoEntryPointEvent(
        userId: String,
        entryPointMessages: List<String>,
        entryPointInfo: PromoEntryPointInfo?,
        lastApply: LastApplyUiModel,
        recommendedPromoCodes: List<String>
    ) {
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
        val eventLabel =
            "$entryPointMessage - $totalAvailablePromo - $totalBenefit - $promoTypes - $isGreyscale - $isAppliedBo"
        val promotions = lastApply.additionalInfo.usageSummaries.mapIndexed { index, summary ->
            val itemName = "${summary.type} - ${summary.amountStr}"
            return@mapIndexed bundleOf(
                ExtraKey.CREATIVE_NAME to summary.description,
                ExtraKey.CREATIVE_SLOT to index + 1,
                CustomDimension.DIMENSION_45 to null,
                ExtraKey.ITEM_ID to null,
                ExtraKey.ITEM_NAME to itemName
            )
        }
        sendEnhancedEcommerceEvent(
            eventName = EventName.SELECT_CONTENT,
            eventAction = EventAction.CLICK_PROMO_ENTRY_POINT,
            eventCategory = EventCategory.CART,
            eventLabel = eventLabel,
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_PROMO_ENTRY_POINT,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMO_CODE to null,
                ExtraKey.PROMOTIONS to promotions
            )
        )
    }
}
