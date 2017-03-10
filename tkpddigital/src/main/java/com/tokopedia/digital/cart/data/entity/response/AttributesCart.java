package com.tokopedia.digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class AttributesCart {

    @SerializedName("client_number")
    @Expose
    private String clientNumber;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("operator_name")
    @Expose
    private String operatorName;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("price_plain")
    @Expose
    private long pricePlain;
    @SerializedName("instant_checkout")
    @Expose
    private boolean instantCheckout;
    @SerializedName("need_otp")
    @Expose
    private boolean needOtp;
    @SerializedName("sms_state")
    @Expose
    private String smsState;
    @SerializedName("user_input_price")
    @Expose
    private UserInputPrice userInputPrice;
    @SerializedName("main_info")
    @Expose
    private List<MainInfo> mainInfo = null;
    @SerializedName("additional_info")
    @Expose
    private List<AdditionalInfo> additionalInfo = null;

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getPricePlain() {
        return pricePlain;
    }

    public void setPricePlain(long pricePlain) {
        this.pricePlain = pricePlain;
    }

    public boolean isInstantCheckout() {
        return instantCheckout;
    }

    public void setInstantCheckout(boolean instantCheckout) {
        this.instantCheckout = instantCheckout;
    }

    public String getSmsState() {
        return smsState;
    }

    public void setSmsState(String smsState) {
        this.smsState = smsState;
    }

    public UserInputPrice getUserInputPrice() {
        return userInputPrice;
    }

    public void setUserInputPrice(UserInputPrice userInputPrice) {
        this.userInputPrice = userInputPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isNeedOtp() {
        return needOtp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MainInfo> getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(List<MainInfo> mainInfo) {
        this.mainInfo = mainInfo;
    }

    public List<AdditionalInfo> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(List<AdditionalInfo> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
