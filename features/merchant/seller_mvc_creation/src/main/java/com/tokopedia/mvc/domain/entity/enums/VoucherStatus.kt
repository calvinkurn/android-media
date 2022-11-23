package com.tokopedia.mvc.domain.entity.enums

enum class VoucherStatus(val type: Int) {
    DELETED(-1),
    PROCESSING(0),
    NOT_STARTED(1),
    ONGOING(2),
    ENDED(3),
    STOPPED(4)
}
