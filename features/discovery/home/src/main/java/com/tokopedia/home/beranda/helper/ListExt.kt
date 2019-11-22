package com.tokopedia.home.beranda.helper

fun <T> List<T>?.copy(): List<T>{
    return if(this != null) ArrayList(this)
    else ArrayList()
}