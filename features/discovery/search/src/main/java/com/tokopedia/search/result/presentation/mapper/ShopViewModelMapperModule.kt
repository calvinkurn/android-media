package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.presentation.model.ShopViewModel

import dagger.Module
import dagger.Provides

@SearchScope
@Module
class ShopViewModelMapperModule {

    @SearchScope
    @Provides
    fun provideShopViewModelMapper(): Mapper<SearchShopModel, ShopViewModel> {
        return ShopViewModelMapper()
    }


    @SearchScope
    @Provides
    fun provideShopHeaderViewModelMapper(): Mapper<SearchShopModel, ShopHeaderViewModel> {
        return ShopHeaderViewModelMapper()
    }
}
