package com.tokopedia.shop.settings.common.util

import kotlinx.coroutines.CoroutineDispatcher

interface ShopSettingDispatcherProvider {

    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher

}