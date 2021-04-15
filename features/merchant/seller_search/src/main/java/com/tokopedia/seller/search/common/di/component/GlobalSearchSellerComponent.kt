package com.tokopedia.seller.search.common.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.seller.search.common.di.module.GlobalSearchSellerModule
import com.tokopedia.seller.search.common.di.scope.GlobalSearchSellerScope
import dagger.Component

@GlobalSearchSellerScope
@Component(modules = [GlobalSearchSellerModule::class], dependencies = [BaseAppComponent::class])
interface GlobalSearchSellerComponent {
    @ApplicationContext
    fun getContext(): Context

    fun getGraphqlRepository(): GraphqlRepository

    fun getMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase

    fun getCoroutineDispatchers(): CoroutineDispatchers
}