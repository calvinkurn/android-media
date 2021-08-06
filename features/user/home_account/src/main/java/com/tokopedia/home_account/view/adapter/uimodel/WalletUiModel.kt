package com.tokopedia.home_account.view.adapter.uimodel

data class WalletUiModel(
    val title: String,
    val subtitle: String,
    val urlImage: String,
    val isShowActionImage: Boolean,
    val actionText: String,
    val type: String,
    val isNotConnected: Boolean
)