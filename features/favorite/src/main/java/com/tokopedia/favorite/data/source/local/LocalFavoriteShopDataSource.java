package com.tokopedia.favorite.data.source.local;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.cachemanager.CacheManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.favorite.data.mapper.FavoriteShopMapper;
import com.tokopedia.favorite.domain.model.FavoriteShop;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Kulomady on 2/13/17.
 */
public class LocalFavoriteShopDataSource {

    public static final String CACHE_KEY_FAVORITE_SHOP = "FAVORITE_SHOP";

    private Context context;
    private Gson gson;

    public LocalFavoriteShopDataSource(Context context,
                                       Gson gson) {
        this.context = context;
        this.gson = gson;
    }

    public Observable<FavoriteShop> getFavorite() {

        Response<String> data
                = Response.success(PersistentCacheManager.instance.getString(CACHE_KEY_FAVORITE_SHOP, null));
        return Observable.just(data)
                .map(new FavoriteShopMapper(context, gson))
                .onErrorReturn(nullResponse());
    }

    @NonNull
    private Func1<Throwable, FavoriteShop> nullResponse() {
        return new Func1<Throwable, FavoriteShop>() {
            @Override
            public FavoriteShop call(Throwable throwable) {
                FavoriteShop favoriteShop = new FavoriteShop();
                favoriteShop.setDataIsValid(false);
                return favoriteShop;
            }
        };
    }
}
