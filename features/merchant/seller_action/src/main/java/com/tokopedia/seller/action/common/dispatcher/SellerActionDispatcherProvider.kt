package com.tokopedia.seller.action.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface SellerActionDispatcherProvider {

    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher

}