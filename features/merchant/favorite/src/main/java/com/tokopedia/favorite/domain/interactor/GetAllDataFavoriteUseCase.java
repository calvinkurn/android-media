package com.tokopedia.favorite.domain.interactor;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.favorite.domain.model.DataFavorite;
import com.tokopedia.favorite.domain.model.FavoriteShop;
import com.tokopedia.favorite.domain.model.TopAdsShop;
import com.tokopedia.topads.sdk.utils.CacheHandler;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.functions.Func2;

/**
 * @author Kulomady on 2/9/17.
 */

public class GetAllDataFavoriteUseCase extends UseCase<DataFavorite> {

    private final GetFavoriteShopUsecase getFavoriteShopUsecase;
    private final GetTopAdsShopUseCase getTopAdsShopUseCase;
    private final Context context;
    private final CacheHandler cacheHandler;
    private final Random random;
    private UserSessionInterface userSession;

    public GetAllDataFavoriteUseCase(Context context,
                                     GetFavoriteShopUsecase getFavoriteShopUsecase,
                                     GetTopAdsShopUseCase GetTopAdsShopUseCase) {
        this.context = context;
        this.getFavoriteShopUsecase = getFavoriteShopUsecase;
        this.getTopAdsShopUseCase = GetTopAdsShopUseCase;
        this.cacheHandler = new CacheHandler(context, CacheHandler.TOP_ADS_CACHE);
        random = new Random();
        userSession = new UserSession(context);

    }

    @Override
    public Observable<DataFavorite> createObservable(RequestParams requestParams) {
        return Observable.zip(getTopAdsShop(), getFavoriteShopList(),
                new Func2<TopAdsShop, FavoriteShop, DataFavorite>() {

            @Override
            public DataFavorite call(TopAdsShop adsShop, FavoriteShop favoriteShop) {

                return validateDataFavorite(adsShop, favoriteShop);
            }
        });
    }

    @NonNull
    private DataFavorite validateDataFavorite(TopAdsShop adsShop,
                                              FavoriteShop favoriteShop) {

        if (adsShop.isNetworkError()
                && favoriteShop.isNetworkError()
                && adsShop.getTopAdsShopItemList() == null
                && favoriteShop.getData() == null) {
            throw new RuntimeException("all network error");
        }

        DataFavorite dataFavorite = new DataFavorite();
        dataFavorite.setTopAdsShop(adsShop);
        dataFavorite.setFavoriteShop(favoriteShop);
        return dataFavorite;
    }


    private Observable<TopAdsShop> getTopAdsShop() {
        RequestParams requestParams = GetTopAdsShopUseCase.Companion.defaultParams();
        requestParams.putBoolean(GetTopAdsShopUseCase.KEY_IS_FORCE_REFRESH, true);
        requestParams.putString(GetTopAdsShopUseCase.KEY_USER_ID, userSession.getUserId());

        ArrayList<Integer> preferredCacheList
                = cacheHandler.getArrayListInteger(CacheHandler.KEY_PREFERRED_CATEGORY);
        requestParams.putInt(GetTopAdsShopUseCase.KEY_DEP_ID, getRandomId(preferredCacheList));
        return getTopAdsShopUseCase.createObservable(requestParams);
    }

    private Observable<FavoriteShop> getFavoriteShopList(){
        RequestParams defaultParams = GetFavoriteShopUsecase.Companion.getDefaultParams();
        defaultParams.putBoolean(GetFavoriteShopUsecase.KEY_IS_FIRST_PAGE, true);
        return getFavoriteShopUsecase.createObservable(defaultParams);
    }

    private int getRandomId(List<Integer> ids) {
        if (ids.size() > 0) {
            return ids.get(random.nextInt(ids.size()));
        } else {
            return 0;
        }
    }
}
