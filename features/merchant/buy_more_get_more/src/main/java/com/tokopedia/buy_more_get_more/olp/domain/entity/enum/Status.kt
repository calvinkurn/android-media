package com.tokopedia.buy_more_get_more.olp.domain.entity.enum

enum class Status(val code: Long) {
    INVALID_OFFER_ID(801),
    OFFER_ALREADY_FINISH(802),
    OOS(803),
    NO_CONNECTION(804),
    SUCCESS(200)
}
