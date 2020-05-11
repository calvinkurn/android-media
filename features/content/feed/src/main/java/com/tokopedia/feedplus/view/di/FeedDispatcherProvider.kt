package com.tokopedia.feedplus.view.di

import kotlinx.coroutines.CoroutineDispatcher

interface FeedDispatcherProvider {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}