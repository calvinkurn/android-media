package com.tokopedia.recharge_component.model.denom

data class DenomWidgetModel(
   val mainTitle: String = "",
   val subTitle: String = "",
   val textColor: String = "",
   val listDenomData: List<DenomData> = emptyList()
)

data class DenomData(
    val title: String = "",
    val description: String = "",
    val quotaInfo: String = "",
    val expiredDays: String = "",
    val expiredDate: String = "",
    val specialLabel: String = "",
    val discountLabel: String = "",
    val slashPrice: String = "",
    val flashSalePercentage: Int = 0,
    val flashSaleLabel: String = "",
    val price: String = "",
    val pricePlain: Int = 0,
    val isShowChevron: Boolean = true,
    val appLink: String = "",
)
