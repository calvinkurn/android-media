package com.tokopedia.vouchercreation.create.view.uimodel.voucherimage

import com.tokopedia.vouchercreation.create.view.enums.PromotionType

data class BannerVoucherUiModel(
        override val promotionType: PromotionType,
        override val promoName: String,
        override val shopName: String,
        override val shopAvatar: String
) : VoucherImage