package com.tokopedia.common_digital.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by Rizky on 13/08/18.
 */
@DigitalCommonScope
@Component(modules = [DigitalCommonModule::class],
        dependencies = [BaseAppComponent::class])
interface DigitalCommonComponent {

    @ApplicationContext
    fun context(): Context

    fun httpLoggingInterceptor(): HttpLoggingInterceptor

    fun userSession(): UserSessionInterface

    fun rechargeAnalytics(): RechargeAnalytics

    @DigitalAddToCartQualifier
    fun restRepository(): RestRepository

    fun digitalInterceptor(): DigitalInterceptor

    fun networkRouter(): NetworkRouter

    fun coroutineDispatchers(): CoroutineDispatchers
}

