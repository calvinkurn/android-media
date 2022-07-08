package com.tokopedia.shop.flashsale.domain.entity.enums

enum class ManageProductErrorType {
    MAX_DISCOUNT_PRICE,
    MAX_DISCOUNT_PRICE_AND_OTHER,
    MAX_STOCK,
    MAX_STOCK_AND_OTHER,
    MIN_DISCOUNT_PRICE,
    MIN_DISCOUNT_PRICE_AND_OTHER,
    MIN_STOCK,
    MIN_STOCK_AND_OTHER,
    MAX_ORDER,
    MIN_ORDER,
    EMPTY_PRICE,
    NOT_ERROR
}