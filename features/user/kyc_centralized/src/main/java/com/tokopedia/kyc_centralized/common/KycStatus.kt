package com.tokopedia.kyc_centralized.common

enum class KycStatus(val code: Int) {
    DEFAULT(-3),
    ERROR(-2),
    REJECTED(-1),
    PENDING(0),
    VERIFIED(1),
    EXPIRED(2),
    NOT_VERIFIED(3),
    APPROVED(4),
    BLACKLISTED(5);

    companion object {
        fun map(code: Int): KycStatus? {
            val map = values().associateBy(KycStatus::code)
            return map[code]
        }
    }
}