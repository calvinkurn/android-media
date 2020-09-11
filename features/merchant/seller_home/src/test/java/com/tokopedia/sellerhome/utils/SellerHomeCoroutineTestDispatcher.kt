package com.tokopedia.sellerhome.utils

import com.tokopedia.sellerhome.common.coroutine.SellerHomeCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

/**
 * Created By @ilhamsuaib on 10/09/20
 */

object SellerHomeCoroutineTestDispatcher : SellerHomeCoroutineDispatcher {

    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun main(): CoroutineDispatcher = Dispatchers.Unconfined

    @ExperimentalCoroutinesApi
    fun testCoroutineDispatcher(): CoroutineDispatcher = TestCoroutineDispatcher()
}