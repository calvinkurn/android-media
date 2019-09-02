package com.tokopedia.search.utils

fun <T> List<T>?.betweenFirstAndLast(): List<T> {
    if (this == null) return listOf()
    if (this.size < 3) return listOf()

    return this.subList(1, this.size - 1)
}

fun <T> List<T>?.secondToLast(): List<T> {
    if (this == null) return listOf()
    if (this.size < 2) return listOf()

    return this.subList(1, this.size)
}