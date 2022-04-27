package com.tokopedia.loginregister.invalid_phone_number.data

enum class StatusPhoneNumber(val value: Int) {
    ACTIVE(1),
    BANNED(2),
    RECOVERY(3),
    PENDING(-1),
    NOT_EXIST(0)
}