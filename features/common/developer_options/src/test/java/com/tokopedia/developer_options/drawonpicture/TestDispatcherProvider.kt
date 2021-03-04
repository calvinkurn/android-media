package com.tokopedia.developer_options.drawonpicture

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by furqan on 01/10/2020
 */
class TestDispatcherProvider : DispatcherProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun ui(): CoroutineDispatcher = Dispatchers.Unconfined
}