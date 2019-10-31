package com.tokopedia.power_merchant.subscribe.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gm.common.constant.GMParamConstant
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @PowerMerchantSubscribeScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @PowerMerchantSubscribeScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main



    @Provides
    @Named(GMParamConstant.RAW_GM_QUESTIONNAIRE_QUESTION)
    fun provicePmStatusRaw(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gold_cancelation_questionnaire)
    }
}