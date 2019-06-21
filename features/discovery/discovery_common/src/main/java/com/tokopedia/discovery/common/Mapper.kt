package com.tokopedia.discovery.common

interface Mapper<From, To> {

    fun convert(source : From) : To
}