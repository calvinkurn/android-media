package com.tokopedia.videoTabComponent.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class VideoTabModule {

    @VideoTabScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
    @VideoTabScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)
    @Provides
    @VideoTabScope
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }
    @VideoTabScope
    @Provides
    fun provideGraphQlRepository(@ApplicationContext context: Context): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
//    @VideoTabScope
//    @Provides
//    fun providePlayWidget(playWidgetUseCase: PlayWidgetUseCase,
//                          mapperProviders: Map<PlayWidgetSize, @JvmSuppressWildcards PlayWidgetMapper>): PlayWidgetTools {
//        return PlayWidgetTools(playWidgetUseCase, mapperProviders)
//    }
}