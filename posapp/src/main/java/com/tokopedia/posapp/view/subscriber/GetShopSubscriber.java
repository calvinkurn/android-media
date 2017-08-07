package com.tokopedia.posapp.view.subscriber;

import com.tokopedia.posapp.domain.model.shop.ShopDomain;
import com.tokopedia.posapp.view.Shop;
import com.tokopedia.posapp.view.viewmodel.shop.ShopInfoViewModel;
import com.tokopedia.posapp.view.viewmodel.shop.ShopViewModel;

import rx.Subscriber;

/**
 * Created by okasurya on 8/3/17.
 */

public class GetShopSubscriber extends Subscriber<ShopDomain> {
    private Shop.View viewListener;

    public GetShopSubscriber(Shop.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(ShopDomain shopDomain) {
        ShopViewModel shopViewModel = getShopViewModel(shopDomain);
        viewListener.onSuccessGetShop(shopViewModel);
    }

    private ShopViewModel getShopViewModel(ShopDomain shopDomain) {
        ShopViewModel shopViewModel = new ShopViewModel();
        ShopInfoViewModel shopInfoViewModel = new ShopInfoViewModel();
        shopInfoViewModel.setShopName(shopDomain.getShopInfo().getShopName());
        shopViewModel.setShopInfo(shopInfoViewModel);
        return shopViewModel;
    }
}
