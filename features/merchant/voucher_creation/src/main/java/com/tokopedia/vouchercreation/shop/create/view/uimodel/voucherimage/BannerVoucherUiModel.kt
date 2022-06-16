package com.tokopedia.vouchercreation.shop.create.view.uimodel.voucherimage

import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherImageType

data class BannerVoucherUiModel(
        override var imageType: VoucherImageType,
        override var promoName: String,
        override var shopName: String,
        override var shopAvatar: String) : VoucherImage