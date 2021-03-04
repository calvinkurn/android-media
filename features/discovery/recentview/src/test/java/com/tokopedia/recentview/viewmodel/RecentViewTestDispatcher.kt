package com.tokopedia.recentview.viewmodel

import com.tokopedia.recentview.di.RecentViewDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Lukas on 13/11/20.
 */
class RecentViewTestDispatcher : RecentViewDispatcherProvider{
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun ui(): CoroutineDispatcher = Dispatchers.Unconfined

}