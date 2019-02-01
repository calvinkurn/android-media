package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.tkpd.home.favorite.data.FavoriteShopResponseValidator;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.entity.home.FavoritShopResponseData;
import com.tokopedia.tkpd.home.favorite.data.mapper.FavoritShopGraphQlMapper;
import com.tokopedia.tkpd.home.favorite.data.source.apis.FavoriteShopAuthService;
import com.tokopedia.core.network.entity.home.TopAdsHome;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.mapper.AddFavoriteShopMapper;
import com.tokopedia.tkpd.home.favorite.domain.interactor.AddFavoriteShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetFavoriteShopUsecase;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.tokopedia.tkpd.R;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author Kulomady on 1/19/17.
 */
public class CloudFavoriteShopDataSource {
    private Context context;
    private Gson gson;
    private ServiceV4 serviceV4;
    private final FavoriteShopAuthService favoriteShopAuthService;

    public CloudFavoriteShopDataSource(Context context, Gson gson, ServiceV4 serviceV4) {
        this.context = context;
        this.gson = gson;
        this.serviceV4 = serviceV4;
        favoriteShopAuthService = new FavoriteShopAuthService();
    }

    public Observable<FavoriteShop> getFavorite(
            TKPDMapParam<String, String> param, boolean isMustSaveToCache) {

        TKPDMapParam<String, String> paramWithAuth = AuthUtil.generateParamsNetwork(context, param);
        if (isMustSaveToCache) {
            Observable<FavoriteShop> observable = favoriteShopAuthService.getApi()
                    .getFavoritShopsData(getRequestPayload(context, paramWithAuth))
                    .doOnNext(validateResponse())
                    .map(new FavoritShopGraphQlMapper(context, gson));
            return observable;
        } else {
            Observable<FavoriteShop> observable = favoriteShopAuthService.getApi()
                    .getFavoritShopsData(getRequestPayload(context, paramWithAuth))
                    .map(new FavoritShopGraphQlMapper(context, gson));
            return observable;
        }
    }

    private String getRequestPayload(Context context, TKPDMapParam<String, String> params) {

        return String.format(
                loadRawString(context.getResources(), R.raw.favorit_shop_query),
                params.get(GetFavoriteShopUsecase.KEY_PAGE),
                params.get(GetFavoriteShopUsecase.KEY_PER_PAGE)
        );
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }

    @NonNull
    private Action1<Response<GraphqlResponse<FavoritShopResponseData>>> validateResponse() {
        return FavoriteShopResponseValidator.validate(new FavoriteShopResponseValidator.HttpValidationListener() {
            @Override
            public void OnPassValidation(Response<GraphqlResponse<FavoritShopResponseData>> response) {
                saveResponseToCache(response);
            }
        });
    }

    public Observable<FavShop> postFavoriteShop(TKPDMapParam<String, String> param) {
        String shopId = param.get(AddFavoriteShopUseCase.KEY_SHOP_ID);
        TKPDMapParam<String, String> paramWithAuth = AuthUtil.generateParamsNetwork(context, param);
        return serviceV4.postFavoriteShop(paramWithAuth)
                .doOnNext(updateTopAdsShopCache(shopId))
                .map(new AddFavoriteShopMapper(context, gson));
    }

    private Action1<? super Response<String>> updateTopAdsShopCache(final String shopId) {
        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> stringResponse) {
                try {
                    String valueString
                            = new GlobalCacheManager().getValueString(TkpdCache.Key.TOP_ADS_SHOP);
                    TopAdsHome topAdsResponse = gson.fromJson(valueString, TopAdsHome.class);
                    for (TopAdsHome.Data data : topAdsResponse.getData()) {
                        if (data.getHeadline().getShop().getId().equalsIgnoreCase(shopId)) {
                            data.isSelected = true;
                        }
                    }
                    String topadsShopUpdated = gson.toJson(topAdsResponse);
                    new GlobalCacheManager()
                            .setKey(TkpdCache.Key.TOP_ADS_SHOP)
                            .setValue(topadsShopUpdated)
                            .store();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

            }
        };
    }


    private void saveResponseToCache(Response<GraphqlResponse<FavoritShopResponseData>> response) {
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.FAVORITE_SHOP)
                .setValue(response.body().toString())
                .store();
    }


}
