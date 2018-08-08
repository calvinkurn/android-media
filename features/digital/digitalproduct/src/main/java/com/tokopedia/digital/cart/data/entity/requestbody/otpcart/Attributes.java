package com.tokopedia.digital.cart.data.entity.requestbody.otpcart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

/**
 * @author anggaprasetiyo on 3/13/17.
 */

public class Attributes {

    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    @SerializedName("user_agent")
    @Expose
    private String userAgent;
    @SerializedName("identifier")
    @Expose
    private RequestBodyIdentifier identifier;

    public void setIdentifier(RequestBodyIdentifier identifier) {
        this.identifier = identifier;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
