package com.tokopedia.topads.dashboard.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.common.data.api.TopAdsManagementApi;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsOldManagementApi;
import com.tokopedia.topads.dashboard.di.qualifier.TopAdsManagementQualifier;
import com.tokopedia.topads.dashboard.di.scope.TopAdsManagementScope;
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.topads.dashboard.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.topads.dashboard.data.factory.TopAdsProductAdFactory;
import com.tokopedia.topads.dashboard.data.factory.TopAdsShopAdFactory;
import com.tokopedia.topads.common.data.repository.TopAdsCheckProductPromoRepositoryImpl;
import com.tokopedia.topads.dashboard.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.repository.TopAdsProductAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.repository.TopAdsSearchProductRepositoryImpl;
import com.tokopedia.topads.dashboard.data.repository.TopAdsShopAdsRepositoryImpl;
import com.tokopedia.topads.common.data.source.TopAdsCheckProductPromoDataSource;
import com.tokopedia.topads.dashboard.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.topads.common.data.source.cloud.TopAdsCheckProductPromoDataSourceCloud;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.topads.common.domain.repository.TopAdsCheckProductPromoRepository;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.topads.dashboard.domain.TopAdsSearchProductRepository;
import com.tokopedia.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateDetailShopUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateNewGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditShopPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditShopPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewShopPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewShopPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsGetProductDetailPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsGetProductDetailPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 8/13/17.
 */

