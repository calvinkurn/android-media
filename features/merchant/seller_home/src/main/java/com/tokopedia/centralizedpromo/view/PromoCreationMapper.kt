package com.tokopedia.centralizedpromo.view

import com.tokopedia.centralizedpromo.domain.model.MerchantPromotionGetPromoList
import com.tokopedia.centralizedpromo.view.model.FilterPromoUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel

object PromoCreationMapper {

    const val TAB_ID_ALL_FEATURE = "0"
    const val TAB_NAME_ALL_FEATURE = "Semua Fitur"

    fun mapperToPromoCreationUiModel(merchantPromotionGetPromoList: MerchantPromotionGetPromoList): PromoCreationListUiModel {

        val filterItems = arrayListOf(FilterPromoUiModel(TAB_ID_ALL_FEATURE, TAB_NAME_ALL_FEATURE))
        merchantPromotionGetPromoList.data.filterTab.map {
            filterItems.add(FilterPromoUiModel(it.id, it.name))
        }

        val promotionItem =
            merchantPromotionGetPromoList.data.pages.map {
                PromoCreationUiModel(
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
                    headerText = it.headerText
                )
            }

        return PromoCreationListUiModel(
            filterItems = filterItems,
            items = promotionItem,
            errorMessage = ""
        )
    }
}