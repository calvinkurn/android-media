package com.tokopedia.play.widget.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import dagger.Module
import dagger.Provides

/**
 * Created by jegul on 09/11/20
 */
@Module(includes = [PlayWidgetMapperModule::class])
class PlayWidgetModule {

    @Provides
    fun providePlayWidgetUpdateChannelUseCase(graphqlRepository: GraphqlRepository): PlayWidgetUpdateChannelUseCase {
        return PlayWidgetUpdateChannelUseCase(graphqlRepository)
    }

    @Provides
    fun providePlayWidgetConfigMapper(@ApplicationContext context: Context): SharedPreferences {
        /** TODO("dont use deprecated way") */
        return PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }
}