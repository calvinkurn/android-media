package com.tokopedia.officialstore

import kotlinx.coroutines.CoroutineDispatcher

interface OfficialStoreDispatcherProvider {
    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}