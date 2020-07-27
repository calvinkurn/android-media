package com.tokopedia.topads.auto.di

import kotlinx.coroutines.CoroutineDispatcher

interface AutoAdsDispatcherProvider {

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}