package com.tokopedia.vouchercreation.create.view.uimodel.voucherimage

import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType

data class BannerVoucherUiModel(
        override var imageType: VoucherImageType,
        override val promoName: String,
        override val shopName: String,
        override val shopAvatar: String,
        val bannerUrl: String) : VoucherImage