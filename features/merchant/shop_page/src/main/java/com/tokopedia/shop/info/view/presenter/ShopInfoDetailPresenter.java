package com.tokopedia.shop.info.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.info.view.listener.ShopInfoDetailView;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopInfoDetailPresenter extends BaseDaggerPresenter<ShopInfoDetailView> {

    private final UserSessionInterface userSession;

    @Inject
    public ShopInfoDetailPresenter(UserSessionInterface userSession) {
        this.userSession = userSession;
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }
}