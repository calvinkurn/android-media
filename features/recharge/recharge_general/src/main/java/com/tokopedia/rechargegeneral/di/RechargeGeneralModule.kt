package com.tokopedia.rechargegeneral.di

import com.tokopedia.rechargegeneral.model.mapper.RechargeGeneralMapper
import com.tokopedia.rechargegeneral.util.RechargeGeneralAnalytics
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import dagger.Module
import dagger.Provides

/**
 * @author by resakemal on 07/01/20
 */
@Module
class RechargeGeneralModule {

    @RechargeGeneralScope
    @Provides
    fun provideRechargeGeneralAnalytics(): RechargeGeneralAnalytics = RechargeGeneralAnalytics()

    @RechargeGeneralScope
    @Provides
    fun provideDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider

    @RechargeGeneralScope
    @Provides
    fun provideRechargeGeneralMapper(): RechargeGeneralMapper = RechargeGeneralMapper()
}