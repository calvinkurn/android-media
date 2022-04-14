package com.tokopedia.shopdiscount.search.domain.entity

import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING

enum class CategoryFilter(val value: String) {
    BUYER("buyer"),
    SELLER("seller"),
    SHOW_ALL(EMPTY_STRING)
}