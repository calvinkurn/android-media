package com.tokopedia.shop.info.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.info.view.listener.ShopInfoDetailView;

import javax.inject.Inject;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopInfoDetailPresenter extends BaseDaggerPresenter<ShopInfoDetailView> {

    private final UserSession userSession;

    @Inject
    public ShopInfoDetailPresenter(UserSession userSession) {
        this.userSession = userSession;
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }
}