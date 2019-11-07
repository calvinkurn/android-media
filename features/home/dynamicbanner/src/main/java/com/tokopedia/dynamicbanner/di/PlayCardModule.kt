package com.tokopedia.dynamicbanner.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.dynamicbanner.QUERY_PLAY_CARD
import com.tokopedia.dynamicbanner.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module class PlayCardModule {

    @Provides
    @Named(QUERY_PLAY_CARD)
    fun provideRawPlayQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_home_play)
    }

}