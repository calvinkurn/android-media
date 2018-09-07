package com.tokopedia.otp.cotp.view.viewmodel;

/**
 * @author by nisie on 5/26/17.
 */

public class OtpLoginDomain {

    private final boolean shopIsGold;
    private final boolean isMsisdnVerified;
    private final int shopId;
    private final String shopName;
    private final String fullName;
    private final boolean isLogin;
    private final int userId;

    public OtpLoginDomain(boolean shopIsGold,
                          boolean isMsisdnVerified,
                          int shopId, String shopName,
                          String fullName,
                          boolean isLogin,
                          int userId) {
        this.shopIsGold = shopIsGold;
        this.isMsisdnVerified = isMsisdnVerified;
        this.shopId = shopId;
        this.shopName = shopName;
        this.fullName = fullName;
        this.isLogin = isLogin;
        this.userId = userId;
    }

    public boolean getShopIsGold() {
        return shopIsGold;
    }

    public int getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getFullName() {
        return fullName;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isMsisdnVerified() {
        return isMsisdnVerified;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
