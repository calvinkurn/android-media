package com.tokopedia.homenav.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@BaseNavScope
@Component(modules = [BaseNavModule::class], dependencies = [BaseAppComponent::class])
interface BaseNavComponent {

    fun provideGraphqlRepository(): GraphqlRepository
    fun provideUserSession(): UserSessionInterface
    fun provideRemoteConfig(): RemoteConfig
    fun provideLocalCacheHandler(): LocalCacheHandler

}