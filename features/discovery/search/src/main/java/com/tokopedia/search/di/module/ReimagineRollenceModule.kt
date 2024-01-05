package com.tokopedia.search.di.module

import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class ReimagineRollenceModule {

    @SearchScope
    @Provides
    fun provideReimagineRollence(): ReimagineRollence = ReimagineRollence()
}
