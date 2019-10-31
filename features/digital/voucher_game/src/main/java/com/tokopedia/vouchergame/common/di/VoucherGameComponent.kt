package com.tokopedia.vouchergame.common.di

import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchergame.common.VoucherGameAnalytics
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

    fun graphqlRepository(): GraphqlRepository

    fun voucherGameAnalytics(): VoucherGameAnalytics

    fun rechargeAnalytics(): RechargeAnalytics

    fun inject(baseVoucherGameActivity: BaseVoucherGameActivity)

}