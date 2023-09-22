package com.tokopedia.promousage.util.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.list.PromoAttemptItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.view.viewmodel.getSelectedPromoCodes
import javax.inject.Inject

class PromoUsageAnalytics @Inject constructor() : PromoAnalytics() {

    companion object {
        private const val SOURCE_PAGE_CART = "cart"
        private const val SOURCE_PAGE_CHECKOUT = "checkout"
        private const val SOURCE_PAGE_OCC = "occ"
        private const val STATUS_ELIGIBLE = "eligible"
        private const val STATUS_INELIGIBLE = "ineligible"
        private const val STATUS_ERROR = "error"
        private const val SELECTED = "selected"
        private const val DESELECTED = "deselected"
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4226
    // Tracker ID: 47112
    fun sendViewAvailablePromoListNewEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        items: List<PromoItem>?,
        isError: Boolean
    ) {
        val status = if (isError) {
            STATUS_ERROR
        } else {
            val hasEligiblePromo = items
                ?.firstOrNull { it.state !is PromoItemState.Ineligible } != null
            if (hasEligiblePromo) {
                STATUS_ELIGIBLE
            } else {
                STATUS_INELIGIBLE
            }
        }
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "$status - $sourcePage"
        sendGeneralEvent(
            event = EventName.VIEW_PG_IRIS,
            eventAction = EventAction.VIEW_AVAILABLE_PROMO_LIST_NEW,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = mapOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.VIEW_AVAILABLE_PROMO_LIST_NEW,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4226
    // Tracker ID: 47113
    fun sendImpressionOfPromoCardNewEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        viewedPromo: PromoItem
    ) {
        val isEligible = viewedPromo.state !is PromoItemState.Ineligible
        val sectionName = viewedPromo.headerId
        val errorMessage = viewedPromo.errorMessage
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "$isEligible - $sectionName - $errorMessage - $sourcePage"
        val promotions = generatePromoPromotions(listOf(viewedPromo))
        sendEnhancedEcommerceEvent(
            event = EventName.VIEW_ITEM,
            eventAction = EventAction.IMPRESSION_OF_PROMO_CARD_NEW,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.IMPRESSION_OF_PROMO_CARD_NEW,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMOTIONS to promotions
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4226
    // Tracker ID: 47119
    fun sendClickPromoCardEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        clickedPromo: PromoItem
    ) {
        val selectedStr = if (clickedPromo.state is PromoItemState.Selected) {
            SELECTED
        } else {
            DESELECTED
        }
        val sectionName = clickedPromo.headerId
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "$selectedStr - $sectionName - $sourcePage"
        val promotions = generatePromoPromotions(listOf(clickedPromo))
        sendEnhancedEcommerceEvent(
            event = EventName.SELECT_CONTENT,
            eventAction = EventAction.CLICK_PROMO_CARD,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_PROMO_CARD,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMOTIONS to promotions
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4226
    // Tracker ID: 47121
    fun sendClickPakaiPromoPromoCodeEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        attemptItem: PromoAttemptItem?,
        attemptedPromo: PromoItem?
    ) {
        val attemptedCodeStr = attemptItem?.attemptedPromoCode.ifNull { "" }
        val amountStr = attemptedPromo?.benefitAmount.ifNull { "" }
        val errorStr = attemptItem?.errorMessage.ifNull { "" }
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "$attemptedCodeStr - $amountStr - $errorStr - $sourcePage"
        val promotions = if (attemptedPromo != null) {
            generatePromoPromotions(listOf(attemptedPromo))
        } else {
            emptyList()
        }
        sendEnhancedEcommerceEvent(
            event = EventName.SELECT_CONTENT,
            eventAction = EventAction.CLICK_PAKAI_PROMO_PROMO_CODE,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_PAKAI_PROMO_PROMO_CODE,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMOTIONS to promotions
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4226
    // Tracker ID: 47131
    fun sendClickExitPromoBottomsheetEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        appliedPromos: List<PromoItem>
    ) {
        val hasAppliedPromo = appliedPromos.isNotEmpty()
        val promoCodes = appliedPromos.getSelectedPromoCodes().joinToString(",")
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "$hasAppliedPromo - $promoCodes - $sourcePage"
        val promotions = generatePromoPromotions(appliedPromos)
        sendEnhancedEcommerceEvent(
            event = EventName.SELECT_CONTENT,
            eventAction = EventAction.CLICK_EXIT_PROMO_BOTTOMSHEET,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_EXIT_PROMO_BOTTOMSHEET,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMOTIONS to promotions
            )
        )
    }

    private fun generatePromoPromotions(promos: List<PromoItem>): List<Bundle> {
        if (promos.isEmpty()) return emptyList()
        return promos.map { promo ->
            val itemId = if (promo.useSecondaryPromo) {
                "${promo.secondaryPromo.code} - ${promo.secondaryPromo.benefitAmount} - ${promo.secondaryPromo.benefitDetail.benefitType} - ${promo.secondaryPromo.uniqueId}"
            } else {
                "${promo.code} - ${promo.benefitAmount} - ${promo.benefitDetail.benefitType} - ${promo.uniqueId}"
            }
            bundleOf(
                ExtraKey.CREATIVE_NAME to null,
                ExtraKey.CREATIVE_SLOT to null,
                CustomDimension.DIMENSION_79 to promo.shopId,
                ExtraKey.ITEM_ID to itemId,
                ExtraKey.ITEM_NAME to null
            )
        }
    }

    private fun generateSourcePage(entryPoint: PromoPageEntryPoint): String {
        return when (entryPoint) {
            PromoPageEntryPoint.CART_PAGE -> SOURCE_PAGE_CART
            PromoPageEntryPoint.CHECKOUT_PAGE -> SOURCE_PAGE_CHECKOUT
            PromoPageEntryPoint.OCC_PAGE -> SOURCE_PAGE_OCC
        }
    }
}
