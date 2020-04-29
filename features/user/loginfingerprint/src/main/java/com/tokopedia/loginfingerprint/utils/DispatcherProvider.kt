package com.tokopedia.loginfingerprint.utils

/**
 * Created by Yoris Prayogo on 2020-02-21.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun ui(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
}