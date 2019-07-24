package com.tokopedia.shop.product.view.model;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductPromoViewModel {

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

}
