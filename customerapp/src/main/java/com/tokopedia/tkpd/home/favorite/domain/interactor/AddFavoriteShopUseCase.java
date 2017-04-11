package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;

import rx.Observable;

/**
 * @author Kulomady on 2/9/17.
 */

public class AddFavoriteShopUseCase extends UseCase<FavShop> {

    public static final String KEY_AD = "ad_key";
    public static final String KEY_SHOP_ID = "shop_id";
    public static final String KEY_SRC = "src";
    public static final String DEFAULT_VALUE_SRC = "fav_shop";

    private FavoriteRepository favoriteRepository;

    public AddFavoriteShopUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  FavoriteRepository favoriteRepository) {
        super(threadExecutor, postExecutionThread);
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Observable<FavShop> createObservable(RequestParams requestParams) {
        return this.favoriteRepository.addFavoriteShop(requestParams.getParamsAllValueInString());
    }
}
