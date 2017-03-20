package com.tokopedia.tkpd.home.favorite.domain.interactor;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;

import rx.Observable;

/**
 * @author Kulomady on 1/19/17.
 */

public class GetFavoriteShopUsecase extends UseCase<FavoriteShop> {

    public static final String KEY_OPTION_LOCATION = "option_location";
    public static final String KEY_OPTION_NAME = "option_name";
    public static final String KEY_PAGE = "page";
    public static final String KEY_PER_PAGE = "per_page";

    public static final String DEFAULT_OPTION_NAME = "";
    public static final String DEFAULT_OPTION_LOCATION = "";
    public static final String DEFAULT_PER_PAGE = "20";
    public static final String INITIAL_VALUE = "1";


    private final FavoriteRepository mFavoriteRepository;

    public GetFavoriteShopUsecase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  FavoriteRepository favoriteRepository) {
        super(threadExecutor, postExecutionThread);
        mFavoriteRepository = favoriteRepository;
    }


    @Override
    public Observable<FavoriteShop> createObservable(RequestParams requestParams) {
        return mFavoriteRepository.getFavoriteShop(getParamsInstring(requestParams));
    }

    public static RequestParams getDefaultParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KEY_OPTION_LOCATION, DEFAULT_OPTION_LOCATION);
        requestParams.putString(KEY_OPTION_NAME, DEFAULT_OPTION_NAME);
        requestParams.putString(KEY_PER_PAGE, DEFAULT_PER_PAGE);
        requestParams.putString(KEY_PAGE, INITIAL_VALUE);

        return requestParams;
    }
    private TKPDMapParam<String, String> getParamsInstring(RequestParams requestParams) {

        TKPDMapParam<String, String> results = new TKPDMapParam<>();
        results.put(KEY_OPTION_NAME, requestParams.getString(KEY_OPTION_NAME, ""));
        results.put(KEY_OPTION_LOCATION, requestParams.getString(KEY_OPTION_LOCATION, ""));
        results.put(KEY_PER_PAGE, requestParams.getString(KEY_PER_PAGE, ""));
        results.put(KEY_PAGE, requestParams.getString(KEY_PAGE, ""));

        return results;
    }

}

