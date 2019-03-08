package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoalQRInquiry {

@SerializedName("redirect_url")
@Expose
private String redirectUrl;
@SerializedName("amount")
@Expose
private Float amount;
@SerializedName("fee")
@Expose
private Float fee;
@SerializedName("transfer_id")
@Expose
private Integer transferId;
@SerializedName("is_cash_enabled")
@Expose
private Boolean isCashEnabled;
@SerializedName("is_point_enabled")
@Expose
private Boolean isPointEnabled;
@SerializedName("is_input_fee_enabled")
@Expose
private Boolean isInputFeeEnabled;
@SerializedName("show_use_point_toggle")
@Expose
private Boolean showUsePointToggle;
@SerializedName("merchant")
@Expose
private Merchant merchant;
@SerializedName("promo")
@Expose
private Promo promo;
@SerializedName("errors")
@Expose
private List<Errors> errors;

    public GoalQRInquiry(String redirectUrl, Float amount, Float fee, Integer transferId, Boolean isCashEnabled, Boolean isPointEnabled, Boolean isInputFeeEnabled, Boolean showUsePointToggle, Merchant merchant, Promo promo, List<Errors> errors) {
        this.redirectUrl = redirectUrl;
        this.amount = amount;
        this.fee = fee;
        this.transferId = transferId;
        this.isCashEnabled = isCashEnabled;
        this.isPointEnabled = isPointEnabled;
        this.isInputFeeEnabled = isInputFeeEnabled;
        this.showUsePointToggle = showUsePointToggle;
        this.merchant = merchant;
        this.promo = promo;
        this.errors = errors;
    }

    public String getRedirectUrl() {
return redirectUrl;
}

public void setRedirectUrl(String redirectUrl) {
this.redirectUrl = redirectUrl;
}

public Float getAmount() {
return amount;
}

public void setAmount(Float amount) {
this.amount = amount;
}

public Float getFee() {
return fee;
}

public void setFee(Float fee) {
this.fee = fee;
}

public Integer getTransferId() {
return transferId;
}

public void setTransferId(Integer transferId) {
this.transferId = transferId;
}

public Boolean getIsCashEnabled() {
return isCashEnabled;
}

public void setIsCashEnabled(Boolean isCashEnabled) {
this.isCashEnabled = isCashEnabled;
}

public Boolean getIsPointEnabled() {
return isPointEnabled;
}

public void setIsPointEnabled(Boolean isPointEnabled) {
this.isPointEnabled = isPointEnabled;
}

public Boolean getIsInputFeeEnabled() {
return isInputFeeEnabled;
}

public void setIsInputFeeEnabled(Boolean isInputFeeEnabled) {
this.isInputFeeEnabled = isInputFeeEnabled;
}

public Boolean getShowUsePointToggle() {
return showUsePointToggle;
}

public void setShowUsePointToggle(Boolean showUsePointToggle) {
this.showUsePointToggle = showUsePointToggle;
}

public Merchant getMerchant() {
return merchant;
}

public void setMerchant(Merchant merchant) {
this.merchant = merchant;
}

public Promo getPromo() {
return promo;
}

public void setPromo(Promo promo) {
this.promo = promo;
}

public List<Errors> getErrors() {
return errors;
}

public void setErrors(List<Errors> errors) {
this.errors = errors;
}

}