package com.tokopedia.digital.common.di


import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.di.DigitalAddToCartQualifier
import com.tokopedia.common_digital.common.di.DigitalCommonComponent
import com.tokopedia.digital.common.analytic.DigitalAnalytics
import com.tokopedia.digital.newcart.domain.mapper.ICartMapperData
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@DigitalScope
@Component(modules = arrayOf(DigitalModule::class), dependencies = arrayOf(DigitalCommonComponent::class))
interface DigitalComponent {
    fun digitalAnalytics(): DigitalAnalytics

    fun rechargeAnalytics(): RechargeAnalytics

    fun userSession(): UserSessionInterface

    @ApplicationContext
    fun context(): Context

    fun httpLoggingInterceptor(): HttpLoggingInterceptor

    @DigitalRestApiRetrofit
    fun digitalRestApiRetrofit(): Retrofit

    fun cartMapperData(): ICartMapperData

    @DigitalAddToCartQualifier
    fun restRepository(): RestRepository

}
