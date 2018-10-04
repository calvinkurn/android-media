package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.feedplus.data.repository.FavoriteShopRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by stevenfredian on 5/26/17.
 */

public class FavoriteShopUseCase extends UseCase<Boolean> {

    public static String PARAM_SHOP_ID= "shop_id";
    public static String PARAM_SHOP_DOMAIN= "shop_domain";
    public static String PARAM_SRC= "src";
    public static String PARAM_AD_KEY= "ad_key";
    public static String DEFAULT_VALUE_SRC= "fav_shop";

    public static final String PARAM_SHOP_NAME = "shop_name";
    public static final String PARAM_ACTION = "action";

    private FavoriteShopRepository repository;

    @Inject
    public FavoriteShopUseCase(FavoriteShopRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return repository.doFavoriteShop(requestParams);
    }
}
