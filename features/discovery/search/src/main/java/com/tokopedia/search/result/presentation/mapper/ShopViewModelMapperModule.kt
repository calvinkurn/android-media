package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchShopModelKt
import com.tokopedia.search.result.presentation.model.ShopViewModelKt

import dagger.Module
import dagger.Provides

@SearchScope
@Module
class ShopViewModelMapperModule {

    @SearchScope
    @Provides
    internal fun provideShopViewModelMapper(): Mapper<SearchShopModelKt, ShopViewModelKt> {
        return ShopViewModelMapperKt()
    }
}
