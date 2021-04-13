package com.tokopedia.shop.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant;
import com.tokopedia.shop.common.constant.GqlQueryConstant;
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor;
import com.tokopedia.shop.common.data.repository.ShopCommonRepositoryImpl;
import com.tokopedia.shop.common.data.source.ShopCommonDataSource;
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopFavoriteStatusUseCase;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import static com.tokopedia.shop.common.constant.GQLQueryNamedConstant.DEFAULT_SHOP_INFO_QUERY_NAME;
import static com.tokopedia.shop.common.constant.GQLQueryNamedConstant.SHOP_INFO_CORE_AND_ASSETS_QUERY_NAME;
import static com.tokopedia.shop.common.constant.GQLQueryNamedConstant.SHOP_INFO_FAVORITE_QUERY_NAME;
import static com.tokopedia.shop.common.constant.GQLQueryNamedConstant.SHOP_INFO_HEADER_CONTENT_DATA_QUERY_NAME;
import static com.tokopedia.shop.common.constant.GQLQueryNamedConstant.SHOP_TOP_CONTENT_QUERY_NAME;
import static com.tokopedia.shop.common.constant.GqlQueryConstant.GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS_QUERY_STRING;
import static com.tokopedia.shop.common.constant.GqlQueryConstant.QUERY_SHOP_SCORE_STRING;
import static com.tokopedia.shop.common.constant.GqlQueryConstant.SHOP_REPUTATION_QUERY_STRING;

@Module
public class ShopCommonModule {
    @Provides
    public CoroutineDispatcher provideMainDispatcher() {
        return Dispatchers.getMain();
    }

