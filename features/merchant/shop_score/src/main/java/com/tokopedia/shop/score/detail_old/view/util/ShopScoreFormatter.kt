package com.tokopedia.shop.score.detail_old.view.util

fun Float.formatShopScore(): String {
    return if(this % 1 == 0f) {
        this.toInt().toString()
    } else {
        this.toString()
    }
}