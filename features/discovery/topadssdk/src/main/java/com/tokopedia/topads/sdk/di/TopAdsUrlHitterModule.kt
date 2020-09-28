package com.tokopedia.topads.sdk.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import dagger.Module
import dagger.Provides

@Module
class TopAdsUrlHitterModule {

    @Provides
    fun topAdsUrlHitter(@ApplicationContext context: Context): TopAdsUrlHitter {
        return TopAdsUrlHitter(context)
    }

}