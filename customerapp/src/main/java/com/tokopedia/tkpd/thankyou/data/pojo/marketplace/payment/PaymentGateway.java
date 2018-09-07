package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 12/7/17.
 */

public class PaymentGateway {
    @SerializedName("gateway_id")
    @Expose
    private int gatewayId;
    @SerializedName("gateway_img_url")
    @Expose
    private String gatewayImgUrl;
    @SerializedName("gateway_name")
    @Expose
    private String gatewayName;

    public int getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(int gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getGatewayImgUrl() {
        return gatewayImgUrl;
    }

    public void setGatewayImgUrl(String gatewayImgUrl) {
        this.gatewayImgUrl = gatewayImgUrl;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }
}
