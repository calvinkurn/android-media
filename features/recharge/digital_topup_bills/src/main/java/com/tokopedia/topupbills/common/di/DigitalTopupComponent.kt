package com.tokopedia.topupbills.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.common.di.DigitalAddToCartQualifier
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@DigitalTopupScope
@Component(modules = [DigitalTopupModule::class], dependencies = [CommonTopupBillsComponent::class])
interface DigitalTopupComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun coroutineDispatcher(): CoroutineDispatcher

    fun graphqlRepository(): GraphqlRepository

    fun rechargeAnalytics(): RechargeAnalytics

    fun topupBillsDispatchersProvider(): CoroutineDispatchers

    fun commonTopupBillsAnalytics(): CommonTopupBillsAnalytics

    fun digitalCheckVoucherUseCase(): DigitalCheckVoucherUseCase

    @DigitalAddToCartQualifier
    fun restRepository(): RestRepository

    fun digitalInterceptor(): DigitalInterceptor

    fun networkRouter(): NetworkRouter

}