package com.tokopedia.home.beranda.data.newatf.balance

import com.tokopedia.home_component.model.AtfContent

data class DynamicBalanceWidgetModel (
    val balanceItems: List<BalanceItemModel> = mutableListOf(),
    val isLoggedIn: Boolean = true,
): AtfContent
