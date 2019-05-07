package com.tokopedia.search.result.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.module.RemoteConfigModule;
import com.tokopedia.search.result.data.gql.searchproduct.GqlSearchProductSpecModule;
import com.tokopedia.search.result.di.module.AddWishListUseCaseModule;
import com.tokopedia.search.result.di.module.RemoveWishListUseCaseModule;
import com.tokopedia.search.result.domain.usecase.ProductWishlistUrlUseCaseModule;
import com.tokopedia.search.result.domain.usecase.SearchProductUseCaseModule;
import com.tokopedia.search.result.presentation.ProductListSectionContract;

import dagger.Component;

@SearchScope
@Component(modules = {
        RemoteConfigModule.class,
        AddWishListUseCaseModule.class,
        RemoveWishListUseCaseModule.class,
        ProductWishlistUrlUseCaseModule.class,
        GqlSearchProductSpecModule.class,
        SearchProductUseCaseModule.class
}, dependencies = BaseAppComponent.class)
public interface ProductListPresenterComponent {

    void inject(ProductListSectionContract.Presenter presenter);
}
