package com.tokopedia.tkpd.home.favorite.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.WishlistMapper;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Kulomady on 2/13/17.
 */
public class LocalWishlistDataSource {

    private final Context context;
    private final Gson gson;
    private final GlobalCacheManager cacheManager;

    public LocalWishlistDataSource(Context context, Gson gson, GlobalCacheManager cacheManager) {
        this.context = context;
        this.gson = gson;
        this.cacheManager = cacheManager;
    }

    public Observable<DomainWishlist> getWishlist() {
        Response<String> data
                = Response.success(cacheManager.getValueString(TkpdCache.Key.WISHLIST));
        return Observable.just(data)
                .map(new WishlistMapper(context, gson))
                .onErrorReturn(nullResponse());
    }

    @NonNull
    private Func1<Throwable, DomainWishlist> nullResponse() {
        return new Func1<Throwable, DomainWishlist>() {
            @Override
            public DomainWishlist call(Throwable throwable) {
                return null;
            }
        };
    }
}
