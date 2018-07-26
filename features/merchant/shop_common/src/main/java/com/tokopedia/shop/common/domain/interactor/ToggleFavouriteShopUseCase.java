package com.tokopedia.shop.common.domain.interactor;

import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

public class ToggleFavouriteShopUseCase extends UseCase<Boolean> {

    private static final String SHOP_ID = "SHOP_ID";

    private ShopCommonRepository shopRepository;

    public ToggleFavouriteShopUseCase(ShopCommonRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        String shopId = requestParams.getString(SHOP_ID, null);
        return shopRepository.toggleFavouriteShop(shopId);
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
