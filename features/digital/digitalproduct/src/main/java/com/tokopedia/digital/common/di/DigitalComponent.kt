package com.tokopedia.digital.common.di


import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.storage.CacheManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase
import com.tokopedia.common_digital.common.DigitalRouter
import com.tokopedia.common_digital.common.data.api.DigitalRestApi
import com.tokopedia.common_digital.common.di.DigitalCommonComponent
import com.tokopedia.common_digital.common.di.DigitalRestApiRetrofit
import com.tokopedia.digital.common.analytic.DigitalAnalytics
import com.tokopedia.digital.common.router.DigitalModuleRouter
import com.tokopedia.user.session.UserSession

import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@DigitalScope
@Component(modules = arrayOf(DigitalModule::class), dependencies = arrayOf(DigitalCommonComponent::class))
interface DigitalComponent{
    fun digitalAnalytics() : DigitalAnalytics

    fun digitalRouter(): DigitalRouter

    fun globalCacheManager(): CacheManager

    fun userSession(): UserSession;

    @ApplicationContext
    fun context(): Context

    fun abstractionRouter(): AbstractionRouter

    fun digitalAddToCartUseCase(): DigitalAddToCartUseCase

    fun digitalInstantCheckoutUseCase(): DigitalInstantCheckoutUseCase

    fun httpLoggingInterceptor(): HttpLoggingInterceptor

    fun digitalModuleRouter() : DigitalModuleRouter

    @DigitalRestApiRetrofit
    fun digitalRestApiRetrofit(): Retrofit

    fun digitalApi(): DigitalRestApi

}
