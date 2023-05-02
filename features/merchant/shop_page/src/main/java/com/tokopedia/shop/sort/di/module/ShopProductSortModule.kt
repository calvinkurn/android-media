package com.tokopedia.shop.sort.di.module

import android.content.Context
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.sort.di.scope.ShopProductSortScope
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopProductSortViewModelModule::class])
class ShopProductSortModule {

    @ShopProductSortScope
    @Provides
    fun provideShopProductSortMapper(): ShopProductSortMapper {
        return ShopProductSortMapper()
    }

    @ShopProductSortScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

}
