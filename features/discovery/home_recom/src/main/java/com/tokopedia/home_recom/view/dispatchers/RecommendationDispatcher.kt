package com.tokopedia.home_recom.view.dispatchers

import kotlinx.coroutines.Dispatchers

class RecommendationDispatcher{
    fun getMainDispatcher() = Dispatchers.Main

    fun getIODispatcher() = Dispatchers.IO
}