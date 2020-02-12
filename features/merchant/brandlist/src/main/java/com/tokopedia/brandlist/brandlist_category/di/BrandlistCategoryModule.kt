package com.tokopedia.brandlist.brandlist_category.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.common.GQLQueryConstant.QUERY_BRANDLIST_CATEGORIES
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module(includes = [BrandlistCategoryViewModelModule::class])
@BrandlistCategoryScope
class BrandlistCategoryModule {

    @BrandlistCategoryScope
    @Provides
    @Named(QUERY_BRANDLIST_CATEGORIES)
    fun provideQueryBrandlistCategories(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_brandlist_get_categories)
    }

}