package com.tokopedia.discovery.common

interface RepositoryKt<I, O> {

    fun getResponse(inputParameter: I): O
}