package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.mapper.WishlistMapper;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author Kulomady on 1/18/17.
 */
public class CloudWishlistDataStore {

    private Context context;
    private MojitoService mojitoService;
    private Gson gson;

    public CloudWishlistDataStore(Context context, Gson gson, MojitoService mojitoService) {
        this.context = context;
        this.gson = gson;
        this.mojitoService = mojitoService;
    }

    public Observable<DomainWishlist> getWishlist(String userId, TKPDMapParam<String, Object> param) {
        return mojitoService.getWishlist(userId, param)
                .doOnNext(validateError())
                .map(new WishlistMapper(context, gson));
    }

    public Action1<Response<String>> saveToCache() {

        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> response) {
                saveResponseToCache(response);
            }
        };
    }


    private Action1<Response<String>> validateError() {
        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> stringResponse) {
                if (stringResponse.code() >= 500 && stringResponse.code() < 600) {
                    throw new RuntimeException("Server Error!");
                } else if (stringResponse.code() >= 400 && stringResponse.code() < 500) {
                    throw new RuntimeException("Client Error!");
                } else {
                    saveResponseToCache(stringResponse);
                }
            }
        };
    }

    private void saveResponseToCache(Response<String> response) {
        int tenMinute = 600000;
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.WISHLIST)
                .setCacheDuration(tenMinute)
                .setValue(response.body())
                .store();
    }
}
