package com.tokopedia.v2.home.util

fun <T> List<T>?.copy(): List<T>{
    return if(this != null) ArrayList(this)
    else ArrayList()
}