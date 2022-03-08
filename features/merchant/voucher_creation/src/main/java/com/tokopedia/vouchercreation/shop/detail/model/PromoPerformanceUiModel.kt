package com.tokopedia.vouchercreation.shop.detail.model

import com.tokopedia.vouchercreation.shop.detail.view.adapter.factory.VoucherDetailAdapterFactory

/**
 * Created By @ilhamsuaib on 05/05/20
 */

data class PromoPerformanceUiModel(
        val totalSpending: String,
        val voucherUsed: Int,
        val voucherQuota: Int
) : VoucherDetailUiModel {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}