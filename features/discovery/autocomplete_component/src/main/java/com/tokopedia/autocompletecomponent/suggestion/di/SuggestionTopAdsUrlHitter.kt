package com.tokopedia.autocompletecomponent.suggestion.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import dagger.Module
import dagger.Provides

@Module
class SuggestionTopAdsUrlHitter {

    @SuggestionScope
    @Provides
    fun provideTopAdsUrlHitter(@ApplicationContext context: Context): TopAdsUrlHitter =
        TopAdsUrlHitter(context)
}