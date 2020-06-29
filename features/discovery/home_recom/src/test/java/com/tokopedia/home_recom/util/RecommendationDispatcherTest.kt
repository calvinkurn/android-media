package com.tokopedia.home_recom.util

import com.tokopedia.home_recom.view.dispatchers.RecommendationDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RecommendationDispatcherTest : RecommendationDispatcher{
    override fun getMainDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun getIODispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
}