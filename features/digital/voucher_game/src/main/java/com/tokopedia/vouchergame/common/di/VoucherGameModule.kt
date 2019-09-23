package com.tokopedia.vouchergame.common.di

import com.tokopedia.vouchergame.common.VoucherGameAnalytics
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
}