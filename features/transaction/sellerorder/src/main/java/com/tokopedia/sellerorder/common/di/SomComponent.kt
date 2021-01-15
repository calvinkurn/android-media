package com.tokopedia.sellerorder.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by fwidjaja on 2019-08-28.
 */

@SomScope
@Component(modules = [SomModule::class, SomViewModelModule::class], dependencies = [BaseAppComponent::class])
interface SomComponent {
    @ApplicationContext
    fun context(): Context

    fun dispatcher(): CoroutineDispatcher

    fun graphQlRepository(): GraphqlRepository

    fun userSessionInterface(): UserSessionInterface

    fun dispatcherProvider(): SomDispatcherProvider
}