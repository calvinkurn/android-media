package com.tokopedia.topupbills.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.utils.TopupBillsDispatchersProvider
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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

    fun topupBillsDispatchersProvider(): TopupBillsDispatchersProvider

    fun commonTopupBillsAnalytics(): CommonTopupBillsAnalytics

    fun digitalCheckVoucherUseCase(): DigitalCheckVoucherUseCase

}