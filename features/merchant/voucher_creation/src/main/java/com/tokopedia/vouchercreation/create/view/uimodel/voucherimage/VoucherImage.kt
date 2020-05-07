package com.tokopedia.vouchercreation.create.view.uimodel.voucherimage

import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType

interface VoucherImage {
    val imageType: VoucherImageType?
    val shopName: String
    val shopAvatar: String
    val promoName: String
}