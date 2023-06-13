package com.tokopedia.mvc.util.extension

import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer


fun VoucherTargetBuyer.isAllBuyer(): Boolean {
    return this == VoucherTargetBuyer.ALL_BUYER
}

fun VoucherTargetBuyer.isNewFollower(): Boolean {
    return this == VoucherTargetBuyer.NEW_FOLLOWER
}
