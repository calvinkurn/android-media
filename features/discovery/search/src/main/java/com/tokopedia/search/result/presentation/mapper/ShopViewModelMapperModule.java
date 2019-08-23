package com.tokopedia.search.result.presentation.mapper;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class ShopViewModelMapperModule {

    @SearchScope
    @Provides
    ShopViewModelMapper provideShopViewModelMapper() {
        return new ShopViewModelMapper();
    }
}
