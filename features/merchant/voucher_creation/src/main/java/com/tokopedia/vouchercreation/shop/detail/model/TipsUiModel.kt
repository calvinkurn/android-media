package com.tokopedia.vouchercreation.shop.detail.model

import com.tokopedia.vouchercreation.shop.detail.view.adapter.factory.VoucherDetailAdapterFactory

/**
 * Created By @ilhamsuaib on 04/05/20
 */

data class TipsUiModel(
        val tips: String,
        val clickableText: String
) : VoucherDetailUiModel {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int = typeFactory.type(this)
}