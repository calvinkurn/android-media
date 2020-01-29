package com.tokopedia.brandlist.brandlist_search.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.common.GQLQueryConstant.QUERY_BRANDLIST_SEARCH_BRAND
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

}