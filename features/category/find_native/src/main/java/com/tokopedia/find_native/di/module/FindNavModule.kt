package com.tokopedia.find_native.di.module

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.find_native.R
import com.tokopedia.find_native.di.scope.FindNavScope
import com.tokopedia.find_native.util.FindNavConstants
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@FindNavScope
@Module
class FindNavModule {

    @FindNavScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }

    @FindNavScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @FindNavScope
    @Provides
    fun provideGcmHandler(@ApplicationContext context: Context): GCMHandler {
        return GCMHandler(context)
    }

    @FindNavScope
    @Provides
    fun provideResources(context: Context): Resources {
        return context.resources
    }

    @FindNavScope
    @Provides
    fun getAddWishListUseCase(context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @FindNavScope
    @Provides
    fun getRemoveWishListUseCase(context: Context): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @FindNavScope
    @Named(FindNavConstants.GQL_NAV_SEARCH_PRODUCT)
    @Provides
    fun provideProductListQuery(resources: Resources): String {
        return GraphqlHelper.loadRawString(resources, R.raw.gql_nav_search_product)
    }

    @FindNavScope
    @Named(FindNavConstants.GQL_NAV_QUICK_FILTER)
    @Provides
    fun provideQuickFilterListQuery(resources: Resources): String {
        return GraphqlHelper.loadRawString(resources, R.raw.gql_nav_quick_filter)
    }

    @FindNavScope
    @Named(FindNavConstants.GQL_NAV_DYNAMIC_FILTER)
    @Provides
    fun provideDynamicFilterListQuery(resources: Resources): String {
        return GraphqlHelper.loadRawString(resources, R.raw.gql_nav_dynamic_attribute)
    }

    @FindNavScope
    @Named(FindNavConstants.GQL_NAV_RELATED_LINK)
    @Provides
    fun provideRelatedLinkListQuery(resources: Resources): String {
        return GraphqlHelper.loadRawString(resources, R.raw.gql_find_related_link)
    }

}