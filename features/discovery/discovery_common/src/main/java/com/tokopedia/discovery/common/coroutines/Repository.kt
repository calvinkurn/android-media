package com.tokopedia.discovery.common.coroutines

interface Repository<T> {

    suspend fun getResponse(inputParameter: Map<String, Any>): T
}
