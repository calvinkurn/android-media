package com.tokopedia.shop.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.shop.common.R;
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant;
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.constant.ShopCommonUrl;
import com.tokopedia.shop.common.data.interceptor.ShopAuthInterceptor;
import com.tokopedia.shop.common.data.repository.ShopCommonRepositoryImpl;
import com.tokopedia.shop.common.data.source.ShopCommonDataSource;
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopFavoriteStatusUseCase;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.GetBroadcasterShopConfigUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;
import com.tokopedia.shop.common.util.CacheApiTKPDResponseValidator;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

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
        return "query getShopInfo($shopIds: [Int!]!, $fields: [String!]!, $shopDomain: String, $source: String){\n" +
                "    shopInfoByID(input: {\n" +
                "        shopIDs: $shopIds,\n" +
                "        fields: $fields,\n" +
                "        domain: $shopDomain,\n" +
                "        source: $source\n" +
                "    }){\n" +
                "        result {\n" +
                "            shopCore{\n" +
                "                shopID\n" +
                "                description\n" +
                "                domain\n" +
                "                name\n" +
                "                tagLine\n" +
                "                url\n" +
                "            }\n" +
                "            freeOngkir{\n" +
                "                isActive\n" +
                "                imgURL\n" +
                "            }\n" +
                "            closedInfo{\n" +
                "                closedNote\n" +
                "                reason\n" +
                "                until\n" +
                "            }\n" +
                "            createInfo{\n" +
                "                openSince\n" +
                "            }\n" +
                "            shopAssets{\n" +
                "                avatar\n" +
                "                cover\n" +
                "            }\n" +
                "            shipmentInfo{\n" +
                "                isAvailable\n" +
                "                code\n" +
                "                shipmentID\n" +
                "                image\n" +
                "                name\n" +
                "                product{\n" +
                "                    isAvailable\n" +
                "                    productName\n" +
                "                    shipProdID\n" +
                "                    uiHidden\n" +
                "                }\n" +
                "                isPickup\n" +
                "                maxAddFee\n" +
                "                awbStatus\n" +
                "            }\n" +
                "            shopLastActive\n" +
                "            location\n" +
                "            isAllowManage\n" +
                "            goldOS {\n" +
                "                isGold\n" +
                "                isGoldBadge\n" +
                "                isOfficial\n" +
                "            }\n" +
                "            favoriteData{\n" +
                "                totalFavorite\n" +
                "                alreadyFavorited\n" +
                "            }\n" +
                "            statusInfo {\n" +
                "                shopStatus\n" +
                "                statusMessage\n" +
                "                statusTitle\n" +
                "            }\n" +
                "            bbInfo {\n" +
                "                bbName\n" +
                "                bbNameEN\n" +
                "                bbDesc\n" +
                "                bbDescEN\n" +
                "            }\n" +
                "            topContent{\n" +
                "                topURL\n" +
                "            }\n" +
                "            address {\n" +
                "                id\n" +
                "                name\n" +
                "                address\n" +
                "                area\n" +
                "                email\n" +
                "                phone\n" +
                "                fax\n" +
                "            }\n" +
                "            shopHomeType\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    @Provides
    @Named(GQLQueryNamedConstant.SHOP_REPUTATION)
    public String provideGqlQueryShopReputation(@ApplicationContext Context context){
        return "query getShopBadge($shopIds: [Int!]!){\n" +
                "     reputation_shops(shop_ids: $shopIds) {\n" +
                "         badge\n" +
                "         badge_hd\n" +
                "         score\n" +
                "         score_map\n" +
                "     }\n" +
                " }";
    }

    @Provides
    @Named(GQLQueryNamedConstant.GQL_GET_SHOP_OPERATIONAL_HOUR_STATUS)
    public String provideGqlQueryShopOperationalHourStatus(@ApplicationContext Context context){
        return "query getShopOperationalHourStatus($shopID: String!, $type:Int!) {\n" +
                "    getShopOperationalHourStatus(shopID: $shopID, type: $type) {\n" +
                "        timestamp\n" +
                "      \tstatusActive\n" +
                "      \tstartTime\n" +
                "\t\tendTime\n" +
                "      \terror{\n" +
                "          message\n" +
                "        }\n" +
                "        tickerTitle\n" +
                "        tickerMessage\n" +
                "    }\n" +
                "}";
    }

    @Provides
    public GetShopInfoByDomainUseCase provideGetShopInfoByDomainUseCase(ShopCommonRepository shopCommonRepository) {
        return new GetShopInfoByDomainUseCase(shopCommonRepository);
    }

    @Provides
    public DeleteShopInfoCacheUseCase provideDeleteShopInfoCacheUseCase(@ApplicationContext Context context) {
        return new DeleteShopInfoCacheUseCase(context);
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
                                            ErrorResponseInterceptor errorResponseInterceptor,
                                            CacheApiInterceptor cacheApiInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(cacheApiInterceptor)
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
    public CacheApiInterceptor provideApiCacheInterceptor(@ApplicationContext Context context) {
        return new CacheApiInterceptor(context, new CacheApiTKPDResponseValidator<>(TkpdV4ResponseError.class));
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
        return "query getShopScore($shopId: String!) {\n" +
                "  shopScore(input: {shopID: $shopId}) {\n" +
                "    result {\n" +
                "      shopID\n" +
                "      shopScore\n" +
                "      shopScoreSummary {\n" +
                "        title\n" +
                "        value\n" +
                "        maxValue\n" +
                "        color\n" +
                "        description\n" +
                "      }\n" +
                "      badgeScore\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    @Provides
    @Named(GQLQueryNamedConstant.FAVORITE_STATUS_GQL)
    public String provideGqlQueryFavoriteStatus(@ApplicationContext Context context) {
        return "query getShopInfo($shopIds: [Int!]!, $fields: [String!]!, $shopDomain: String){\n" +
                "     shopInfoByID(input: {\n" +
                "         shopIDs: $shopIds,\n" +
                "         fields: $fields,\n" +
                "         domain: $shopDomain}){\n" +
                "         result {\n" +
                "             favoriteData{\n" +
                "                 totalFavorite\n" +
                "                 alreadyFavorited\n" +
                "             }\n" +
                "         }\n" +
                "     }\n" +
                " }";
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

    @Provides
    public GetBroadcasterShopConfigUseCase provideGetBroadcasterShopConfigUseCase(MultiRequestGraphqlUseCase graphqlUseCase) {
        return new GetBroadcasterShopConfigUseCase(graphqlUseCase);
    }
}
