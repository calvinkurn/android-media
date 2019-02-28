package com.tokopedia.posapp.shop.view.presenter;

import com.tokopedia.posapp.shop.domain.usecase.GetShopUseCase;
import com.tokopedia.posapp.shop.view.GetShopSubscriber;
import com.tokopedia.posapp.shop.view.Shop;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopPresenter implements Shop.Presenter {

    private static final String SHOP_ID = "shop_id";
    private static final String SHOP_DOMAIN = "shop_domain";
    private static final String SHOW_ALL = "show_all";

    private Shop.View view;
    private GetShopUseCase shopUseCase;

    @Inject
    public ShopPresenter(GetShopUseCase shopUseCase) {
        this.shopUseCase = shopUseCase;
    }

    @Override
    public void attachView(Shop.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        shopUseCase.unsubscribe();
    }

    @Override
    public void getUserShop() {
        shopUseCase.execute(new GetShopSubscriber(view));
    }
}
