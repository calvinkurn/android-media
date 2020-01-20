package com.tokopedia.search.result.presentation.presenter.product;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.search.di.module.AddWishListUseCaseModule;
import com.tokopedia.search.di.module.AdvertisingLocalCacheHandlerModule;
import com.tokopedia.search.di.module.GraphqlRepositoryModule;
import com.tokopedia.search.di.module.RecommendationModule;
import com.tokopedia.search.di.module.RemoteConfigModule;
import com.tokopedia.search.di.module.RemoveWishListUseCaseModule;
import com.tokopedia.search.di.module.ResourcesModule;
import com.tokopedia.search.di.module.UserSessionModule;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterGqlUseCaseModule;
import com.tokopedia.search.result.domain.usecase.productwishlisturl.ProductWishlistUrlUseCaseModule;
import com.tokopedia.search.result.domain.usecase.searchproduct.SearchProductUseCaseModule;
import com.tokopedia.search.result.network.service.TopAdsServiceModule;
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandlerModule;

import dagger.Component;

@SearchScope
@Component(modules = {
        RemoteConfigModule.class,
        AddWishListUseCaseModule.class,
        RemoveWishListUseCaseModule.class,
        RecommendationModule.class,
        UserSessionModule.class,
        TopAdsServiceModule.class,
        ProductWishlistUrlUseCaseModule.class,
        SearchProductUseCaseModule.class,
        GetDynamicFilterGqlUseCaseModule.class,
        SearchLocalCacheHandlerModule.class,
        RemoteConfigModule.class,
        ResourcesModule.class,
        GraphqlRepositoryModule.class,
        AdvertisingLocalCacheHandlerModule.class
}, dependencies = BaseAppComponent.class)
public interface ProductListPresenterComponent {

    void inject(ProductListPresenter presenter);
}