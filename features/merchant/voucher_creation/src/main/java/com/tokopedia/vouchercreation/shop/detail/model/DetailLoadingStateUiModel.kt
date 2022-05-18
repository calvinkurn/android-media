package com.tokopedia.vouchercreation.shop.detail.model

import com.tokopedia.vouchercreation.shop.detail.view.adapter.factory.VoucherDetailAdapterFactory

class DetailLoadingStateUiModel : VoucherDetailUiModel {
    override fun type(typeFactory: VoucherDetailAdapterFactory): Int =
            typeFactory.type(this)
}