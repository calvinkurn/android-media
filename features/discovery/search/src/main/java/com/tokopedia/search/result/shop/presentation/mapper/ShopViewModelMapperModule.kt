package com.tokopedia.search.result.shop.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopCpmDataView
import com.tokopedia.search.result.shop.presentation.model.ShopDataView
import dagger.Module
import dagger.Provides

@Module
internal class ShopViewModelMapperModule {

    @SearchScope
    @Provides
    fun provideShopViewModelMapper(): Mapper<SearchShopModel, ShopDataView> {
        return ShopViewModelMapper()
    }

    @SearchScope
    @Provides
    fun provideShopCpmViewModelMapper(): Mapper<SearchShopModel, ShopCpmDataView> {
        return ShopCpmViewModelMapper()
    }
}
