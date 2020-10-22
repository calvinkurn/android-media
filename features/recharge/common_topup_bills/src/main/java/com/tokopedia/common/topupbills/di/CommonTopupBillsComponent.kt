package com.tokopedia.common.topupbills.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.common.topupbills.utils.TopupBillsDispatchersProvider
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.di.DigitalCommonComponent
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by resakemal on 12/08/19.
 */
@CommonTopupBillsScope
@Component(modules = [CommonTopupBillsModule::class], dependencies = [DigitalCommonComponent::class])
interface CommonTopupBillsComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun coroutineDispatcher(): CoroutineDispatcher

    fun topupBillsDispatchersProvider(): TopupBillsDispatchersProvider

    fun graphqlRepository(): GraphqlRepository

    fun rechargeAnalytics(): RechargeAnalytics

    fun commonTopupBillsAnalytics(): CommonTopupBillsAnalytics

    fun digitalCheckVoucherUseCase(): DigitalCheckVoucherUseCase

}