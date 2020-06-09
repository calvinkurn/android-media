package com.tokopedia.shop_showcase.shop_showcase_management.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.GQLQueryConstant
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ShopShowcaseManagementViewModelModule::class])
@ShopShowcaseManagementScope
class ShopShowcaseManagementModule(val context: Context) {

    @Provides
    fun provideShopShowcaseManagementContext() = context

    @ShopShowcaseManagementScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShopShowcaseManagementScope
    @Provides
    @Named(GQLQueryConstant.QUERY_SHOP_SHOWCASE_LIST)
    fun provideQueryShopShowcaseListData(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.get_shop_showcase_list)
    }

    @ShopShowcaseManagementScope
    @Provides
    @Named(GQLQueryConstant.QUERY_DELETE_SINGLE_SHOP_SHOWCASE)
    fun provideQueryDeleteShopShowcaseData(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.delete_shop_showcase)
    }

    @ShopShowcaseManagementScope
    @Provides
    @Named(GQLQueryConstant.QUERY_REORDER_SHOP_SHOWCASE)
    fun provideQueryReorderShopShowcase(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.reorder_shop_showcase)
    }

    @ShopShowcaseManagementScope
    @Provides
    @Named(GQLQueryConstant.QUERY_SHOP_SHOWCASE_LIST_AS_BUYER)
    fun provideQueryShopShowcaseListAsBuyer(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.get_shop_showcase_list_as_buyer)
    }

    @ShopShowcaseManagementScope
    @Provides
    @Named(GQLQueryConstant.QUERY_SHOP_SHOWCASE_LIST_AS_SELLER)
    fun provideQueryShopShowcaseListAsSeller(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.get_shop_showcase_list_as_seller)
    }

    @ShopShowcaseManagementScope
    @Provides
    @Named(GQLQueryConstant.QUERY_SHOP_SHOWCASE_GET_TOTAL_PRODUCTS)
    fun provideQueryShopShowcaseGetTotalProduct(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.get_shop_showcase_product)
    }

}