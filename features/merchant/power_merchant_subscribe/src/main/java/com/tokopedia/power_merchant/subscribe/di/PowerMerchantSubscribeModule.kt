package com.tokopedia.power_merchant.subscribe.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gm.common.di.GmCommonModule
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import com.tokopedia.power_merchant.subscribe.view.contract.PmTermsContract
import com.tokopedia.power_merchant.subscribe.view.presenter.PmSubscribePresenter
import com.tokopedia.power_merchant.subscribe.view.presenter.PmTermsPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@PowerMerchantSubscribeScope
@Module(includes = [GmCommonModule::class])
class PowerMerchantSubscribeModule {

    @PowerMerchantSubscribeScope
    @Provides
    fun providePmSubscribePresenter(pmSubscribePresenter: PmSubscribePresenter):
            PmSubscribeContract.Presenter {
        return pmSubscribePresenter
    }

    @PowerMerchantSubscribeScope
    @Provides
    fun providePmTermsPresenter(pmTermsPresenter: PmTermsPresenter):
            PmTermsContract.Presenter {
        return pmTermsPresenter
    }

    @PowerMerchantSubscribeScope
    @Provides
    internal fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @PowerMerchantSubscribeScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

}