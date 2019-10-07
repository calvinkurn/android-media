package com.tokopedia.search.result.presentation.mapper;

import com.tokopedia.search.di.scope.SearchScope;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class CatalogViewModelMapperModule {

    @SearchScope
    @Provides
    CatalogViewModelMapper provideCatalogViewModelMapper() {
        return new CatalogViewModelMapper();
    }
}