    @Provides
    public ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase(@ApplicationContext Context context) {
        return new ToggleFavouriteShopUseCase(new GraphqlUseCase(), context.getResources());
    }

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO)
    public String provideGqlQueryShopInfo(@ApplicationContext Context context){
        return GqlQueryConstant.INSTANCE.getShopInfoQuery(
                GqlQueryConstant.SHOP_INFO_REQUEST_QUERY_STRING,
                DEFAULT_SHOP_INFO_QUERY_NAME
        );
    }

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_HEADER)
    public String provideGqlQueryShopInfoForHeader(@ApplicationContext Context context){
        return GqlQueryConstant.INSTANCE.getShopInfoQuery(
                GqlQueryConstant.SHOP_INFO_FOR_HEADER_REQUEST_QUERY_STRING,
                SHOP_INFO_HEADER_CONTENT_DATA_QUERY_NAME
        );
    }

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_TOP_CONTENT)
    public String provideGqlQueryShopInfoForTopContent(@ApplicationContext Context context){
        return GqlQueryConstant.INSTANCE.getShopInfoQuery(
                GqlQueryConstant.SHOP_INFO_FOR_TOP_CONTENT_REQUEST_QUERY_STRING,
                SHOP_TOP_CONTENT_QUERY_NAME
        );
    }

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS)
    public String provideGqlQueryShopInfoForCoreAndAssets(@ApplicationContext Context context){
        return GqlQueryConstant.INSTANCE.getShopInfoQuery(
                GqlQueryConstant.SHOP_INFO_FOR_CORE_AND_ASSETS_REQUEST_QUERY_STRING,
                SHOP_INFO_CORE_AND_ASSETS_QUERY_NAME
        );
    }

    @Provides
    @Named(GQLQueryNamedConstant.FAVORITE_STATUS_GQL)
    public String provideGqlQueryFavoriteStatus(@ApplicationContext Context context) {
        return GqlQueryConstant.INSTANCE.getShopInfoQuery(
                GqlQueryConstant.FAVORITE_STATUS_GQL_STRING,
                SHOP_INFO_FAVORITE_QUERY_NAME
        );
    }

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_REPUTATION)
    public String provideGqlQueryShopReputation(@ApplicationContext Context context){
        return SHOP_REPUTATION_QUERY_STRING;
    }

    @Provides
    @Named(GQLQueryNamedConstant.GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS)
    public String provideGqlQueryShopOperationalHourStatus(@ApplicationContext Context context){
        return GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS_QUERY_STRING;
    }


    @Provides
    public GetShopInfoByDomainUseCase provideGetShopInfoByDomainUseCase(ShopCommonRepository shopCommonRepository) {
        return new GetShopInfoByDomainUseCase(shopCommonRepository);
    }

    @Provides
    public ShopCommonRepository provideShopCommonRepository(ShopCommonDataSource shopInfoDataSource) {
        return new ShopCommonRepositoryImpl(shopInfoDataSource);
    }
    @Provides
    public ShopCommonCloudDataSource provideShopCommonCloudDataSource(ShopCommonApi shopCommonApi) {
        return new ShopCommonCloudDataSource(shopCommonApi);
    }

    @Provides
    public ShopCommonDataSource provideShopCommonDataSource(ShopCommonCloudDataSource shopInfoCloudDataSource) {
        return new ShopCommonDataSource(shopInfoCloudDataSource);
    }

    @Provides
    public ShopCommonApi provideShopCommonApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(ShopCommonApi.class);
    }

    @ShopQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(ShopAuthInterceptor shopAuthInterceptor,
                                            @ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            ErrorResponseInterceptor errorResponseInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(shopAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @ShopQualifier
    @Provides
    public Retrofit provideRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TkpdV4ResponseError.class);
    }


    @ShopWSQualifier
    @Provides
    public Retrofit provideWSRetrofit(@ShopQualifier OkHttpClient okHttpClient,
                                      Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_WS_URL).client(okHttpClient).build();
    }

    @Provides
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context){
        return (NetworkRouter)context;
    }

    @Provides
    @Named(ShopCommonParamApiConstant.QUERY_SHOP_SCORE)
    public String provideQueryShopScore(@ApplicationContext Context context) {
        return QUERY_SHOP_SCORE_STRING;
    }

    @Provides
    public GQLGetShopFavoriteStatusUseCase provideFavorite(MultiRequestGraphqlUseCase graphqlUseCase,
                                                           @Named(GQLQueryNamedConstant.FAVORITE_STATUS_GQL)
                                                                   String gqlQuery) {
        return new GQLGetShopFavoriteStatusUseCase(gqlQuery, graphqlUseCase);
    }

    @Provides
    public GQLGetShopInfoUseCase provideGqlGetShopInfoUseCase(MultiRequestGraphqlUseCase graphqlUseCase,
                                                              @Named(GQLQueryNamedConstant.SHOP_INFO)
                                                                    String gqlQuery) {
        return new GQLGetShopInfoUseCase(gqlQuery, graphqlUseCase);
    }

    @GqlGetShopInfoForHeaderUseCaseQualifier
    @Provides
    public GQLGetShopInfoUseCase provideGqlGetShopInfoForHeaderUseCase(MultiRequestGraphqlUseCase graphqlUseCase,
                                                                    @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_HEADER)
                                                                            String gqlQuery) {
        return new GQLGetShopInfoUseCase(gqlQuery, graphqlUseCase);
    }

    @GqlGetShopInfoUseCaseTopContentQualifier
    @Provides
    public GQLGetShopInfoUseCase provideGqlGetShopInfoUseCaseTopContent(MultiRequestGraphqlUseCase graphqlUseCase,
                                                                    @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_TOP_CONTENT)
                                                                            String gqlQuery) {
        return new GQLGetShopInfoUseCase(gqlQuery, graphqlUseCase);
    }

    @GqlGetShopInfoUseCaseCoreAndAssetsQualifier
    @Provides
    public GQLGetShopInfoUseCase provideGqlGetShopInfoUseCaseCoreAndAssets(MultiRequestGraphqlUseCase graphqlUseCase,
                                                                    @Named(GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS)
                                                                            String gqlQuery) {
        return new GQLGetShopInfoUseCase(gqlQuery, graphqlUseCase);
    }

}
