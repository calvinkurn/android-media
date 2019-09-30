package com.tokopedia.officialstore.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.officialstore.common.di.OfficialStoreModule
import com.tokopedia.officialstore.common.di.OfficialStoreScope
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

@Component(modules = [OfficialStoreModule::class], dependencies = [BaseAppComponent::class])
@OfficialStoreScope
interface OfficialStoreComponent {
    @ApplicationContext
    fun getContext(): Context
    fun getMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase
    fun getCoroutineDispatcher(): CoroutineDispatcher
}
