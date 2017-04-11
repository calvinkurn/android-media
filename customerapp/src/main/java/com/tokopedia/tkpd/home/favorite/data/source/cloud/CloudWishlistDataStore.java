package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.utils.HttpResponseValidator;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.mapper.WishlistMapper;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;

import retrofit2.Response;
import rx.Observable;

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
                .doOnNext(HttpResponseValidator
                        .validate(new HttpResponseValidator.HttpValidationListener() {
                            @Override
                            public void OnPassValidation(Response<String> response) {
                                saveResponseToCache(response);
                            }
                        }))
                .map(new WishlistMapper(context, gson));
    }

    private void saveResponseToCache(Response<String> response) {
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.WISHLIST)
                .setValue(response.body())
                .store();
    }
}
