package com.tokopedia.tokopedianow.common.helper

interface ResourceProvider {
    fun getString(resId: Int): String
    fun getString(resId: Int, arg: Int): String
    fun getColor(resId: Int): Int
}

