package com.tokopedia.shop.score.view.util

fun Float.formatShopScore(): String {
    return if(this % 1 == 0f) {
        this.toInt().toString()
    } else {
        this.toString()
    }
}