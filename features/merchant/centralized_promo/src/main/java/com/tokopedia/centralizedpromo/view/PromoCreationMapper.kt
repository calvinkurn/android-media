package com.tokopedia.centralizedpromo.view

import com.tokopedia.centralizedpromo.view.model.FilterPromoUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.sellerhomecommon.domain.model.MerchantPromotionGetPromoList
import com.tokopedia.sellerhomecommon.domain.model.MerchantPromotionGetPromoListPage

object PromoCreationMapper {

    const val TAB_ID_ALL_FEATURE = "0"
    const val TAB_NAME_ALL_FEATURE = "Semua Fitur"

    private const val PAGE_ID_TOKOPEDIA_PLAY = "64"
    private const val PAGE_ID_SHORT_VIDEO = "66"

    fun mapperToPromoCreationUiModel(
        merchantPromotionGetPromoList: MerchantPromotionGetPromoList,
        hasPlayContent: Boolean = false
    ): PromoCreationListUiModel {
        val filterItems = arrayListOf(FilterPromoUiModel(TAB_ID_ALL_FEATURE, TAB_NAME_ALL_FEATURE))
        merchantPromotionGetPromoList.data.filterTab.map {
            filterItems.add(FilterPromoUiModel(it.id, it.name))
        }

        val promotionItem =
            merchantPromotionGetPromoList.data.pages.map {
                if (shouldMapPlayPromo(it.pageId)) {
                    getCustomPlayMapping(it, hasPlayContent)
                } else {
                    PromoCreationUiModel(
                        pageId = it.pageId,
                        icon = it.iconImage,
                        title = it.pageName,
                        description = it.headerText,
                        notAvailableText = it.notAvailableText,
                        titleSuffix = it.pageNameSuffix,
                        ctaLink = it.ctaLink,
                        ctaText = it.ctaText,
                        eligible = it.isEligible,
                        banner = it.bannerImage,
                        infoText = it.infoText,
                        bottomText = it.bottomText,
                        headerText = it.headerText,
                        hasPerformanceButton = false,
                        hideCheckBox = false,
                        currentTimeMillis = System.currentTimeMillis()
                    )
                }
            }

        return PromoCreationListUiModel(
            filterItems = filterItems,
            items = promotionItem,
            errorMessage = ""
        )
    }

    private fun shouldMapPlayPromo(pageId: String): Boolean {
        return pageId == PAGE_ID_TOKOPEDIA_PLAY || pageId == PAGE_ID_SHORT_VIDEO
    }

    private fun getCustomPlayMapping(
        promo: MerchantPromotionGetPromoListPage,
        hasContent: Boolean
    ): PromoCreationUiModel {
        return PromoCreationUiModel(
            pageId = promo.pageId,
            icon = promo.iconImage,
            title = promo.pageName,
            description = promo.headerText,
            notAvailableText = promo.notAvailableText,
            titleSuffix = promo.pageNameSuffix,
            ctaLink = promo.ctaLink,
            ctaText = promo.ctaText,
            eligible = promo.isEligible,
            banner = promo.bannerImage,
            infoText = promo.infoText,
            bottomText = promo.bottomText,
            headerText = promo.headerText,
            hasPerformanceButton = hasContent,
            hideCheckBox = true,
            currentTimeMillis = System.currentTimeMillis()
        )
    }
}
