package com.tokopedia.shop.product.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.gm.common.data.interceptor.GMAuthInterceptor;
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl;
import com.tokopedia.gm.common.data.source.GMCommonDataSource;
import com.tokopedia.gm.common.data.source.cloud.GMCommonCloudDataSource;
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.shop.R;
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.ClaimBenefitMembershipUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetMembershipUseCase;
import com.tokopedia.shop.product.data.GQLQueryConstant;
import com.tokopedia.shop.product.data.repository.ShopProductRepositoryImpl;
import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource;
import com.tokopedia.shop.product.data.source.cloud.api.ShopOfficialStoreApi;
import com.tokopedia.shop.product.data.source.cloud.interceptor.ShopOfficialStoreAuthInterceptor;
import com.tokopedia.shop.product.di.ShopProductGMFeaturedQualifier;
import com.tokopedia.shop.product.di.ShopProductQualifier;
import com.tokopedia.shop.product.di.ShopProductWishListFeaturedQualifier;
import com.tokopedia.shop.product.di.scope.ShopProductScope;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductAceUseCase;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductTomeUseCase;
import com.tokopedia.shop.product.domain.interactor.GetProductCampaignsUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase;
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.constant.WishListCommonUrl;
import com.tokopedia.wishlist.common.data.interceptor.WishListAuthInterceptor;
import com.tokopedia.wishlist.common.data.repository.WishListCommonRepositoryImpl;
import com.tokopedia.wishlist.common.data.source.WishListCommonDataSource;
import com.tokopedia.wishlist.common.data.source.cloud.WishListCommonCloudDataSource;
import com.tokopedia.wishlist.common.data.source.cloud.api.WishListCommonApi;
import com.tokopedia.wishlist.common.data.source.cloud.mapper.WishListProductListMapper;
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase;
import com.tokopedia.wishlist.common.domain.repository.WishListCommonRepository;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import static com.tokopedia.shop.common.constant.ShopCommonParamApiConstant.QUERY_CLAIM_MEMBERSHIP;
import static com.tokopedia.shop.common.constant.ShopCommonParamApiConstant.QUERY_STAMP_PROGRESS;

@ShopProductScope
@Module(includes = ShopProductViewModelModule.class)
public class ShopProductModule {

    @ShopProductScope
    @Provides
    public NetworkRouter getNetworkRouter(@ApplicationContext Context context){
        return (NetworkRouter)context;
    }

