package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class ReimagineRollenceModule {

    @SearchScope
    @Provides
    fun provideReimagineRollence(@SearchContext context: Context): ReimagineRollence =
        ReimagineRollence(context)
}
