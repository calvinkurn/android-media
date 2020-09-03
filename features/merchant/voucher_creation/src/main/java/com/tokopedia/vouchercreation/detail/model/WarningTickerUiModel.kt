package com.tokopedia.vouchercreation.detail.model

import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactory

class WarningTickerUiModel(
        val dataKey: String
): VoucherDetailUiModel {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int =
            typeFactory.type(this)
}