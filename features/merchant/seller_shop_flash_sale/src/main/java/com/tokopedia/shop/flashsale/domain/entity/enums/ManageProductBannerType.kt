package com.tokopedia.shop.flashsale.domain.entity.enums

const val EMPTY_BANNER = 0
const val ERROR_BANNER = 1
const val HIDE_BANNER = 2

enum class ManageProductBannerType(val type: Int) {
    EMPTY(EMPTY_BANNER),
    ERROR(ERROR_BANNER),
    HIDE(HIDE_BANNER)
}