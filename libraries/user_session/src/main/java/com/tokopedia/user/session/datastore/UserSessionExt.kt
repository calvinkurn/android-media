package com.tokopedia.user.session.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

suspend fun Flow<String>.value(): String {
    return this.first()
}

fun <T> Flow<T>.toBlocking(): T {
    return runBlocking {
        first()
    }
}