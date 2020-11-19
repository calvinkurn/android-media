package com.tokopedia.shop.open.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.open.presentation.view.fragment.ShopOpenRevampInputShopFragment
import com.tokopedia.shop.open.presentation.view.fragment.ShopOpenRevampQuisionerFragment
import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@ShopOpenRevampScope
@Component(modules = [ShopOpenRevampModule::class], dependencies = [BaseAppComponent::class])
interface ShopOpenRevampComponent {

    @ApplicationContext
    fun getContext(): Context
    fun getMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase
    fun getDispatcherProvider(): CoroutineDispatchers
    fun httpLoggingInterceptor(): HttpLoggingInterceptor
    fun retrofitBuilder(): Retrofit.Builder
    fun graphQlRepository(): GraphqlRepository

    fun inject(view: ShopOpenRevampQuisionerFragment)
    fun inject(view: ShopOpenRevampInputShopFragment)

}