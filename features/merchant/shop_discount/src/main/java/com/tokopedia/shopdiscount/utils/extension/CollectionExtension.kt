package com.tokopedia.shopdiscount.utils.extension


fun <T> Collection<T>.allCheckEmptyList(predicate: (T) -> Boolean): Boolean {
    if (isEmpty()) return false
    for (element in this) if (!predicate(element)) return false
    return true
}

fun <E> MutableList<E>.setElement(index: Int, element: E){
    if(index in 0 until size){
        set(index, element)
    }
}
