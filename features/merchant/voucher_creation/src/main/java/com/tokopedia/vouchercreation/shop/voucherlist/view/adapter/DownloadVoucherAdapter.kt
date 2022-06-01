package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.DownloadVoucherFactoryImpl

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class DownloadVoucherAdapter : BaseAdapter<DownloadVoucherFactoryImpl>(DownloadVoucherFactoryImpl()) {

    val items: List<DownloadVoucherUiModel>
        get() = visitables.filterIsInstance<DownloadVoucherUiModel>()
}