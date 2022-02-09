package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherUiModel
import com.tokopedia.vouchercreation.product.download.CouponImageUiModel

/**
 * Created By @ilhamsuaib on 28/04/20
 */

interface DownloadVoucherFactory {

    fun type(model: DownloadVoucherUiModel): Int
    fun type(model: CouponImageUiModel) : Int
}