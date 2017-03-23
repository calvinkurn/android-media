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
 * @author Kulomady on 2/9/17.
 */

@SuppressWarnings("WeakerAccess")
public class GetFavoriteShopUsecase extends UseCase<FavoriteShop> {

    public static final String KEY_OPTION_LOCATION = "option_location";
    public static final String KEY_OPTION_NAME = "option_name";
    public static final String KEY_PAGE = "page";
    public static final String KEY_PER_PAGE = "per_page";

    public static final String KEY_IS_FIRST_PAGE = "isFirstPage";

    public static final String DEFAULT_OPTION_NAME = "";
    public static final String DEFAULT_OPTION_LOCATION = "";
    public static final String DEFAULT_PER_PAGE = "20";
    public static final String INITIAL_VALUE = "1";

    private FavoriteRepository favoriteRepository;

    public GetFavoriteShopUsecase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  FavoriteRepository favoriteRepository) {

        super(threadExecutor, postExecutionThread);
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Observable<FavoriteShop> createObservable(RequestParams requestParams) {
        boolean isFirstPage = requestParams.getBoolean(KEY_IS_FIRST_PAGE, false);
        requestParams.clearValue(KEY_IS_FIRST_PAGE);
        TKPDMapParam<String, String> paramsAllInString = requestParams.getParamsAllValueInString();
        return favoriteRepository.getFavoriteShop(paramsAllInString, isFirstPage);
    }

    public static RequestParams getDefaultParams() {
        RequestParams params = RequestParams.create();
        params.putString(KEY_OPTION_LOCATION, DEFAULT_OPTION_LOCATION);
        params.putString(KEY_OPTION_NAME,DEFAULT_OPTION_NAME);
        params.putString(KEY_PER_PAGE, DEFAULT_PER_PAGE);
        params.putString(KEY_PAGE, INITIAL_VALUE);

        return params;
    }
}
