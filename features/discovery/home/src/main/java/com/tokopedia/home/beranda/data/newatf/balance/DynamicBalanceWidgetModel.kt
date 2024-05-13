package com.tokopedia.home.beranda.data.newatf.balance

import com.tokopedia.home_component.model.AtfContent

data class DynamicBalanceWidgetModel (
    val balanceItems: MutableList<BalanceItemModel> = mutableListOf(),
): AtfContent
