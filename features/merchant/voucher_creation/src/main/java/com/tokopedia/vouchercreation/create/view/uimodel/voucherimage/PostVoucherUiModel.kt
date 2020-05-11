package com.tokopedia.vouchercreation.create.view.uimodel.voucherimage

import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType

data class PostVoucherUiModel(
        override var imageType: VoucherImageType,
        override val promoName: String,
        override val shopAvatar: String,
        override val shopName: String,
        val promoCode: String,
        val promoPeriod: String
) : VoucherImage