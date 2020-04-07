package com.tokopedia.brandlist.brandlist_search.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.common.GQLQueryConstant.QUERY_BRANDLIST_RECOMMENDATION
import com.tokopedia.brandlist.common.GQLQueryConstant.QUERY_BRANDLIST_SEARCH_BRAND
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [BrandlistSearchViewModelModule::class])
@BrandlistSearchScope
class BrandlistSearchModule {

    @BrandlistSearchScope
    @Provides
    @Named(QUERY_BRANDLIST_SEARCH_BRAND)
    fun provideQueryBrandlistSearch(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_brandlist_search_brand)
    }

    @BrandlistSearchScope
    @Provides
    @Named(QUERY_BRANDLIST_RECOMMENDATION)
    fun provideQueryBrandlistSearchRecommendation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_brandlist_recommendation)
    }

    @BrandlistSearchScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

}