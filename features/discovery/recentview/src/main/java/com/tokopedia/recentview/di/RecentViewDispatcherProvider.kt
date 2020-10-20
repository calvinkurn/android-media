package com.tokopedia.recentview.di

import kotlinx.coroutines.CoroutineDispatcher

interface RecentViewDispatcherProvider {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}