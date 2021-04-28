package com.tokopedia.power_merchant.subscribe.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gm.common.constant.GMParamConstant
import com.tokopedia.gm.common.di.GmCommonModule
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [GmCommonModule::class])
class PowerMerchantSubscribeModule {

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

    @Provides
    @Named(GMParamConstant.RAW_GM_QUESTIONNAIRE_QUESTION)
    fun provicePmStatusRaw(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gold_cancelation_questionnaire)
    }

    @PowerMerchantSubscribeScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @PowerMerchantSubscribeScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}