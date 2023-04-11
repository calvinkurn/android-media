package com.tokopedia.kol.feature.postdetail.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.domain.SUSPEND_GRAPHQL_REPOSITORY
import com.tokopedia.feedcomponent.people.mapper.ProfileMutationMapper
import com.tokopedia.feedcomponent.people.mapper.ProfileMutationMapperImpl
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kol.feature.postdetail.data.mapper.ContentDetailMapperImpl
import com.tokopedia.kol.feature.postdetail.di.ContentDetailScope
import com.tokopedia.kol.feature.postdetail.domain.mapper.ContentDetailMapper
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by meyta.taliti on 02/08/22.
 */
@Module
class ContentDetailModule {

    @ContentDetailScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ContentDetailScope
    @Provides
    fun provideToggleFavouriteShopUseCase(@ApplicationContext context: Context): ToggleFavouriteShopUseCase {
        return ToggleFavouriteShopUseCase(GraphqlUseCase(), context.resources)
    }

    @ContentDetailScope
    @Named(SUSPEND_GRAPHQL_REPOSITORY)
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @ContentDetailScope
    @Provides
    fun provideGqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @ContentDetailScope
    @Provides
    fun provideMapper(): ContentDetailMapper {
        return ContentDetailMapperImpl()
    }

    @ContentDetailScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)

    @ContentDetailScope
    @Provides
    fun provideProfileUiMapper(@ApplicationContext context: Context): ProfileMutationMapper {
        return ProfileMutationMapperImpl(context)
    }
}
