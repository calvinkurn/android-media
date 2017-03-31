package com.tokopedia.digital.cart.data.entity.requestbody.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class Attributes {

    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;
    @SerializedName("transaction_amount")
    @Expose
    private Long transactionAmount;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    @SerializedName("user_agent")
    @Expose
    private String userAgent;
//    @SerializedName("access_token")
//    @Expose
//    private String accessToken;
//    @SerializedName("wallet_refresh_token")
//    @Expose
//    private String walletRefreshToken;

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public void setTransactionAmount(Long transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
//
//    public void setWalletRefreshToken(String walletRefreshToken) {
//        this.walletRefreshToken = walletRefreshToken;
//    }
}
