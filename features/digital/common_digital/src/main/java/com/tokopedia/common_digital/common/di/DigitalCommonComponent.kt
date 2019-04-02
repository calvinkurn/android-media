package com.tokopedia.common_digital.common.di

import android.content.Context

import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.storage.CacheManager
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase
import com.tokopedia.common_digital.cart.domain.usecase.DigitalInstantCheckoutUseCase
import com.tokopedia.common_digital.cart.view.activity.InstantCheckoutActivity
import com.tokopedia.common_digital.common.DigitalRouter
import com.tokopedia.common_digital.common.data.api.DigitalRestApi
import com.tokopedia.user.session.UserSession

import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Created by Rizky on 13/08/18.
 */
@DigitalCommonScope
@Component(modules = arrayOf(DigitalCommonModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface DigitalCommonComponent {

    @ApplicationContext
    fun context(): Context

    @DigitalRestApiRetrofit
    fun digitalRestApiRetrofit(): Retrofit

    fun digitalApi(): DigitalRestApi

    fun abstractionRouter(): AbstractionRouter

    fun digitalAddToCartUseCase(): DigitalAddToCartUseCase

    fun digitalInstantCheckoutUseCase(): DigitalInstantCheckoutUseCase

    fun digitalRouter(): DigitalRouter

    fun globalCacheManager(): CacheManager

    fun inject(instantCheckoutActivity: InstantCheckoutActivity)

    fun httpLoggingInterceptor(): HttpLoggingInterceptor

    fun userSession(): UserSession;

}

