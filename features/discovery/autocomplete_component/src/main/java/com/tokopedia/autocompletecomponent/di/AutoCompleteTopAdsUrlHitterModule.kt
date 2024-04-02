package com.tokopedia.autocompletecomponent.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import dagger.Module
import dagger.Provides

@Module
class AutoCompleteTopAdsUrlHitterModule {
    @AutoCompleteScope
    @Provides
    fun provideTopAdsUrlHitter(@ApplicationContext context: Context): TopAdsUrlHitter =
        TopAdsUrlHitter(context)
}
