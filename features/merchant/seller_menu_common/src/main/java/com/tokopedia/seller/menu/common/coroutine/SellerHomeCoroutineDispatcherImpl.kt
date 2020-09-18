package com.tokopedia.seller.menu.common.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created By @ilhamsuaib on 10/09/20
 */

object SellerHomeCoroutineDispatcherImpl : com.tokopedia.seller.menu.common.coroutine.SellerHomeCoroutineDispatcher {

    override fun io(): CoroutineDispatcher = Dispatchers.IO

    override fun main(): CoroutineDispatcher = Dispatchers.Main
}