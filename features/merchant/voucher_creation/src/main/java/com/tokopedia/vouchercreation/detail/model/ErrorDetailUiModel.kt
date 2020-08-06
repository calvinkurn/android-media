package com.tokopedia.vouchercreation.detail.model

import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactory

object ErrorDetailUiModel : VoucherDetailUiModel {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int =
            typeFactory.type(this)
}