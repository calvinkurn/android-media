package com.tokopedia.buyerorderdetail.presentation.model


data class OwocGroupedOrderWrapper(
    val owocGroupedOrderList: List<BaseOwocSectionGroupUiModel> = emptyList()
)
