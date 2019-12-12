package com.tokopedia.rechargegeneral.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralFragment
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralPromoListFragment
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralRecentTransactionFragment
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

@RechargeGeneralScope
@Component(modules = [RechargeGeneralModule::class, RechargeGeneralViewModelModule::class], dependencies = [BaseAppComponent::class])
interface RechargeGeneralComponent {

    @ApplicationContext
    fun context(): Context

    fun userSession(): UserSessionInterface

    fun dispatcher(): CoroutineDispatcher

    fun graphQlRepository(): GraphqlRepository

    fun inject(rechargeGeneralFragment: RechargeGeneralFragment)

    fun inject(rechargeGeneralRecentTransactionFragment: RechargeGeneralRecentTransactionFragment)

    fun inject(rechargeGeneralPromoListFragment: RechargeGeneralPromoListFragment)
}