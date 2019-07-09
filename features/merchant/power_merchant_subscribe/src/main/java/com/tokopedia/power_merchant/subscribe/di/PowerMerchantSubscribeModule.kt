package com.tokopedia.power_merchant.subscribe.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gm.common.constant.GMParamConstant.*
import com.tokopedia.gm.common.di.GmCommonModule
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import com.tokopedia.power_merchant.subscribe.view.contract.PmTermsContract
import com.tokopedia.power_merchant.subscribe.view.presenter.PmSubscribePresenter
import com.tokopedia.power_merchant.subscribe.view.presenter.PmTermsPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

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

    @PowerMerchantSubscribeScope
    @Provides
    @Named(RAW_DEACTIVATION)
    fun providePmOffRaw(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gold_merchant_turn_off)
    }

    @PowerMerchantSubscribeScope
    @Provides
    @Named(RAW_ACTIVATION)
    fun providePmOnRaw(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gold_merchant_activation)

    }

    @PowerMerchantSubscribeScope
    @Provides
    @Named(RAW_GM_STATUS)
    fun provicePmStatusRaw(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gold_merchant_status)

    }

}