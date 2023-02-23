package com.tokopedia.topads.common.extension


val Int.Companion.EIGHT get() = 8
val Long.Companion.ZERO get() = 0L

fun <E> MutableList<E>.createListOfSize(topUpCreditItemData: E, size: Int): MutableList<E> {
    for (i in 1..size) {
        this.add(topUpCreditItemData)
    }
    return this
}
