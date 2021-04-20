package com.tokopedia.search.result.presentation.view.fragment;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.recommendation_widget_common.di.RecommendationModule;
import com.tokopedia.search.di.module.GraphqlRepositoryModule;
import com.tokopedia.search.di.module.RemoteConfigModule;
import com.tokopedia.search.di.module.ResourcesModule;
import com.tokopedia.search.di.module.SearchContextModule;
import com.tokopedia.search.di.module.SearchOnBoardingLocalCacheModule;
import com.tokopedia.search.di.module.UserSessionModule;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterGqlUseCaseModule;
import com.tokopedia.search.result.domain.usecase.getinspirationcarouselchips.GetInspirationCarouselChipsProductUseCaseModule;
import com.tokopedia.search.result.domain.usecase.getlocalsearchrecommendation.GetLocalSearchRecommendationUseCaseModule;
import com.tokopedia.search.result.domain.usecase.getproductcount.GetProductCountUseCaseModule;
import com.tokopedia.search.result.domain.usecase.searchproduct.SearchProductUseCaseModule;
import com.tokopedia.search.result.presentation.presenter.product.ProductListPresenterModule;
import com.tokopedia.search.utils.ProductionSchedulersProviderModule;
import com.tokopedia.topads.sdk.di.TopAdsUrlHitterModule;

import dagger.Component;

@SearchScope
@Component(modules = {
        SearchContextModule.class,
        RemoteConfigModule.class,
        RecommendationModule.class,
        UserSessionModule.class,
        SearchProductUseCaseModule.class,
        GetProductCountUseCaseModule.class,
        GetDynamicFilterGqlUseCaseModule.class,
        GetLocalSearchRecommendationUseCaseModule.class,
        ResourcesModule.class,
        GraphqlRepositoryModule.class,
        SearchOnBoardingLocalCacheModule.class,
        TopAdsUrlHitterModule.class,
        ProductionSchedulersProviderModule.class,
        GetInspirationCarouselChipsProductUseCaseModule.class,
        ProductListPresenterModule.class
}, dependencies = BaseAppComponent.class)
public interface ProductListViewComponent {

    void inject(ProductListFragment view);
}
