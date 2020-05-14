package com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.DownloadVoucherFactory

/**
 * Created By @ilhamsuaib on 28/04/20
 */

data class DownloadVoucherUiModel(
        var isSelected: Boolean,
        val ratioStr: String,
        val description: String,
        val downloadVoucherType: DownloadVoucherType
) : Visitable<DownloadVoucherFactory> {

    override fun type(typeFactory: DownloadVoucherFactory): Int {
        return typeFactory.type(this)
    }
}