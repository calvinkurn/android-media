package com.tokopedia.vouchercreation.create.view.uimodel.voucherimage

import com.tokopedia.vouchercreation.create.view.enums.PromotionType

interface VoucherImage {
    val promotionType: PromotionType
    val shopName: String
    val shopAvatar: String
    val promoName: String
}