package com.tokopedia.search.result.shop.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopCpmViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopTotalCountViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import dagger.Module
import dagger.Provides

@SearchScope
@Module
internal class ShopViewModelMapperModule {

    @SearchScope
    @Provides
    fun provideShopViewModelMapper(): Mapper<SearchShopModel, ShopViewModel> {
        return ShopViewModelMapper()
    }

    @SearchScope
    @Provides
    fun provideShopCpmViewModelMapper(): Mapper<SearchShopModel, ShopCpmViewModel> {
        return ShopCpmViewModelMapper()
    }

    @SearchScope
    @Provides
    fun provideShopTotalCountViewModelMapper(): Mapper<SearchShopModel, ShopTotalCountViewModel> {
        return ShopTotalCountViewModelMapper()
    }
}
