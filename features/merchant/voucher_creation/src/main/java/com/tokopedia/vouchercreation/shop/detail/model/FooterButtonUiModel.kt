package com.tokopedia.vouchercreation.shop.detail.model

import com.tokopedia.vouchercreation.shop.detail.view.adapter.factory.VoucherDetailAdapterFactory

/**
 * Created By @ilhamsuaib on 05/05/20
 */

data class FooterButtonUiModel(
        val ctaText: String,
        val appLink: String,
        val canLoad: Boolean = false,
        var isLoading: Boolean = false
) : VoucherDetailUiModel {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}