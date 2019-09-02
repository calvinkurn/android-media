package com.tokopedia.tkpd.home.favorite.data.source.local;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.mapper.FavoriteShopMapper;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Kulomady on 2/13/17.
 */
public class LocalFavoriteShopDataSource {

    private Context context;
    private Gson gson;
    private GlobalCacheManager cacheManager;

    public LocalFavoriteShopDataSource(Context context,
                                       Gson gson, GlobalCacheManager cacheManager) {
        this.context = context;
        this.gson = gson;
        this.cacheManager = cacheManager;
    }

    public Observable<FavoriteShop> getFavorite() {
        Response<String> data
                = Response.success(cacheManager.getValueString(TkpdCache.Key.FAVORITE_SHOP));
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
