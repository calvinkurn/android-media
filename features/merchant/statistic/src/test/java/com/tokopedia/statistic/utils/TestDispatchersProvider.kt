package com.tokopedia.statistic.utils

import com.tokopedia.statistic.common.coroutine.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created By @ilhamsuaib on 20/07/20
 */

object TestDispatchersProvider : DispatchersProvider {

    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun main(): CoroutineDispatcher = Dispatchers.Unconfined
}