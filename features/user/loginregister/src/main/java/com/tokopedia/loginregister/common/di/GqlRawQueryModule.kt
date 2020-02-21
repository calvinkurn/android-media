package com.tokopedia.loginregister.common.di

import com.tokopedia.loginregister.common.data.DynamicBannerConstant
import com.tokopedia.loginregister.common.domain.query.QueryDynamicBanner
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
    fun provideRawDynamicBanner(): String {
        return QueryDynamicBanner.getQuery()
    }
}