package com.tokopedia.discovery.find.di.module

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.discovery.find.di.scope.FindNavScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tradein_common.repository.BaseRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides

@FindNavScope
@Module
class FindNavModule {

    @FindNavScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @FindNavScope
    @Provides
    fun provideGqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @FindNavScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @FindNavScope
    @Provides
    fun provideGcmHandler(@ApplicationContext context: Context): GCMHandler {
        return GCMHandler(context)
    }

    @FindNavScope
    @Provides
    fun provideResources(context: Context): Resources {
        return context.resources
    }

    @FindNavScope
    @Provides
    fun provideGraphQlRepo(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @FindNavScope
    @Provides
    fun provideBaseRepo(): BaseRepository {
        return BaseRepository.repositoryInstance
    }

    @FindNavScope
    @Provides
    fun getAddWishListUseCase(context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @FindNavScope
    @Provides
    fun getRemoveWishListUseCase(context: Context): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

}