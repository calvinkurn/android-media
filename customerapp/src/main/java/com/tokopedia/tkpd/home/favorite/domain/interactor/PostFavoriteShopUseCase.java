package com.tokopedia.tkpd.home.favorite.domain.interactor;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;

import rx.Observable;

/**
 * @author by erry on 31/01/17.
 */

public class PostFavoriteShopUseCase extends UseCase<FavShop> {

    public static final String KEY_SHOP_ITEM = "KEY_SHOP_ITEM";

    private static final String KEY_SRC_DEFAULT_VALUE = "fav_shop";

    private static final String KEY_AD = "ad_key";
    private static final String KEY_SHOP_ID = "shop_id";
    private static final String KEY_SRC = "src";


    private final FavoriteRepository mFavoriteRepository;

    public PostFavoriteShopUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   FavoriteRepository mFavoriteRepository) {
        super(threadExecutor, postExecutionThread);
        this.mFavoriteRepository = mFavoriteRepository;
    }

    @Override
    public Observable<FavShop> createObservable(RequestParams requestParams) {
        ShopItemParam shopItems = (ShopItemParam) requestParams.getObject(KEY_SHOP_ITEM);
        return mFavoriteRepository.postFavShop(getParamsInstring(shopItems), shopItems);
    }

    private TKPDMapParam<String, String> getParamsInstring(ShopItemParam shopItemParam) {

        TKPDMapParam<String, String> results = new TKPDMapParam<>();
        results.put(KEY_AD, shopItemParam.getAdKey());
        results.put(KEY_SHOP_ID, shopItemParam.getShopId());
        results.put(KEY_SRC, KEY_SRC_DEFAULT_VALUE);
        return results;
    }

}
