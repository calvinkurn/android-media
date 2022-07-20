package com.tokopedia.home_account.view.adapter.uimodel

data class BalanceAndPointUiModel(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var urlImage: String = "",
    var applink: String = "",
    var isFailed: Boolean = false,
    var isActive: Boolean = true,
    var hideTitle: Boolean = false
)