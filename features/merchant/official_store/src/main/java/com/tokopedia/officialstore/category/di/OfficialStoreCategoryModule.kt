package com.tokopedia.officialstore.category.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.officialstore.GQLQueryConstant.QUERY_OFFICIAL_STORE_CATEGORIES
import com.tokopedia.officialstore.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [OfficialStoreCategoryViewModelModule::class])
class OfficialStoreCategoryModule {

    @OfficialStoreCategoryScope
    @Provides
    @Named(QUERY_OFFICIAL_STORE_CATEGORIES)
    fun provideQueryofficialStoreCategories(@ApplicationContext context: Context): String{
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_official_store_categories)
    }

}