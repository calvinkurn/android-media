package com.tokopedia.mvc.util.extension

import com.tokopedia.mvc.domain.entity.enums.VoucherTarget


fun VoucherTarget.isPrivate(): Boolean {
    return this == VoucherTarget.PRIVATE
}

fun VoucherTarget.isPublic(): Boolean {
    return this == VoucherTarget.PUBLIC
}
