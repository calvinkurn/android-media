package com.tokopedia.vouchergame.common.di

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.common.di.DigitalAddToCartQualifier
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchergame.common.VoucherGameAnalytics
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchergame.common.view.BaseVoucherGameActivity
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by resakemal on 26/08/19
 */

@VoucherGameScope
@Component(modules = [VoucherGameModule::class], dependencies = [CommonTopupBillsComponent::class])
interface VoucherGameComponent {

    fun userSessionInterface(): UserSessionInterface

    fun coroutineDispatcher(): CoroutineDispatcher

    fun coroutineDispatchersProvider(): CoroutineDispatchers

    fun graphqlRepository(): GraphqlRepository

    fun voucherGameAnalytics(): VoucherGameAnalytics

    fun rechargeAnalytics(): RechargeAnalytics

    fun commonTopupBillsAnalytics(): CommonTopupBillsAnalytics

    fun digitalCheckVoucherUseCase(): DigitalCheckVoucherUseCase

    fun inject(baseVoucherGameActivity: BaseVoucherGameActivity)

    @DigitalAddToCartQualifier
    fun restRepository(): RestRepository

    fun digitalInterceptor(): DigitalInterceptor

    fun networkRouter(): NetworkRouter

}