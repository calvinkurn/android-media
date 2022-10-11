package com.tokopedia.tokopedianow.home.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.widget.analytic.impression.DefaultImpressionValidator
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMapper
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics
import com.tokopedia.tokopedianow.home.di.scope.HomeScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module(includes = [
    PlayWidgetModule::class
])
class HomeModule {

    @HomeScope
    @Provides
    fun provideHomeAnalytic(userSession: UserSessionInterface): HomeAnalytics {
        return HomeAnalytics(userSession)
    }

    @HomeScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @HomeScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @HomeScope
    @Provides
    fun provideGetRecommendationUseCase(@ApplicationContext context: Context, coroutineGqlRepository: GraphqlRepository): GetRecommendationUseCase = GetRecommendationUseCase(context, coroutineGqlRepository)

    @HomeScope
    @Provides
    fun providePlayWidget(
        playWidgetUseCase: PlayWidgetUseCase,
        playWidgetReminderUseCase: Lazy<PlayWidgetReminderUseCase>,
        playWidgetUpdateChannelUseCase: Lazy<PlayWidgetUpdateChannelUseCase>,
        mapper: PlayWidgetUiMapper,
        connectionUtil: PlayWidgetConnectionUtil
    ): PlayWidgetTools {
        return PlayWidgetTools(
            playWidgetUseCase,
            playWidgetReminderUseCase,
            playWidgetUpdateChannelUseCase,
            mapper,
            connectionUtil
        )
    }

    @HomeScope
    @Provides
    fun providePlayWidgetImpressionValidator(): DefaultImpressionValidator {
        return DefaultImpressionValidator()
    }
}