package com.tokopedia.officialstore.official.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.officialstore.GQLQueryConstant.QUERY_OFFICIAL_STORE_BANNERS
import com.tokopedia.officialstore.GQLQueryConstant.QUERY_OFFICIAL_STORE_DYNAMIC_CHANNEL
import com.tokopedia.officialstore.GQLQueryConstant.QUERY_OFFICIAL_STORE_FEATURED_SHOPS
import com.tokopedia.officialstore.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [OfficialStoreHomeViewModelModule::class])
@OfficialStoreHomeScope
class OfficialStoreHomeModule {

    @OfficialStoreHomeScope
    @Provides
    @Named(QUERY_OFFICIAL_STORE_BANNERS)
    fun provideQueryofficialStoreBanners(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_official_store_banner)
    }

    @OfficialStoreHomeScope
    @Provides
    @Named(QUERY_OFFICIAL_STORE_FEATURED_SHOPS)
    fun provideQueryofficialStoreFeaturedShop(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_official_store_featured)
    }

    @OfficialStoreHomeScope
    @Provides
    @Named(QUERY_OFFICIAL_STORE_DYNAMIC_CHANNEL)
    fun provideQueryOfficialStoreDynamicChannel(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_official_store_dynamic_channel)
    }
}