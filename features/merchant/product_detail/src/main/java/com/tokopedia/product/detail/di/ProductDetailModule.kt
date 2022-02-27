package com.tokopedia.product.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliatecommon.di.AffiliateCommonModule
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.ui.type.PlayWidgetSize
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.product.detail.di.RawQueryKeyConstant.QUERY_DISCUSSION_MOST_HELPFUL
import com.tokopedia.product.detail.usecase.DiscussionMostHelpfulUseCase
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import dagger.Module
import dagger.Provides

@Module(includes = [RecommendationModule::class, AffiliateCommonModule::class, PlayWidgetModule::class])
class ProductDetailModule {

    @ProductDetailScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideGraphQlRepository() = Interactor.getInstance().graphqlRepository

    @ProductDetailScope
    @Provides
    fun provideDiscussionMostHelpfulUseCase(rawQueries: Map<String, String>, graphqlRepository: GraphqlRepository): DiscussionMostHelpfulUseCase =
            DiscussionMostHelpfulUseCase(rawQueries[QUERY_DISCUSSION_MOST_HELPFUL]
                    ?: "", graphqlRepository)

    @ProductDetailScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @ProductDetailScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)

    @ProductDetailScope
    @Provides
    fun provideGetRecommendationFilterChips(graphqlRepository: GraphqlRepository): GetRecommendationFilterChips {
        val graphqlUseCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<RecommendationFilterChipsEntity>(graphqlRepository)
        return GetRecommendationFilterChips(graphqlUseCase)
    }

    @ProductDetailScope
    @Provides
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface, topAdsIrisSession: TopAdsIrisSession): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository(), topAdsIrisSession.getSessionId())
    }

    @ProductDetailScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @ProductDetailScope
    @Provides
    fun providePlayWidget(
            playWidgetUseCase: PlayWidgetUseCase,
            playWidgetReminderUseCase: Lazy<PlayWidgetReminderUseCase>,
            playWidgetUpdateChannelUseCase: Lazy<PlayWidgetUpdateChannelUseCase>,
            mapperProviders: Map<PlayWidgetSize, @JvmSuppressWildcards PlayWidgetMapper>,
            connectionUtil: PlayWidgetConnectionUtil
    ): PlayWidgetTools {
        return PlayWidgetTools(
                playWidgetUseCase,
                playWidgetReminderUseCase,
                playWidgetUpdateChannelUseCase,
                mapperProviders,
                connectionUtil
        )
    }

}