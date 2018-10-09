package com.tokopedia.common_digital.cart.data.entity.requestbody.atc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rizky on 27/08/18.
 */
public class Attributes {

    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("device_id")
    @Expose
    private int deviceId;
    @SerializedName("instant_checkout")
    @Expose
    private boolean instantCheckout;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    @SerializedName("user_agent")
    @Expose
    private String userAgent;
    @SerializedName("fields")
    @Expose
    private List<Field> fields = new ArrayList<>();
    @SerializedName("identifier")
    @Expose
    private RequestBodyIdentifier identifier;
    @SerializedName("is_reseller")
    @Expose
    private boolean isReseller;
    @SerializedName("show_subscribe_flag")
    @Expose
    private boolean showSubscribeFlag;
    @SerializedName("is_thankyou_native")
    @Expose
    private boolean isThankyouNative;
    @SerializedName("is_thankyou_native_new")
    @Expose
    private boolean isThankyouNativeNew;

    public void setIdentifier(RequestBodyIdentifier identifier) {
        this.identifier = identifier;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public void setInstantCheckout(boolean instantCheckout) {
        this.instantCheckout = instantCheckout;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void setReseller(boolean reseller) {
        this.isReseller = reseller;
    }

    public void setShowSubscribeFlag(boolean showSubscribeFlag) {
        this.showSubscribeFlag = showSubscribeFlag;
    }

    public void setThankyouNative(boolean thankyouNative) {
        isThankyouNative = thankyouNative;
    }

    public void setThankyouNativeNew(boolean thankyouNativeNew) {
        isThankyouNativeNew = thankyouNativeNew;
    }

}