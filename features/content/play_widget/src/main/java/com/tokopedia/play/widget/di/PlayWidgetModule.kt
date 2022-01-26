package com.tokopedia.play.widget.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMapper
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by jegul on 09/11/20
 */
@Module
class PlayWidgetModule {

    @Provides
    fun provideWidgetMapper(userSession: UserSessionInterface): PlayWidgetUiMapper {
        return PlayWidgetUiMapper(userSession)
    }

    @Provides
    fun providePlayWidgetUpdateChannelUseCase(graphqlRepository: GraphqlRepository): PlayWidgetUpdateChannelUseCase {
        return PlayWidgetUpdateChannelUseCase(graphqlRepository)
    }
}