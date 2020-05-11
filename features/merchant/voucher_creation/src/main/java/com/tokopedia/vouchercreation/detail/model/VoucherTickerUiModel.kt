package com.tokopedia.vouchercreation.detail.model

import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactory

/**
 * Created By @ilhamsuaib on 10/05/20
 */

data class VoucherTickerUiModel(
        val title: String,
        val description: String,
        val nominalStr: String,
        val hasTooltip: Boolean = false
) : VoucherDetailUiModel {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}