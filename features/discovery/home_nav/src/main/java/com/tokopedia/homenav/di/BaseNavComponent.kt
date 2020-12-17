package com.tokopedia.homenav.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@BaseNavScope
@Component(modules = [BaseNavModule::class], dependencies = [BaseAppComponent::class])
interface BaseNavComponent {
    @ApplicationContext
    fun getApplicationContext(): Context
    fun provideGraphqlRepository(): GraphqlRepository
    fun provideUserSession(): UserSessionInterface
    fun provideRemoteConfig(): RemoteConfig
    fun provideTrackingQueue(): TrackingQueue
    fun provideLocalCacheHandler(): LocalCacheHandler
}