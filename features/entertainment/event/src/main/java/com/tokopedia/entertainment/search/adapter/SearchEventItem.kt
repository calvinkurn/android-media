package com.tokopedia.entertainment.search.adapter

interface SearchEventItem<T> {

    fun type(typeFactory: T) : Int
}