    @ShopProductScope
    @Provides
    @Named(GQLQueryConstant.SHOP_FEATURED_PRODUCT)
    public String getShopFeaturedProductQuery(@ApplicationContext Context context){
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_get_shop_featured_product);
    }

    @ShopProductScope
    @Provides
    @Named(GQLQueryConstant.SHOP_PRODUCT)
    public String getShopProductQuery(@ApplicationContext Context context){
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_get_shop_product);
    }

    @Provides
    @Named(ShopCommonParamApiConstant.QUERY_STAMP_PROGRESS)
    public String provideQueryStampProgress(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), com.tokopedia.shop.common.R.raw.gql_get_stamp_progress);
    }

    @Provides
    @Named(ShopCommonParamApiConstant.QUERY_CLAIM_MEMBERSHIP)
    public String provideQueryClaimBenefit (@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), com.tokopedia.shop.common.R.raw.gql_mutation_membership_claim);
    }

    @ShopProductScope
    @Provides
    public GetMembershipUseCase provideGetMembershipUseCase(@Named(QUERY_STAMP_PROGRESS)
                                                                    String gqlQuery,
                                                            MultiRequestGraphqlUseCase gqlUseCase) {
        return new GetMembershipUseCase(gqlQuery, gqlUseCase);
    }

    @ShopProductScope
    @Provides
    public ClaimBenefitMembershipUseCase provideClaimBenefitMembershipUseCase (@Named(QUERY_CLAIM_MEMBERSHIP)
                                                                    String gqlQuery,
                                                                     MultiRequestGraphqlUseCase gqlUseCase) {
        return new ClaimBenefitMembershipUseCase(gqlQuery, gqlUseCase);
    }

    @ShopProductScope
    @Provides
    public GetShopFeaturedProductUseCase getShopFeaturedProductUseCase(@Named(GQLQueryConstant.SHOP_FEATURED_PRODUCT)
                                                                       String gqlQuery,
                                                                       MultiRequestGraphqlUseCase gqlUseCase){
        return new GetShopFeaturedProductUseCase(gqlQuery, gqlUseCase);
    }

    @ShopProductScope
    @Provides
    public GqlGetShopProductUseCase getShopProductUseCase(@Named(GQLQueryConstant.SHOP_PRODUCT)
                                                                               String gqlQuery,
                                                                  MultiRequestGraphqlUseCase gqlUseCase){
        return new GqlGetShopProductUseCase(gqlQuery, gqlUseCase);
    }

    @Provides
    public GMAuthInterceptor provideGMAuthInterceptor(@ApplicationContext Context context,
                                                      AbstractionRouter abstractionRouter) {
        return new GMAuthInterceptor(context, abstractionRouter);
    }

    @ShopProductGMFeaturedQualifier
    @Provides
    public OkHttpClient provideGMOkHttpClient(GMAuthInterceptor gmAuthInterceptor,
                                              @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                              HeaderErrorResponseInterceptor errorResponseInterceptor,
                                              CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(gmAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopProductGMFeaturedQualifier
    @ShopProductScope
    @Provides
    public Retrofit provideGMRetrofit(@ShopProductGMFeaturedQualifier OkHttpClient okHttpClient,
                                      Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @ShopProductScope
    @Provides
    public GMCommonApi provideGMCommonApi(@ShopProductGMFeaturedQualifier Retrofit retrofit) {
        return retrofit.create(GMCommonApi.class);
    }

    @ShopProductScope
    @Provides
    public GMCommonCloudDataSource provideGMCommonCloudDataSource(GMCommonApi gmCommonApi) {
        return new GMCommonCloudDataSource(gmCommonApi);
    }

    @ShopProductScope
    @Provides
    public GMCommonDataSource provideGMCommonDataSource(GMCommonCloudDataSource gmCommonCloudDataSource) {
        return new GMCommonDataSource(gmCommonCloudDataSource);
    }

    @ShopProductScope
    @Provides
    public GMCommonRepository provideGMCommonRepository(GMCommonDataSource gmCommonDataSource) {
        return new GMCommonRepositoryImpl(gmCommonDataSource);
    }

    // WishList
    @Provides
    public WishListAuthInterceptor provideWishListAuthInterceptor(@ApplicationContext Context context,
                                                                  NetworkRouter networkRouter,
                                                                  UserSessionInterface userSessionInterface) {
        return new WishListAuthInterceptor(context, networkRouter, userSessionInterface);
    }

    @ShopProductWishListFeaturedQualifier
    @Provides
    public OkHttpClient provideWishListOkHttpClient(WishListAuthInterceptor wishListAuthInterceptor,
                                                    @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                                    HeaderErrorResponseInterceptor errorResponseInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(wishListAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopProductWishListFeaturedQualifier
    @ShopProductScope
    @Provides
    public Retrofit provideWishListRetrofit(@ShopProductWishListFeaturedQualifier OkHttpClient okHttpClient,
                                            Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(WishListCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @ShopProductScope
    @Provides
    public WishListCommonApi provideWishListCommonApi(@ShopProductWishListFeaturedQualifier Retrofit retrofit) {
        return retrofit.create(WishListCommonApi.class);
    }

    @ShopProductScope
    @Provides
    public WishListProductListMapper provideWishListProductListMapper() {
        return new WishListProductListMapper();
    }

    @ShopProductScope
    @Provides
    public WishListCommonCloudDataSource provideWishListCommonCloudDataSource(WishListCommonApi wishListCommonApi) {
        return new WishListCommonCloudDataSource(wishListCommonApi);
    }

    @ShopProductScope
    @Provides
    public WishListCommonDataSource provideWishListCommonDataSource(WishListCommonCloudDataSource wishListCommonCloudDataSource) {
        return new WishListCommonDataSource(wishListCommonCloudDataSource);
    }

    @ShopProductScope
    @Provides
    public WishListCommonRepository provideWishListCommonRepository(WishListCommonDataSource wishListCommonDataSource) {
        return new WishListCommonRepositoryImpl(wishListCommonDataSource);
    }

    @ShopProductScope
    @Provides
    public GetWishListUseCase provideGetWishListUseCase(WishListCommonRepository wishListCommonRepository) {
        return new GetWishListUseCase(wishListCommonRepository);
    }

    @ShopProductScope
    @Provides
    public AddWishListUseCase provideAddToWishListUseCase(@ApplicationContext Context context) {
        return new AddWishListUseCase(context);
    }

    @ShopProductScope
    @Provides
    public RemoveWishListUseCase provideRemoveFromWishListUseCase(@ApplicationContext Context context) {
        return new RemoveWishListUseCase(context);
    }

    // Product
    @Provides
    public ShopOfficialStoreAuthInterceptor provideShopOfficialStoreAuthInterceptor(@ApplicationContext Context context,
                                                                                    NetworkRouter networkRouter,
                                                                                    UserSessionInterface userSessionInterface) {
        return new ShopOfficialStoreAuthInterceptor(context, networkRouter,userSessionInterface );
    }

    @ShopProductQualifier
    @Provides
    public OkHttpClient provideOfficialStoreOkHttpClient(ShopOfficialStoreAuthInterceptor shopOfficialStoreAuthInterceptor,
                                                         @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                                         HeaderErrorResponseInterceptor errorResponseInterceptor,
                                                         CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(shopOfficialStoreAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopProductQualifier
    @ShopProductScope
    @Provides
    public Retrofit provideOfficialStoreRetrofit(@ShopProductQualifier OkHttpClient okHttpClient, Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopUrl.BASE_OFFICIAL_STORE_URL).client(okHttpClient).build();
    }

    @ShopProductScope
    @Provides
    public ShopOfficialStoreApi provideShopOfficialStoreApi(@ShopProductQualifier Retrofit retrofit) {
        return retrofit.create(ShopOfficialStoreApi.class);
    }

    @ShopProductScope
    @Provides
    public ShopProductRepository provideShopProductRepository(ShopProductCloudDataSource shopProductDataSource) {
        return new ShopProductRepositoryImpl(shopProductDataSource);
    }

    @ShopProductScope
    @Provides
    public DeleteShopProductTomeUseCase provideDeleteShopProductTomeUseCase(@ApplicationContext Context context) {
        return new DeleteShopProductTomeUseCase(context);
    }

    @ShopProductScope
    @Provides
    public DeleteShopProductAceUseCase provideDeleteShopProductAceUseCase(@ApplicationContext Context context) {
        return new DeleteShopProductAceUseCase(context);
    }

    @ShopProductScope
    @Provides
    public GetProductCampaignsUseCase provideGetProductCampaignsUseCase(ShopProductRepository wishListCommonRepository) {
        return new GetProductCampaignsUseCase(wishListCommonRepository);
    }

    @ShopProductScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}