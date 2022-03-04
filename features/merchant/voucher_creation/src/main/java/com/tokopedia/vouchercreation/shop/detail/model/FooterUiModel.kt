package com.tokopedia.vouchercreation.shop.detail.model

import com.tokopedia.vouchercreation.shop.detail.view.adapter.factory.VoucherDetailAdapterFactory

/**
 * Created By @ilhamsuaib on 06/05/20
 */

data class FooterUiModel(
        val footerText: String,
        val clickableText: String
) : VoucherDetailUiModel {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}