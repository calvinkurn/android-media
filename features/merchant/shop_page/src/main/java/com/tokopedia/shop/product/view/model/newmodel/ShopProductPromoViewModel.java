package com.tokopedia.shop.product.view.model.newmodel;

import com.tokopedia.shop.product.view.adapter.newadapter.ShopProductAdapterTypeFactory;
import com.tokopedia.shop.product.view.adapter.newadapter.viewholder.ShopProductPromoViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductUserVisibleHintListener;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductPromoViewModel implements BaseShopProductViewModel {

    private String url;
    private boolean isLogin;
    private String userId;
    private String accessToken;

    public ShopProductPromoViewModel() {

    }

    public ShopProductPromoViewModel(String url, String userId, String accessToken, boolean isLogin) {
        this.url = url;
        this.userId = userId;
        this.accessToken = accessToken;
        this.isLogin = isLogin;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUrl() {
        return url;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public int type(ShopProductAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
