package com.tokopedia.favorite.data.source.cloud;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.favorite.R;
import com.tokopedia.favorite.data.FavoriteShopResponseValidator;
import com.tokopedia.favorite.data.mapper.FavoritShopGraphQlMapper;
import com.tokopedia.favorite.data.source.apis.FavoriteShopAuthService;
import com.tokopedia.favorite.data.source.local.LocalFavoriteShopDataSource;
import com.tokopedia.favorite.domain.interactor.GetFavoriteShopUsecase;
import com.tokopedia.favorite.domain.model.FavoritShopResponseData;
import com.tokopedia.favorite.domain.model.FavoriteShop;
import com.tokopedia.network.data.model.response.GraphqlResponse;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author Kulomady on 1/19/17.
 */
public class CloudFavoriteShopDataSource {
    private Context context;
    private Gson gson;
    private final FavoriteShopAuthService favoriteShopAuthService;
    private UserSessionInterface userSession;

    public CloudFavoriteShopDataSource(Context context, Gson gson) {
        this.context = context;
        this.gson = gson;
        favoriteShopAuthService = new FavoriteShopAuthService();
        userSession = new UserSession(context);
    }

    public Observable<FavoriteShop> getFavorite(
            HashMap<String, String> param, boolean isMustSaveToCache) {

        TKPDMapParam<String, String> tkpdMapParam = new TKPDMapParam<>();
        tkpdMapParam.putAll(param);

        TKPDMapParam<String, String> paramWithAuth = AuthUtil.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), tkpdMapParam);

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


    private void saveResponseToCache(Response<GraphqlResponse<FavoritShopResponseData>> response) {
        PersistentCacheManager.instance.put(LocalFavoriteShopDataSource.CACHE_KEY_FAVORITE_SHOP, response.body().toString(), - System.currentTimeMillis());
    }


}
