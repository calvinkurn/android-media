package com.tokopedia.vouchercreation.product.download

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.DownloadVoucherFactoryImpl

class DownloadCouponImageAdapter : BaseAdapter<DownloadVoucherFactoryImpl>(DownloadVoucherFactoryImpl()) {

    val items: List<CouponImageUiModel>
        get() = visitables.filterIsInstance<CouponImageUiModel>()
}