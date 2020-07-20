package com.tokopedia.oneclickcheckout.order.view.model

data class InstallmentDetailItem(
        val term: Int = 0,
        val mdr: Float = 0f,
        val mdrSubsidize: Float = 0f,
        val minAmount: Long = 0,
        val isSelected: Boolean = false,
        val fee: Long = 0,
        val monthlyAmount: Long = 0
)