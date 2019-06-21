package com.tokopedia.search.result.domain.usecase.productwishlisturl;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.qualifier.TopAdsQualifier;
import com.tokopedia.search.result.network.service.TopAdsService;
import com.tokopedia.usecase.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class ProductWishlistUrlUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.Wishlist.PRODUCT_WISHLIST_URL_USE_CASE)
    UseCase<Boolean> provideProductWishlistUrlUseCase(@TopAdsQualifier TopAdsService topAdsService) {
        return new ProductWishlistUrlUseCase(topAdsService);
    }
}
