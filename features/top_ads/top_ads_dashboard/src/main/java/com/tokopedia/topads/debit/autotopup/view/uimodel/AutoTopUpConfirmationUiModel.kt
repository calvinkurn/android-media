package com.tokopedia.topads.debit.autotopup.view.uimodel


data class AutoTopUpConfirmationUiModel(
    val selectedItemId: String,
    val topAdsCredit: String,
    val topAdsBonus: String,
    val frequencyText: String,
    val autoTopUpFrequencySelected: Int,
    val subTotalActual: String,
    val subTotalStrikethrough: String,
    val ppnPercent: String,
    val ppnAmount: String,
    val totalAmount: String
)
