package com.tokopedia.recharge_component.model.denom

import com.tokopedia.kotlin.model.ImpressHolder

data class DenomWidgetModel(
   val mainTitle: String = "",
   val subTitle: String = "",
   val listDenomData: List<DenomData> = emptyList(),
   val isFlashSale: Boolean = false,
   val isHorizontalMCCM: Boolean = true,
   val imageBackgroundUrl: String? = null,
   val imageBackgroundUrlDarkMode: String? = null,
   val showMoreText: String = "",
)

data class DenomData(
    val id: String = "",
    var position: Int = 0,
    val isSpecialPromo: Boolean = false,
    val promoStatus: String = "0",
    val status: Int = 0,
    val categoryId: String = "",
    val operatorId: String = "",
    val title: String = "",
    val description: String = "",
    val quotaInfo: String = "",
    val expiredDays: String = "",
    val expiredDate: String = "",
    val activePeriod: String = "",
    val specialLabel: String = "",
    val discountLabel: String = "",
    val slashPrice: String = "",
    val slashPricePlain: Int = 0,
    val flashSalePercentage: Int = 0,
    val flashSaleLabel: String = "",
    val price: String = "",
    val pricePlain: Int = 0,
    val isShowChevron: Boolean = true,
    val appLink: String = "",
    val productDescriptions: List<String> = listOf(),
    val greenLabel: String = "",
    val itemType: String = ""
): ImpressHolder()
