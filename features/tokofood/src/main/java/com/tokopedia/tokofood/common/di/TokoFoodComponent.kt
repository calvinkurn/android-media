package com.tokopedia.tokofood.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component

@TokoFoodScope
@Component(modules = [TokoFoodModule::class], dependencies = [BaseAppComponent::class])
interface TokoFoodComponent {

    @ApplicationContext
    fun getContext(): Context

    @ApplicationContext
    fun getGraphqlRepository(): GraphqlRepository

    fun getCoroutineDispatchers(): CoroutineDispatchers

}