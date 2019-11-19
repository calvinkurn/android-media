package com.tokopedia.tkpd.home.favorite.domain.interactor;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.home.favorite.domain.model.DataFavorite;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;
import com.tokopedia.topads.sdk.utils.CacheHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.functions.Func2;


/**
 * @author Kulomady on 1/19/17.
 */

public class GetInitialDataPageUsecase extends UseCase<DataFavorite> {

    private GetFavoriteShopUsecase getFavoriteShopUsecase;
    private GetWishlistUtil getWishlistUtil;
    private final GetTopAdsShopUseCase getTopAdsShopUseCase;
    private final Context context;
    private final CacheHandler cacheHandler;
    private final Random random;

    public GetInitialDataPageUsecase(Context context, ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     GetFavoriteShopUsecase getFavoriteShopUsecase,
                                     GetWishlistUtil getWishlistUtil,
                                     GetTopAdsShopUseCase getTopAdsShopUseCase) {

        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.getFavoriteShopUsecase = getFavoriteShopUsecase;
        this.getWishlistUtil = getWishlistUtil;
        this.getTopAdsShopUseCase = getTopAdsShopUseCase;
        this.cacheHandler = new CacheHandler(context, CacheHandler.TOP_ADS_CACHE);
        random = new Random();
    }

    @Override
    public Observable<DataFavorite> createObservable(RequestParams requestParams) {
        return Observable.zip(getTopAdsShop(), getFavoriteShop(),
                new Func2<TopAdsShop, FavoriteShop, DataFavorite>() {

                    @Override
                    public DataFavorite call(TopAdsShop adsShop, FavoriteShop favoriteShop) {
                        return validateDataFavorite(adsShop, favoriteShop);
                    }
                });
    }


    @NonNull
    private DataFavorite validateDataFavorite(TopAdsShop adsShop, FavoriteShop favoriteShop) {

        if (adsShop.isNetworkError()
                && favoriteShop.isNetworkError()
                && adsShop.getTopAdsShopItemList() == null
                && favoriteShop.getData() == null) {

            throw new RuntimeException("all request network error");
        }

        DataFavorite dataFavorite = new DataFavorite();
        dataFavorite.setTopAdsShop(adsShop);
        dataFavorite.setFavoriteShop(favoriteShop);
        return dataFavorite;
    }

    public Observable<FavoriteShop> getFavoriteShop() {
        RequestParams defaultParams = GetFavoriteShopUsecase.getDefaultParams();
        defaultParams.putBoolean(GetFavoriteShopUsecase.KEY_IS_FIRST_PAGE, true);
        return getFavoriteShopUsecase.createObservable(defaultParams);
    }

    public Observable<DomainWishlist> getWishlist() {
        RequestParams params = GetWishlistUtil.getDefaultParams();
        params.putBoolean(GetWishlistUtil.KEY_IS_FORCE_REFRESH, false);
        return getWishlistUtil.getWishListData(params);
    }

    public Observable<TopAdsShop> getTopAdsShop() {
        RequestParams requestParams = GetTopAdsShopUseCase.defaultParams();
        requestParams.putBoolean(GetTopAdsShopUseCase.KEY_IS_FORCE_REFRESH, false);
        requestParams.putString(GetTopAdsShopUseCase.KEY_USER_ID, SessionHandler.getLoginID(context));
        ArrayList<Integer> preferredCacheList
                = cacheHandler.getArrayListInteger(CacheHandler.KEY_PREFERRED_CATEGORY);
        requestParams.putInt(GetTopAdsShopUseCase.KEY_DEP_ID, getRandomId(preferredCacheList));
        return getTopAdsShopUseCase.createObservable(requestParams);
    }

    private int getRandomId(List<Integer> ids) {
        if (ids.size() > 0) {
            return ids.get(random.nextInt(ids.size()));
        } else {
            return 0;
        }
    }
}
