package com.tokopedia.loginregister.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.data.DynamicBannerConstant
import com.tokopedia.remoteconfig.GraphqlHelper
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@LoginRegisterScope
@Module
class GqlRawQueryModule {

    @LoginRegisterScope
    @Provides
    @IntoMap
    @StringKey(DynamicBannerConstant.Query.GET_AUTH_BANNER)
    fun provideRawDynamicBanner(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_dynamic_banner)
    }
}