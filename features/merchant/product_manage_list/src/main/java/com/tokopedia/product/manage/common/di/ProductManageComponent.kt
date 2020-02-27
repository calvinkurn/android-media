package com.tokopedia.product.manage.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Component(modules = [ProductManageModule::class], dependencies = [BaseAppComponent::class])
@ProductManageScope
interface ProductManageComponent {
    @ApplicationContext
    fun getContext(): Context
    fun getMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase
    fun getCoroutineDispatcher(): CoroutineDispatcher
    fun httpLoggingInterceptor(): HttpLoggingInterceptor
    fun retrofitBuilder(): Retrofit.Builder
}