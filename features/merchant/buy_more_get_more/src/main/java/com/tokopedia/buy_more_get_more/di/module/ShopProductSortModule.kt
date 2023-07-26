package com.tokopedia.buy_more_get_more.di.module

import com.tokopedia.buy_more_get_more.di.scope.ShopProductSortScope
import com.tokopedia.buy_more_get_more.presentation.sort.mapper.ShopProductSortMapper
import dagger.Module
import dagger.Provides

@Module(includes = [ShopProductSortViewModelModule::class])
class ShopProductSortModule {

    @ShopProductSortScope
    @Provides
    fun provideShopProductSortMapper(): ShopProductSortMapper {
        return ShopProductSortMapper()
    }

}
