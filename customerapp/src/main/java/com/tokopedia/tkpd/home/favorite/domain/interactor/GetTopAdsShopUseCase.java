package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import rx.Observable;

/**
 * @author Kulomady on 1/19/17.
 */

public class GetTopAdsShopUseCase extends UseCase<TopAdsShop> {

    public static final String TOPADS_PAGE_DEFAULT_VALUE = "1";
    public static final String TOPADS_ITEM_DEFAULT_VALUE = "4";
    public static final String SRC_FAV_SHOP_VALUE = "fav_shop";

    public static final String KEY_ITEM = "item";
    public static final String KEY_SRC = "src";
    public static final String KEY_PAGE = "page";

    private final FavoriteRepository favoriteRepository;

    public GetTopAdsShopUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                FavoriteRepository favoriteRepository) {

        super(threadExecutor, postExecutionThread);
        this.favoriteRepository = favoriteRepository;
    }


    @Override
    public Observable<TopAdsShop> createObservable(RequestParams requestParams) {
        return favoriteRepository.getTopAdsShop(requestParams.getParameters());
    }

    public static RequestParams getDefaultParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KEY_PAGE, TOPADS_PAGE_DEFAULT_VALUE);
        requestParams.putString(KEY_ITEM, TOPADS_ITEM_DEFAULT_VALUE);
        requestParams.putString(KEY_SRC, SRC_FAV_SHOP_VALUE);
        return requestParams;
    }
}