@TopAdsManagementScope
@Module
public class TopAdsCreatePromoModule {
    @TopAdsManagementScope
    @Provides
    TopAdsDetailNewGroupPresenter providePresenterNewGroup(TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase,
                                                           TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                                           TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase,
                                                           TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase,
                                                           TopAdsProductListUseCase topAdsProductListUseCase,
                                                           TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase,
                                                           TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase) {
        return new TopAdsDetailNewGroupPresenterImpl(topAdsCreateNewGroupUseCase, topAdsGetDetailGroupUseCase,
                topAdsSaveDetailGroupUseCase, topAdsCreateDetailProductListUseCase, topAdsProductListUseCase,
                topAdsGetSuggestionUseCase, topAdsGetSourceTaggingUseCase);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsGetProductDetailPresenter provideTopAdsGetProductDetailPresenter(TopAdsProductListUseCase topAdsProductListUseCase){
        return new TopAdsGetProductDetailPresenterImpl(topAdsProductListUseCase);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsDetailNewProductPresenter provideTopAdsDetailProductPresenter(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase,
                                                                        TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase,
                                                                        TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase,
                                                                        TopAdsProductListUseCase topAdsProductListUseCase,
                                                                        TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase,
                                                                        TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase) {
        return new TopAdsDetailNewProductPresenterImpl(topAdsGetDetailProductUseCase, topAdsSaveDetailProductUseCase,
                topAdsCreateDetailProductListUseCase, topAdsProductListUseCase, topAdsGetSuggestionUseCase,
                topAdsGetSourceTaggingUseCase);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsDetailNewShopPresenter provideTopAdsDetailShopPresenter(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase,
                                                                     TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase,
                                                                     TopAdsCreateDetailShopUseCase topAdsCreateDetailShopUseCase,
                                                                     TopAdsProductListUseCase topAdsProductListUseCase,
                                                                  TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase) {
        return new TopAdsDetailNewShopPresenterImpl(topAdsGetDetailShopUseCase,
                topAdsSaveDetailShopUseCase, topAdsCreateDetailShopUseCase,
                topAdsProductListUseCase, topAdsGetSourceTaggingUseCase);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsDetailEditProductPresenter provideTopadsDetailEditProductPresenter(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase,
                                                                             TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase,
                                                                             TopAdsProductListUseCase topAdsProductListUseCase,
                                                                             TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase,
                                                                             TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase){
        return new TopAdsDetailEditProductPresenterImpl(topAdsGetDetailProductUseCase,
                topAdsSaveDetailProductUseCase, topAdsProductListUseCase, topAdsGetSuggestionUseCase, topAdsGetSourceTaggingUseCase);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsDetailEditGroupPresenter provideTopadsDetailEditGroupPresenter(TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                                                           TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase,
                                                                           TopAdsProductListUseCase topAdsProductListUseCase,
                                                                         TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase,
                                                                         TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase){
        return new TopAdsDetailEditGroupPresenterImpl(topAdsGetDetailGroupUseCase, topAdsSaveDetailGroupUseCase,
                topAdsProductListUseCase, topAdsGetSuggestionUseCase, topAdsGetSourceTaggingUseCase);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsDetailEditShopPresenter provideTopAdsDetailEditShopPresenter(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase,
                                                                          TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase,
                                                                          TopAdsProductListUseCase topAdsProductListUseCase,
                                                                       TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase){
        return new TopAdsDetailEditShopPresenterImpl(topAdsGetDetailShopUseCase, topAdsSaveDetailShopUseCase,
                topAdsProductListUseCase, topAdsGetSourceTaggingUseCase);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsShopAdsRepository provideTopAdsShopRepository(TopAdsShopAdFactory topAdsShopAdFactory) {
        return new TopAdsShopAdsRepositoryImpl(topAdsShopAdFactory);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsGroupAdsRepository provideTopAdsGroupRepository(TopAdsGroupAdFactory topAdsGroupAdFactory) {
        return new TopAdsGroupAdsRepositoryImpl(topAdsGroupAdFactory);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsOldManagementApi provideTopAdsManagementApi(@TopAdsManagementQualifier Retrofit retrofit) {
        return retrofit.create(TopAdsOldManagementApi.class);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsProductAdsRepository provideTopAdsProductRepository(TopAdsProductAdFactory topAdsProductAdFactory) {
        return new TopAdsProductAdsRepositoryImpl(topAdsProductAdFactory);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsSearchProductRepository provideTopAdsSearchProductRepository(@ApplicationContext Context context,
                                                                       CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource) {
        return new TopAdsSearchProductRepositoryImpl(context, cloudTopAdsSearchProductDataSource);
    }

    @TopAdsManagementScope
    @Provides
    TopAdsManagementService provideTopAdsManagementService(@ApplicationContext Context context) {
        return new TopAdsManagementService(new SessionHandler(context));
    }

    @TopAdsManagementScope
    @Provides
    public TopAdsCheckProductPromoDataSourceCloud provideTopAdsCheckProductPromoDataSourceCloud(TopAdsManagementApi topAdsManagementApi){
        return new TopAdsCheckProductPromoDataSourceCloud(topAdsManagementApi);
    }

    @TopAdsManagementScope
    @Provides
    public TopAdsCheckProductPromoDataSource provideTopAdsCheckProductPromoDataSource(TopAdsCheckProductPromoDataSourceCloud dataSourceCloud){
        return new TopAdsCheckProductPromoDataSource(dataSourceCloud);
    }

    @TopAdsManagementScope
    @Provides
    public TopAdsCheckProductPromoRepository provideGetProductSellingPromoTopAdsRepository(TopAdsCheckProductPromoDataSource dataSource){
        return new TopAdsCheckProductPromoRepositoryImpl(dataSource);
    }

    @TopAdsManagementScope
    @Provides
    public TopAdsSourceTaggingLocal provideTopAdsSourceTracking(@ApplicationContext Context context){
        return new TopAdsSourceTaggingLocal(context);
    }

    @TopAdsManagementScope
    @Provides
    public TopAdsSourceTaggingDataSource provideTopAdsSourceTaggingDataSource(TopAdsSourceTaggingLocal topAdsSourceTaggingLocal){
        return new TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal);
    }

    @TopAdsManagementScope
    @Provides
    public TopAdsSourceTaggingRepository provideTopAdsSourceTaggingRepository(TopAdsSourceTaggingDataSource dataSource){
        return new TopAdsSourceTaggingRepositoryImpl(dataSource);
    }

}
