package com.tokopedia.home_recom.view.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface RecommendationDispatcher{
    fun getMainDispatcher(): CoroutineDispatcher
    fun getIODispatcher(): CoroutineDispatcher
}

class RecommendationDispatcherImpl : RecommendationDispatcher{
    override fun getMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    override fun getIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}