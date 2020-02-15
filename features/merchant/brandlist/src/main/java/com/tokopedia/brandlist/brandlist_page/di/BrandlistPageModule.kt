package com.tokopedia.brandlist.brandlist_page.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.common.GQLQueryConstant.QUERY_BRANDLIST_FEATURED_BRAND
import com.tokopedia.brandlist.common.GQLQueryConstant.QUERY_BRANDLIST_RECOMMENDATION
import com.tokopedia.brandlist.common.GQLQueryConstant.QUERY_BRANDLIST_SEARCH_BRAND
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [BrandlistPageViewModelModule::class])
@BrandlistPageScope
class BrandlistPageModule {

    @BrandlistPageScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @BrandlistPageScope
    @Provides
    @Named(QUERY_BRANDLIST_FEATURED_BRAND)
    fun provideQueryBrandlistCategories(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_brandlist_featured_brand)
    }

    @BrandlistPageScope
    @Provides
    @Named(QUERY_BRANDLIST_RECOMMENDATION)
    fun provideQueryBrandlistPopularBrand(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_brandlist_recommendation)
    }

    @BrandlistPageScope
    @Provides
    @Named(QUERY_BRANDLIST_SEARCH_BRAND)
    fun provideQueryBrandlistAllBrand(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_brandlist_search_brand)
    }
}