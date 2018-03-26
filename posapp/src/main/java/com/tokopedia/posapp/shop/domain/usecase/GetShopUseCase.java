package com.tokopedia.posapp.shop.domain.usecase;

import com.tokopedia.posapp.shop.data.repository.ShopCloudRepository;
import com.tokopedia.posapp.shop.data.repository.ShopRepository;
import com.tokopedia.posapp.shop.domain.model.ShopDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/3/17.
 */

public class GetShopUseCase extends UseCase<ShopDomain> {
    private ShopRepository shopRepository;

    @Inject
    public GetShopUseCase(ShopCloudRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Observable<ShopDomain> createObservable(RequestParams requestParams) {
        return shopRepository.getShop(requestParams);
    }
}
