package com.tokopedia.digital.product.data.entity.requestbody.pulsabalance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

/**
 * Created by ashwanityagi on 18/07/17.
 */

public class Attributes {
    @SerializedName("operator_id")
    @Expose
    private int operatorId;
    @SerializedName("client_number")
    @Expose
    private String clientNumber;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_agent")
    @Expose
    private String userAgent;
    @SerializedName("identifier")
    @Expose
    private RequestBodyIdentifier identifier;
    public RequestBodyIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(RequestBodyIdentifier identifier) {
        this.identifier = identifier;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
