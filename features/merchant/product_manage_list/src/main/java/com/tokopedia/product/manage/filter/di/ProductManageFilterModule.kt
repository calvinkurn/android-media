package com.tokopedia.product.manage.filter.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.product.manage.common.GQLQueryConstant
import com.tokopedia.product.manage.oldlist.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ProductManageFilterViewModelModule::class])
@ProductManageFilterScope
class ProductManageFilterModule {

    @ProductManageFilterScope
    @Provides
    @Named(GQLQueryConstant.QUERY_PRODUCTLIST_META)
    fun provideQueryProductListMeta(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_productlist_meta)
    }
}