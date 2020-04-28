package com.tokopedia.vouchercreation.voucherlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.vouchercreation.voucherlist.model.DownloadVoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.DownloadVoucherFactoryImpl

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class DownloadVoucherAdapter : BaseAdapter<DownloadVoucherFactoryImpl>(DownloadVoucherFactoryImpl()) {

    val items: List<DownloadVoucherUiModel>
        get() = visitables.filterIsInstance<DownloadVoucherUiModel>()
}