package com.tokopedia.reviewseller.common.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.reviewseller.common.di.module.ReviewSellerModule
import com.tokopedia.reviewseller.common.di.scope.ReviewSellerScope
import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * @author by Rafli Syam on 02-27-2020
 */
@ReviewSellerScope
@Component(modules = [ReviewSellerModule::class], dependencies = [BaseAppComponent::class])
interface ReviewSellerComponent {
    @ApplicationContext
    fun getContext(): Context

    fun getRetrofitBuilder(): Retrofit.Builder

    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor

    fun getGraphqlRepository(): GraphqlRepository

    fun getMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase
}