package com.tokopedia.home.util

fun isHomeRevampInstance(): Boolean {
    return true
}

fun isHomeRevampOld(): Boolean {
    return false
}

fun homeRevampTestCondition(ifHomeRevamp: ()-> Unit = {}, ifHomeOld: ()-> Unit = {}) {
    if (isHomeRevampInstance()) {
        ifHomeRevamp.invoke()
    } else if (isHomeRevampOld()) {
        ifHomeOld.invoke()
    }
}