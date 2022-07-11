package com.tokopedia.shopadmin.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component


@ShopAdminScope
@Component(modules = [ShopAdminModule::class, ShopAdminViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ShopAdminComponent {
    @ApplicationContext
    fun context(): Context

    fun coroutineDispatcher(): CoroutineDispatchers

    fun graphQlRepository(): GraphqlRepository

    fun userSessionInterface(): UserSessionInterface
}