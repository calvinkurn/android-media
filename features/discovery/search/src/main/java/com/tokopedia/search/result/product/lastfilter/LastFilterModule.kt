package com.tokopedia.search.result.product.lastfilter

import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
object LastFilterModule {

    @JvmStatic
    @Provides
    @SearchScope
    fun provideLastFilterListener(
        lastFilterListener: LastFilterListenerDelegate
    ): LastFilterListener {
        return lastFilterListener
    }
}
