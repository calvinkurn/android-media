package com.tokopedia.play.widget.di

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
}