package com.tokopedia.digital.cart.data.entity.requestbody.atc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 3/3/17.
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
    @SerializedName("isReseller")
    @Expose
    private boolean isReseller;

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
}
