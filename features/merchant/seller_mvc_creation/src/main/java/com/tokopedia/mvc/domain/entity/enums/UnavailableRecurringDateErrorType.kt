package com.tokopedia.mvc.domain.entity.enums

enum class UnavailableRecurringDateErrorType(val type: Int) {
    SAME_DATE_VOUCHER_ALREADY_EXIST(1),
    NEW_FOLLOWER_VOUCHER_ALREADY_EXIST(2),
    RUN_OUT_OF_QUOTA(3)
}
