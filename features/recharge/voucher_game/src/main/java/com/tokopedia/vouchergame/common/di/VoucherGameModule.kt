package com.tokopedia.vouchergame.common.di

import com.tokopedia.vouchergame.common.VoucherGameAnalytics
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import dagger.Module
import dagger.Provides

/**
 * @author by resakemal on 26/08/19
 */
@Module
class VoucherGameModule {

    @VoucherGameScope
    @Provides
    fun provideVoucherGameAnalytics(): VoucherGameAnalytics = VoucherGameAnalytics()

    @VoucherGameScope
    @Provides
    fun provideDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider
}