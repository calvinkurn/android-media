package com.tokopedia.discovery.common.coroutines

interface Repository<I, O> {

    suspend fun getResponse(inputParameter: I): O
}
