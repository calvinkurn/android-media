package com.tokopedia.sellerhome.stub.features.home.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.stub.data.UserSessionStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by @ilhamsuaib on 06/12/21.
 */

@Module
class SellerHomeModuleStub {

    @SellerHomeScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

    @SellerHomeScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SellerHomeScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl {
        return FirebaseRemoteConfigImpl(context)
    }

    @SellerHomeScope
    @Provides
    fun provideSellerHomeRemoteConfig(remoteConfig: FirebaseRemoteConfigImpl): SellerHomeRemoteConfig {
        return SellerHomeRemoteConfig(remoteConfig)
    }
}