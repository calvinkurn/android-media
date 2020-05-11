package com.tokopedia.vouchercreation.create.view.typefactory.voucherimage

import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel

interface VoucherImageTypeFactory {
    fun<T : VoucherImageTypeFactory> type(bannerVoucherUiModel: BannerVoucherUiModel<T>): Int
}