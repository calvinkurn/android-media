package com.tokopedia.vouchergame.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
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