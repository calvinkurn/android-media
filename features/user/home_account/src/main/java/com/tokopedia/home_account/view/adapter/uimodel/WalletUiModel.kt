package com.tokopedia.home_account.view.adapter.uimodel

data class WalletUiModel(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var urlImage: String = "",
    var applink: String = "",
    var weblink: String = "",
    var isFailed: Boolean = false,
    var isActive: Boolean = true
)