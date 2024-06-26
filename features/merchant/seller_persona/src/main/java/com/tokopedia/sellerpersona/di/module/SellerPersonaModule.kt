package com.tokopedia.sellerpersona.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.sellerpersona.data.local.PersonaSharedPref
import com.tokopedia.sellerpersona.data.local.PersonaSharedPrefInterface
import com.tokopedia.sellerpersona.di.SellerPersonaScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by @ilhamsuaib on 24/01/23.
 */

@Module
class SellerPersonaModule {

    @SellerPersonaScope
    @Provides
    fun providePersonaSharedPref(@ApplicationContext context: Context): PersonaSharedPrefInterface {
        return PersonaSharedPref(context)
    }

    @SellerPersonaScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @SellerPersonaScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SellerPersonaScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}