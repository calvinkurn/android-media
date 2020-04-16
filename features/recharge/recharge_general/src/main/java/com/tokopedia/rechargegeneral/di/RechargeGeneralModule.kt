package com.tokopedia.rechargegeneral.di

import com.tokopedia.rechargegeneral.model.mapper.RechargeGeneralDynamicInputMapper
import com.tokopedia.rechargegeneral.util.RechargeGeneralAnalytics
import com.tokopedia.rechargegeneral.util.RechargeGeneralDispatchersProvider
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
    fun provideDispatcher(): RechargeGeneralDispatchersProvider = RechargeGeneralDispatchersProvider()

    @RechargeGeneralScope
    @Provides
    fun provideRechargeGeneralMapper(): RechargeGeneralDynamicInputMapper = RechargeGeneralDynamicInputMapper()
}