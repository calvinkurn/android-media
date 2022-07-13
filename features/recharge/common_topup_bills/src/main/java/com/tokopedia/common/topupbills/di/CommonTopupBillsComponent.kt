package com.tokopedia.common.topupbills.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.favoritepage.view.fragment.TopupBillsPersoContactListFragment
import com.tokopedia.common.topupbills.view.fragment.TopupBillsContactListFragment
import com.tokopedia.common.topupbills.view.fragment.TopupBillsFavoriteNumberFragment
import com.tokopedia.common.topupbills.favoritepage.view.fragment.TopupBillsPersoFavoriteNumberFragment
import com.tokopedia.common.topupbills.favoritepage.view.fragment.DualTabSavedNumberFragment
import com.tokopedia.common.topupbills.favoritepage.view.fragment.SingleTabSavedNumberFragment
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSavedNumberFragment
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.common.di.DigitalAddToCartQualifier
import com.tokopedia.common_digital.common.di.DigitalCommonComponent
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by resakemal on 12/08/19.
 */
@CommonTopupBillsScope
@Component(modules = [CommonTopupBillsModule::class, TopupBillsViewModelModule::class], dependencies = [DigitalCommonComponent::class])
interface CommonTopupBillsComponent {

    @ApplicationContext
    fun context(): Context

    fun userSession(): UserSessionInterface

    fun coroutineDispatcher(): CoroutineDispatcher

    fun topupBillsDispatchersProvider(): CoroutineDispatchers

    fun graphqlRepository(): GraphqlRepository

    fun rechargeAnalytics(): RechargeAnalytics

    fun commonTopupBillsAnalytics(): CommonTopupBillsAnalytics

    fun digitalCheckVoucherUseCase(): DigitalCheckVoucherUseCase

    @DigitalAddToCartQualifier
    fun restRepository(): RestRepository

    fun digitalInterceptor(): DigitalInterceptor

    fun networkRouter(): NetworkRouter

    fun inject(topupBillsFavoriteNumberFragment: TopupBillsFavoriteNumberFragment)

    fun inject(topupBillsContactListFragment: TopupBillsContactListFragment)

    fun inject(topupBillsSavedNumberFragment: TopupBillsSavedNumberFragment)

    fun inject(topupBillsPersoFavoriteNumberFragment: TopupBillsPersoFavoriteNumberFragment)

    fun inject(dualTabSavedNumberFragment: DualTabSavedNumberFragment)

    fun inject(singleTabSavedNumberFragment: SingleTabSavedNumberFragment)

    fun inject(topupBillsPersoContactListFragment: TopupBillsPersoContactListFragment)
}
