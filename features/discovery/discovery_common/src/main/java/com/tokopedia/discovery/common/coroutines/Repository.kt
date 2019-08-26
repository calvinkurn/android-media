package com.tokopedia.discovery.common.coroutines

interface Repository<T> {

    suspend fun query(params: Map<String, Any?>): T
}
