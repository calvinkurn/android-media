package com.tokopedia.favorite.domain.interactor;

import com.tokopedia.favorite.domain.FavoriteRepository;
import com.tokopedia.favorite.domain.model.FavShop;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

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

    public AddFavoriteShopUseCase(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Observable<FavShop> createObservable(RequestParams requestParams) {
        return this.favoriteRepository.addFavoriteShop(requestParams.getParamsAllValueInString());
    }
}
