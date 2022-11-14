package com.tokopedia.privacycenter.consentwithdrawal.ui.adapter.uimodel

data class TitleDividerUiModel(
    val title: String = "",
    val isDivider: Boolean,
    val isSmallDivider: Boolean = false
) : ConsentWithdrawalUiModel
