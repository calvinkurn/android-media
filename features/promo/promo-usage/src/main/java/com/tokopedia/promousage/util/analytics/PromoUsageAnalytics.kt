package com.tokopedia.promousage.util.analytics

import android.os.Bundle
import androidx.core.os.bundleOf
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoAttemptItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
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
    // Tracker ID: 47123
    fun sendClickPakaiPromoNewEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        promoRecommendation: PromoRecommendationItem,
        recommendedPromos: List<PromoItem>
    ) {
        val benefitAmountStr = recommendedPromos.sumOf { it.benefitAmount }
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "$benefitAmountStr - ${promoRecommendation.messageSelected} - $sourcePage"
        val promotions = generatePromoPromotions(recommendedPromos)
        sendEnhancedEcommerceEvent(
            event = EventName.SELECT_CONTENT,
            eventAction = EventAction.CLICK_PAKAI_PROMO_NEW,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_PAKAI_PROMO_NEW,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMOTIONS to promotions
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4226
    // Tracker ID: 47124
    fun sendClickDetailTermAndConditionsEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        selectedPromos: List<PromoItem>
    ) {
        val eventLabel = generateSourcePage(entryPoint)
        val promotions = generatePromoPromotions(selectedPromos)
        sendEnhancedEcommerceEvent(
            event = EventName.SELECT_CONTENT,
            eventAction = EventAction.CLICK_DETAIL_TERM_AND_CONDITIONS,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_DETAIL_TERM_AND_CONDITIONS,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE,
                ExtraKey.PROMOTIONS to promotions
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4226
    // Tracker ID: 47127
    fun sendClickExpandPromoSectionEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        promoHeader: PromoAccordionHeaderItem
    ) {
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "${promoHeader.id} - ${promoHeader.totalPromoCount} - $sourcePage"
        sendGeneralEvent(
            event = EventName.CLICK_PG,
            eventAction = EventAction.CLICK_EXPAND_PROMO_SECTION,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = mapOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_EXPAND_PROMO_SECTION,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4226
    // Tracker ID: 47129
    fun sendClickExpandPromoSectionDetailEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        promoViewAll: PromoAccordionViewAllItem
    ) {
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "${promoViewAll.id} - ${promoViewAll.totalPromoCount} - $sourcePage"
        sendGeneralEvent(
            event = EventName.CLICK_PG,
            eventAction = EventAction.CLICK_EXPAND_PROMO_SECTION_DETAIL,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = mapOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_EXPAND_PROMO_SECTION_DETAIL,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4226
    // Tracker ID: 47130
    fun sendClickCheckoutPromoEvent(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        appliedPromos: List<PromoItem>,
        isSuccess: Boolean
    ) {
        val totalPromo = appliedPromos.size
        val totalPromoBenefitAmount = appliedPromos.sumOf { it.benefitAmount }
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "$isSuccess - $totalPromo - $totalPromoBenefitAmount - $sourcePage"
        val promotions = generatePromoPromotions(appliedPromos)
        sendEnhancedEcommerceEvent(
            event = EventName.SELECT_CONTENT,
            eventAction = EventAction.CLICK_CHECKOUT_PROMO,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = bundleOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_CHECKOUT_PROMO,
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

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4481
    // Tracker ID: 49867
    fun sendImpressionActivationGoPayLater(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        viewedPromo: PromoItem
    ) {
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "$sourcePage"
        sendGeneralEvent(
            event = EventName.VIEW_PG_IRIS,
            eventAction = EventAction.IMPRESSION_GOPAY_LATER_ACTIVATION,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = mapOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.SHOP_ID to viewedPromo.shopId,
                ExtraKey.TRACKER_ID to TrackerId.IMPRESSION_GOPAY_LATER_ACTIVATION,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4481
    // Tracker ID: 49868
    fun sendClickActivationGoPayLater(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        clickedPromo: PromoItem,
    ) {
        val sourcePage = generateSourcePage(entryPoint)
        val selectedStr = if (clickedPromo.state is PromoItemState.Selected) {
            Int.ONE
        } else {
            Int.ZERO
        }
        val eventLabel = "$sourcePage - $selectedStr"
        sendGeneralEvent(
            event = EventName.CLICK_PG,
            eventAction = EventAction.CLICK_GOPAY_LATER,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = mapOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.SHOP_ID to clickedPromo.shopId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_GOPAY_LATER,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4481
    // Tracker ID: 49869
    fun sendImpressionAutoApplyGpl(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        viewedPromo: PromoItem
    ) {
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "${viewedPromo.promoId} - $sourcePage"
        sendGeneralEvent(
            event = EventName.VIEW_PG_IRIS,
            eventAction = EventAction.IMPRESSION_AUTOAPPLY_GPL,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = mapOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.SHOP_ID to viewedPromo.shopId,
                ExtraKey.TRACKER_ID to TrackerId.IMPRESSION_AUTOAPPLY_GPL,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4481
    // Tracker ID: 49870
    fun sendImpressionGplEligible(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        viewedPromo: PromoItem
    ) {
        val sourcePage = generateSourcePage(entryPoint)
        val eventLabel = "${viewedPromo.promoId} - $sourcePage"
        sendGeneralEvent(
            event = EventName.VIEW_PG_IRIS,
            eventAction = EventAction.IMPRESSION_GPL_ELIGIBLE,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = mapOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.SHOP_ID to viewedPromo.shopId,
                ExtraKey.TRACKER_ID to TrackerId.IMPRESSION_GPL_ELIGIBLE,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE
            )
        )
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4481
    // Tracker ID: 49884
    fun sendClickGplEligible(
        userId: String,
        entryPoint: PromoPageEntryPoint,
        clickedPromo: PromoItem
    ) {
        val sourcePage = generateSourcePage(entryPoint)
        val selectedStr = if (clickedPromo.state is PromoItemState.Selected) {
            Int.ONE
        } else {
            Int.ZERO
        }
        val eventLabel = "${clickedPromo.promoId} - $sourcePage - $selectedStr"
        sendGeneralEvent(
            event = EventName.CLICK_PG,
            eventAction = EventAction.CLICK_GPL_ELIGIBLE,
            eventCategory = EventCategory.PROMO,
            eventLabel = eventLabel,
            additionalData = mapOf(
                ExtraKey.USER_ID to userId,
                ExtraKey.SHOP_ID to clickedPromo.shopId,
                ExtraKey.TRACKER_ID to TrackerId.CLICK_GPL_ELIGIBLE,
                ExtraKey.BUSINESS_UNIT to CustomDimension.BUSINESS_UNIT_PHYSICAL_GOODS,
                ExtraKey.CURRENT_SITE to CustomDimension.CURRENT_SITE_MARKETPLACE
            )
        )
    }
}
