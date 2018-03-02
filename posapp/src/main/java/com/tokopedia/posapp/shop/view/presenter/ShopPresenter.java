package com.tokopedia.posapp.shop.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.shop.domain.usecase.GetShopUseCase;
import com.tokopedia.posapp.shop.view.Shop;
import com.tokopedia.posapp.shop.view.GetShopSubscriber;

import javax.inject.Inject;

/**
 * Created by okasurya on 8/3/17.
 */

public class ShopPresenter extends BaseDaggerPresenter<Shop.View>
    implements Shop.Presenter {

    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_DOMAIN = "shop_domain";
    public static final String SHOW_ALL = "show_all";

    private Context context;
    private GetShopUseCase shopUseCase;

    @Inject
    public ShopPresenter(@ApplicationContext Context context, GetShopUseCase outletUseCase) {
        this.context = context;
        this.shopUseCase = outletUseCase;
    }

    @Override
    public void attachView(Shop.View view) {
        super.attachView(view);
    }


    @Override
    public void getUserShop() {
        RequestParams params = AuthUtil.generateRequestParamsNetwork(context);
        if(SessionHandler.getShopID(context) != null
                && !SessionHandler.getShopID(context).isEmpty()) {
            params.putString(SHOP_ID, SessionHandler.getShopID(context));
            params.putString(SHOP_DOMAIN, SessionHandler.getShopDomain(context));
            params.putString(SHOW_ALL, "1");

            shopUseCase.execute(params, new GetShopSubscriber(getView()));
        }
    }
}
