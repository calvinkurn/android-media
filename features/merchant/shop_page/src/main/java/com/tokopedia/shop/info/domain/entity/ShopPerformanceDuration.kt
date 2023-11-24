package com.tokopedia.shop.info.domain.entity

sealed interface ShopPerformanceDuration {
    data class Minute(val value: Int) : ShopPerformanceDuration
    data class Hour(val value: Int) : ShopPerformanceDuration
    data class Day(val value: Int) : ShopPerformanceDuration
}
