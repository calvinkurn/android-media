package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Kulomady on 2/9/17.
 */

@SuppressWarnings("WeakerAccess")
public class GetWishlistUtil {

    public static final String KEY_COUNT = "count";
    public static final String KEY_PAGE = "page";
    public static final String KEY_IS_FORCE_REFRESH = "isForceUpdate";

    public static final int DEFAULT_PAGE_VALUE = 1;
    public static final int DEFAULT_COUNT_VALUE = 4;

    private final FavoriteRepository favoriteRepository;

    @Inject
    public GetWishlistUtil(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public Observable<DomainWishlist> getWishListData(RequestParams requestParams) {
        boolean isForceRefresh = requestParams.getBoolean(KEY_IS_FORCE_REFRESH, false);
        requestParams.clearValue(KEY_IS_FORCE_REFRESH);
        TKPDMapParam<String, Object> parameters = requestParams.getParameters();


        if (isForceRefresh) {
            return favoriteRepository.getFreshWishlist(parameters);
        } else {
            return favoriteRepository.getWishlist(parameters);
        }
    }

    public static RequestParams getDefaultParams() {
        RequestParams param = RequestParams.create();
        param.putInt(KEY_PAGE, DEFAULT_PAGE_VALUE);
        param.putInt(KEY_COUNT, DEFAULT_COUNT_VALUE);
        return param;
    }
}
