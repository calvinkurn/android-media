package com.tokopedia.digital.cart.model;

import java.util.List;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class CartDigitalInfoData {

    private String type;

    private String id;

    private AttributesDigital attributes;

    private String title;

    private boolean instantCheckout;

    private boolean isOtpValid;

    private String smsState;

    private List<CartItemDigital> mainInfo;

    private List<CartAdditionalInfo> additionalInfos;

    private Relationships relationships;

    private String operatorName;

    private String categoryName;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttributesDigital getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesDigital attributes) {
        this.attributes = attributes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isInstantCheckout() {
        return instantCheckout;
    }

    public void setInstantCheckout(boolean instantCheckout) {
        this.instantCheckout = instantCheckout;
    }

    public boolean isOtpValid() {
        return isOtpValid;
    }

    public void setOtpValid(boolean otpValid) {
        isOtpValid = otpValid;
    }

    public String getSmsState() {
        return smsState;
    }

    public void setSmsState(String smsState) {
        this.smsState = smsState;
    }

    public List<CartItemDigital> getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(List<CartItemDigital> mainInfo) {
        this.mainInfo = mainInfo;
    }

    public List<CartAdditionalInfo> getAdditionalInfos() {
        return additionalInfos;
    }

    public void setAdditionalInfos(List<CartAdditionalInfo> additionalInfos) {
        this.additionalInfos = additionalInfos;
    }

    public Relationships getRelationships() {
        return relationships;
    }

    public void setRelationships(Relationships relationships) {
        this.relationships = relationships;
    }
}
