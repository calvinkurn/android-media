package com.tokopedia.sellerhome.utils

import com.tokopedia.sellerhome.common.coroutine.SellerHomeCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

/**
 * Created By @ilhamsuaib on 10/09/20
 */

@ExperimentalCoroutinesApi
object SellerHomeCoroutineTestDispatcher : SellerHomeCoroutineDispatcher {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    override fun io(): CoroutineDispatcher = testCoroutineDispatcher

    override fun main(): CoroutineDispatcher = testCoroutineDispatcher
}