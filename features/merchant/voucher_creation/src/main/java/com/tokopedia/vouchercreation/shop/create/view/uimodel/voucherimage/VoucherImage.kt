package com.tokopedia.vouchercreation.shop.create.view.uimodel.voucherimage

import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherImageType

interface VoucherImage {
    var imageType: VoucherImageType
    val shopName: String
    val shopAvatar: String
    val promoName: String
}