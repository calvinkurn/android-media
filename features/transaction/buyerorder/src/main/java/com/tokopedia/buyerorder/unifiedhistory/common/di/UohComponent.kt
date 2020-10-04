package com.tokopedia.buyerorder.unifiedhistory.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by fwidjaja on 04/07/20.
 */

@UohScope
@Component(modules = [UohModule::class, UohViewModelModule::class], dependencies = [BaseAppComponent::class])
interface UohComponent {
    @ApplicationContext
    fun context(): Context

    fun dispatcher(): CoroutineDispatcher

    fun graphQlRepository(): GraphqlRepository

    fun userSessionInterface(): UserSessionInterface

    fun dispatcherProvider(): BuyerDispatcherProvider
}