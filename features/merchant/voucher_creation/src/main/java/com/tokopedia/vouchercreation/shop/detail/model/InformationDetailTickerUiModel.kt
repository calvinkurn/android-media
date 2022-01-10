package com.tokopedia.vouchercreation.shop.detail.model

import com.tokopedia.vouchercreation.shop.detail.view.adapter.factory.VoucherDetailAdapterFactory

class InformationDetailTickerUiModel(
        val initiallyCanEdit: Boolean = true
) : VoucherDetailUiModel {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int =
            typeFactory.type(this)
}