package com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item

data class CashbackPercentageInfoUiModel (
        val minimumPurchase: Int,
        val cashbackPercentage: Int,
        val minimumDiscount: Int,
        val maximumDiscount: Int = 0)