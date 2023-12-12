package com.tokopedia.statistic.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.sellerhomecommon.common.di.annotation.ActivityContext
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPref
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.statistic.di.StatisticScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By @ilhamsuaib on 09/06/20
 */

@Module
class StatisticModule(private val context: Context) {

    @StatisticScope
    @Provides
    @ActivityContext
    fun providesActivityContext() = context

    @StatisticScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @StatisticScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @StatisticScope
    @Provides
    fun provideWidgetLastUpdatePref(@ApplicationContext context: Context): WidgetLastUpdatedSharedPrefInterface {
        return WidgetLastUpdatedSharedPref(context)
    }

    @StatisticScope
    @Provides
    fun provideLastUpdatedInfoEnabled(): Boolean {
        return false
    }

    @StatisticScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}