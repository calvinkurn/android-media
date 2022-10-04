package com.tokopedia.product_bundle.common.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product_bundle.fragment.EntrypointFragment
import com.tokopedia.productbundlewidget.presentation.ProductBundleWidgetView
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@ProductBundleScope
@Component(modules = [ProductBundleModule::class, ProductBundleViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ProductBundleComponent {

    @ApplicationContext
    fun getContext(): Context

    fun getRetrofitBuilder(): Retrofit.Builder

    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor

    fun getCoroutineDispatcher(): CoroutineDispatcher

    fun gson(): Gson

    fun coroutineDispatchers(): CoroutineDispatchers

    @ApplicationContext
    fun graphqlRepository(): GraphqlRepository

    fun inject(entryPointFragment: EntrypointFragment)

    fun inject(customView: ProductBundleWidgetView)
}
