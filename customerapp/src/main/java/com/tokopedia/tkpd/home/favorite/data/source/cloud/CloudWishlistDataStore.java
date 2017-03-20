package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.WishlistMapper;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author Kulomady on 1/18/17.
 */
public class CloudWishlistDataStore {

    private Context mContext;
    private final MojitoService mMojitoService;
    private Gson mGson;

    public CloudWishlistDataStore(Context context, Gson gson, MojitoService mojitoService) {
        mContext = context;
        mGson = gson;
        mMojitoService = mojitoService;
    }

    public Observable<DomainWishlist> getWishlist(TKPDMapParam<String, Object> param) {
        return mMojitoService.getWishlist(SessionHandler.getLoginID(mContext), param)
                .doOnNext(saveToCache())
                .map(new WishlistMapper(mContext, mGson));
    }

    private Action1<Response<String>> saveToCache() {

        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> response) {
                int tenMinute = 600000;
                new GlobalCacheManager()
                        .setKey(TkpdCache.Key.WISHLIST)
                        .setCacheDuration(tenMinute)
                        .setValue(response.body())
                        .store();
            }
        };
    }
}
