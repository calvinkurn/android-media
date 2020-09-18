package com.tokopedia.sellerhomecommon.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created By @ilhamsuaib on 10/09/20
 */

interface SellerHomeCoroutineDispatcher {

    fun io(): CoroutineDispatcher

    fun main(): CoroutineDispatcher
}