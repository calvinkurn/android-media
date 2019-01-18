package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import rx.Observable;

/**
 * @author Kulomady on 2/9/17.
 */

public class GetTopAdsShopUseCase extends UseCase<TopAdsShop> {

    public static final String KEY_IS_FORCE_REFRESH = "isForceRefresh";

    static final String KEY_ITEM = "item";
    static final String KEY_SRC = "src";
    static final String KEY_PAGE = "page";
    static final String KEY_USER_ID = "user_id";
    static final String KEY_DEP_ID = "dep_id";
    static final String KEY_EP = "ep";
    static final String KEY_TEMPLATE_ID = "template_id";
    static final String KEY_DEVICE = "device";

    static final String TOPADS_PAGE_DEFAULT_VALUE = "1";
    static final String TOPADS_ITEM_DEFAULT_VALUE = "4";
    static final String SRC_FAV_SHOP_VALUE = "fav_shop";
    static final String EP_VALUE = "headline";
    static final String DEVICE_VALUE = "android";
    static final String TEMPLATE_ID_VALUE = "3";

    private FavoriteRepository favoriteRepository;

    public GetTopAdsShopUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                FavoriteRepository favoriteRepository) {

        super(threadExecutor, postExecutionThread);
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Observable<TopAdsShop> createObservable(RequestParams requestParams) {
        boolean isFreshData = isForceRefresh(requestParams);
        TKPDMapParam<String, Object> param = requestParams.getParameters();
        if (isFreshData) {
            return this.favoriteRepository.getFreshTopAdsShop(requestParams.getParameters());
        } else {
            return this.favoriteRepository.getTopAdsShop(param);
        }
    }

    private boolean isForceRefresh(RequestParams requestParams) {
        boolean isForceRefresh = requestParams.getBoolean(KEY_IS_FORCE_REFRESH, false);
        requestParams.clearValue(KEY_IS_FORCE_REFRESH);
        return isForceRefresh;
    }

    public static RequestParams defaultParams() {
        RequestParams params = RequestParams.create();
        params.putString(GetTopAdsShopUseCase.KEY_PAGE,
                GetTopAdsShopUseCase.TOPADS_PAGE_DEFAULT_VALUE);
        params.putString(GetTopAdsShopUseCase.KEY_ITEM,
                GetTopAdsShopUseCase.TOPADS_ITEM_DEFAULT_VALUE);
        params.putString(KEY_SRC,SRC_FAV_SHOP_VALUE);
        params.putString(KEY_EP,EP_VALUE);
        params.putString(KEY_DEVICE,DEVICE_VALUE);
        params.putString(KEY_TEMPLATE_ID,TEMPLATE_ID_VALUE);
        return params;
    }
}
