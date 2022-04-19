package com.tokopedia.vouchercreation.shop.detail.model

import com.tokopedia.vouchercreation.shop.detail.view.adapter.factory.VoucherDetailAdapterFactory

/**
 * Created By @ilhamsuaib on 04/05/20
 */

data class DividerUiModel(
        val dividerHeight: Int = 1
) : VoucherDetailUiModel {

    companion object {
        const val THIN = 2
        const val THICK = 8
    }

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int = typeFactory.type(this)
}