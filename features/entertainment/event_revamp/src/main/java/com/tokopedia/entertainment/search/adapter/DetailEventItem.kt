package com.tokopedia.entertainment.search.adapter

interface DetailEventItem<T> {
    fun type(typeFactory: T) : Int
}