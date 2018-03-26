package com.tokopedia.posapp.shop.view.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.posapp.shop.domain.usecase.GetShopUseCase;
import com.tokopedia.posapp.shop.view.Shop;
import com.tokopedia.posapp.shop.view.GetShopSubscriber;
import com.tokopedia.usecase.RequestParams;

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
    private UserSession userSession;

    @Inject
    public ShopPresenter(GetShopUseCase shopUseCase,
                         UserSession userSession) {
        this.shopUseCase = shopUseCase;
        this.userSession = userSession;
    }

    @Override
    public void attachView(Shop.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }


    @Override
    public void getUserShop() {
        if(TextUtils.isEmpty(userSession.getShopId())) {
            RequestParams params = RequestParams.create();
            params.putString(SHOP_ID, userSession.getShopId());
//            params.putString(SHOP_DOMAIN, SessionHandler.getShopDomain(context));
            params.putString(SHOW_ALL, "1");

            shopUseCase.execute(params, new GetShopSubscriber(view));
        }
    }
}
