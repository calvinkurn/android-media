package com.tokopedia.vouchercreation.voucherlist.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.DownloadVoucherFactory

/**
 * Created By @ilhamsuaib on 28/04/20
 */

data class DownloadVoucherUiModel(
        val isSelected: Boolean,
        val ratioStr: String,
        val description: String
) : Visitable<DownloadVoucherFactory> {

    override fun type(typeFactory: DownloadVoucherFactory): Int {
        return typeFactory.type(this)
    }
}