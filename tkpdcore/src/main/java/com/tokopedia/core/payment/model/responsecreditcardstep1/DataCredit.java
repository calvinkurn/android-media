package com.tokopedia.core.payment.model.responsecreditcardstep1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * DataCredit
 * Created by Angga.Prasetiyo on 13/07/2016.
 */
public class DataCredit {
    @SerializedName("cc_card_bank_type")
    @Expose
    private String ccCardBankType;
    @SerializedName("cc_token_string")
    @Expose
    private String ccTokenString;
    @SerializedName("cc_type")
    @Expose
    private String ccType;
    @SerializedName("fake_cc_agent")
    @Expose
    private Integer fakeCcAgent;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("payment_left")
    @Expose
    private Integer paymentLeft;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("use_one_click")
    @Expose
    private String useOneClick;
    @SerializedName("cc_agent")
    @Expose
    private Integer ccAgent;

    public String getCcCardBankType() {
        return ccCardBankType;
    }

    public void setCcCardBankType(String ccCardBankType) {
        this.ccCardBankType = ccCardBankType;
    }

    public String getCcTokenString() {
        return ccTokenString;
    }

    public void setCcTokenString(String ccTokenString) {
        this.ccTokenString = ccTokenString;
    }

    public String getCcType() {
        return ccType;
    }

    public void setCcType(String ccType) {
        this.ccType = ccType;
    }

    public Integer getFakeCcAgent() {
        return fakeCcAgent;
    }

    public void setFakeCcAgent(Integer fakeCcAgent) {
        this.fakeCcAgent = fakeCcAgent;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getPaymentLeft() {
        return paymentLeft;
    }

    public void setPaymentLeft(Integer paymentLeft) {
        this.paymentLeft = paymentLeft;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getUseOneClick() {
        return useOneClick;
    }

    public void setUseOneClick(String useOneClick) {
        this.useOneClick = useOneClick;
    }

    public Integer getCcAgent() {
        return ccAgent;
    }

    public void setCcAgent(Integer ccAgent) {
        this.ccAgent = ccAgent;
    }
}
