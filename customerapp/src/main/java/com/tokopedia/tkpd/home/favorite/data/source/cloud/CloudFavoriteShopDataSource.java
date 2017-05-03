package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.core.base.utils.HttpResponseValidator;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.entity.home.TopAdsHome;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.mapper.AddFavoriteShopMapper;
import com.tokopedia.tkpd.home.favorite.data.mapper.FavoriteShopMapper;
import com.tokopedia.tkpd.home.favorite.domain.interactor.AddFavoriteShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;

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

    public CloudFavoriteShopDataSource(Context context, Gson gson, ServiceV4 serviceV4) {
        this.context = context;
        this.gson = gson;
        this.serviceV4 = serviceV4;
    }

    public Observable<FavoriteShop> getFavorite(
            TKPDMapParam<String, String> param, boolean isMustSaveToCache) {

        TKPDMapParam<String, String> paramWithAuth = AuthUtil.generateParamsNetwork(context, param);
        if (isMustSaveToCache) {
            return serviceV4.getFavoriteShop(paramWithAuth)
                    .doOnNext(validateResponse())
                    .map(new FavoriteShopMapper(context, gson));
        } else {
            return serviceV4.getFavoriteShop(paramWithAuth)
                    .map(new FavoriteShopMapper(context, gson));
        }
    }

    @NonNull
    private Action1<Response<String>> validateResponse() {
        return HttpResponseValidator.validate(new HttpResponseValidator.HttpValidationListener() {
            @Override
            public void OnPassValidation(Response<String> response) {
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
                String valueString
                        = new GlobalCacheManager().getValueString(TkpdCache.Key.TOP_ADS_SHOP);
                TopAdsHome topAdsResponse = gson.fromJson(valueString, TopAdsHome.class);
                for (TopAdsHome.Data data : topAdsResponse.getData()) {
                    if (data.shop.id.equalsIgnoreCase(shopId)) {
                        data.isSelected = true;
                    }
                }
                String topadsShopUpdated = gson.toJson(topAdsResponse);
                new GlobalCacheManager()
                        .setKey(TkpdCache.Key.TOP_ADS_SHOP)
                        .setValue(topadsShopUpdated)
                        .store();

            }
        };
    }


    private void saveResponseToCache(Response<String> response) {
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.FAVORITE_SHOP)
                .setValue(response.body())
                .store();
    }


}
