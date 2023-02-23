package com.tokopedia.mvc.util.extension

import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType


fun VoucherServiceType.isShopVoucher(): Boolean {
    return this == VoucherServiceType.SHOP_VOUCHER
}

fun VoucherServiceType.isProductVoucher(): Boolean {
    return this == VoucherServiceType.PRODUCT_VOUCHER
}
