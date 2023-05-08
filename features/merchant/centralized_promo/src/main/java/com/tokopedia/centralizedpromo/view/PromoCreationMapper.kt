package com.tokopedia.centralizedpromo.view

import com.tokopedia.centralizedpromo.view.model.FilterPromoUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.sellerhomecommon.domain.model.MerchantPromotionGetPromoList

object PromoCreationMapper {

    const val TAB_ID_ALL_FEATURE = "0"
    const val TAB_NAME_ALL_FEATURE = "Semua Fitur"
    const val PAGE_NAME_TOPADS = "TopAds"

    fun mapperToPromoCreationUiModel(merchantPromotionGetPromoList: MerchantPromotionGetPromoList): PromoCreationListUiModel {

        val filterItems = arrayListOf(FilterPromoUiModel(TAB_ID_ALL_FEATURE, TAB_NAME_ALL_FEATURE))
        merchantPromotionGetPromoList.data.filterTab.map {
            filterItems.add(FilterPromoUiModel(it.id, it.name))
        }

        val list = merchantPromotionGetPromoList.data.pages.toMutableList()
        list.removeFirst { it.pageName == PAGE_NAME_TOPADS } // TopAds button has been migrated to sidebar menu

        val promotionItem =
            list.map {
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
