package com.tokopedia.statistic.common.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created By @ilhamsuaib on 20/07/20
 */

interface DispatchersProvider {

    fun io(): CoroutineDispatcher

    fun main(): CoroutineDispatcher
}