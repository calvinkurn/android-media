package com.tokopedia.product.manage.list.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.gm.common.data.interceptor.GMAuthInterceptor;
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl;
import com.tokopedia.gm.common.data.source.GMCommonDataSource;
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi;
import com.tokopedia.gm.common.domain.interactor.GetFeatureProductListUseCase;
import com.tokopedia.gm.common.domain.interactor.SetCashbackUseCase;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.product.manage.list.data.repository.ActionProductManageRepositoryImpl;
import com.tokopedia.product.manage.list.data.source.ActionProductManageDataSource;
import com.tokopedia.product.manage.list.data.source.ProductActionApi;
import com.tokopedia.product.manage.list.domain.ActionProductManageRepository;
import com.tokopedia.product.manage.list.domain.DeleteProductUseCase;
import com.tokopedia.product.manage.list.domain.EditPriceProductUseCase;
import com.tokopedia.product.manage.list.domain.MultipleDeleteProductUseCase;
import com.tokopedia.product.manage.list.view.mapper.GetProductListManageMapperView;
import com.tokopedia.product.manage.list.view.presenter.ProductManagePresenter;
import com.tokopedia.product.manage.list.view.presenter.ProductManagePresenterImpl;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.picker.data.api.GetProductListSellerApi;
import com.tokopedia.seller.product.picker.data.repository.GetProductListSellingRepositoryImpl;
import com.tokopedia.seller.product.picker.data.source.GetProductListSellingDataSource;
import com.tokopedia.seller.product.picker.domain.GetProductListSellingRepository;
import com.tokopedia.seller.product.picker.domain.interactor.GetProductListSellingUseCase;
import com.tokopedia.product.manage.item.common.data.source.cloud.TomeProductApi;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase;
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

@Module
@ProductManageScope
public class ProductManageModule {
    @Provides
    @ProductManageScope
    public ProductManagePresenter provideManageProductPresenter(GetShopInfoUseCase getShopInfoUseCase,
                                                                GetProductListSellingUseCase getProductListSellingUseCase,
                                                                EditPriceProductUseCase editPriceProductUseCase,
                                                                DeleteProductUseCase deleteProductUseCase,
                                                                GetProductListManageMapperView getProductListManageMapperView,
                                                                MultipleDeleteProductUseCase multipleDeleteProductUseCase,
                                                                UserSession userSession,
                                                                TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase,
                                                                TopAdsGetShopDepositGraphQLUseCase topAdsGetShopDepositGraphQLUseCase,
                                                                GetFeatureProductListUseCase getFeatureProductListUseCase,
                                                                SetCashbackUseCase setCashbackUseCase){
        return new ProductManagePresenterImpl(getShopInfoUseCase, getProductListSellingUseCase, editPriceProductUseCase,
                deleteProductUseCase, getProductListManageMapperView, multipleDeleteProductUseCase, userSession,
                topAdsAddSourceTaggingUseCase, topAdsGetShopDepositGraphQLUseCase, getFeatureProductListUseCase, setCashbackUseCase);
    }

    @Provides
    @ProductManageScope
    public GetFeatureProductListUseCase provideGetFeatureProductListUseCase(GMCommonRepository gmCommonRepository){
        return new GetFeatureProductListUseCase(gmCommonRepository);
    }

    @Provides
    @ProductManageScope
    public GMCommonRepository provideGmCommonRepository(GMCommonDataSource gmCommonDataSource){
        return new GMCommonRepositoryImpl(gmCommonDataSource);
    }

    @Provides
    @ProductManageScope
    public GMCommonApi provideGmCommonApi(@GMProductManageQualifier Retrofit retrofit){
        return retrofit.create(GMCommonApi.class);
    }

    @GMProductManageQualifier
    @ProductManageScope
    @Provides
    public Retrofit provideGMRetrofit(@GMProductManageQualifier OkHttpClient okHttpClient,
                                      Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @GMProductManageQualifier
    @Provides
    public OkHttpClient provideGMOkHttpClient(GMAuthInterceptor gmAuthInterceptor,
                                              HttpLoggingInterceptor httpLoggingInterceptor,
                                              HeaderErrorResponseInterceptor errorResponseInterceptor,
                                              CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(gmAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ProductManageScope
    @Provides
    HeaderErrorResponseInterceptor provideHeaderErrorResponseInterceptor(){
        return new HeaderErrorResponseInterceptor(HeaderErrorListResponse.class);
    }

    @ProductManageScope
    @Provides
    public CacheApiInterceptor provideApiCacheInterceptor() {
        return new CacheApiInterceptor();
    }

    @ProductManageScope
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    @Provides
    @ProductManageScope
    public SellerModuleRouter provideSellerModuleRouter(@ApplicationContext Context context){
        if(context instanceof SellerModuleRouter){
            return ((SellerModuleRouter)context);
        }else{
            return null;
        }
    }

    @ProductManageScope
    @Provides
    public GMAuthInterceptor provideGMAuthInterceptor(@ApplicationContext Context context,
                                                      AbstractionRouter abstractionRouter,
                                                      UserSession userSession) {
        return new GMAuthInterceptor(context, abstractionRouter, userSession);
    }

    @ProductManageScope
    @Provides
    UserSession provideUserSessionAbstraction(AbstractionRouter abstractionRouter){
        return abstractionRouter.getSession();
    }

    @Provides
    @ProductManageScope
    AbstractionRouter provideAbstractionRouter(@ApplicationContext Context context){
        if(context instanceof AbstractionRouter){
            return ((AbstractionRouter)context);
        }else{
            return null;
        }
    }

    @Provides
    @ProductManageScope
    public GetProductListSellingRepository provideGetProductListSellingRepository(GetProductListSellingDataSource getProductListSellingDataSource){
        return new GetProductListSellingRepositoryImpl(getProductListSellingDataSource);
    }

    @Provides
    @ProductManageScope
    public GetProductListSellerApi provideGetProductListSellerApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(GetProductListSellerApi.class);
    }


    @Provides
    @ProductManageScope
    public ActionProductManageRepository provideActionManageProductRepository(ActionProductManageDataSource actionProductManageDataSource){
        return new ActionProductManageRepositoryImpl(actionProductManageDataSource);
    }

    @Provides
    @ProductManageScope
    public ProductActionApi provideProductActionApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ProductActionApi.class);
    }

    @Provides
    @ProductManageScope
    public TomeProductApi provideTomeApi(@TomeQualifier Retrofit retrofit){
        return retrofit.create(TomeProductApi.class);
    }

    @Provides
    @ProductManageScope
    public TopAdsSourceTaggingLocal provideTopAdsSourceTracking(@ApplicationContext Context context){
        return new TopAdsSourceTaggingLocal(context);
    }

    @Provides
    @ProductManageScope
    public TopAdsSourceTaggingDataSource provideTopAdsSourceTaggingDataSource(TopAdsSourceTaggingLocal topAdsSourceTaggingLocal){
        return new TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal);
    }

    @Provides
    @ProductManageScope
    public TopAdsSourceTaggingRepository provideTopAdsSourceTaggingRepository(TopAdsSourceTaggingDataSource dataSource){
        return new TopAdsSourceTaggingRepositoryImpl(dataSource);
    }

    @Provides
    @ProductManageScope
    public GraphqlUseCase provideGraphqlUseCase(){
        return new GraphqlUseCase();
    }

    @ProductManageScope
    @Provides
    public UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new com.tokopedia.user.session.UserSession(context);
    }
}
