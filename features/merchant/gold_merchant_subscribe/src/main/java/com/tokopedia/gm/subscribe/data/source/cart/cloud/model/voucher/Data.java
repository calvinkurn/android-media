package com.tokopedia.gm.subscribe.data.source.cart.cloud.model.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("promo_code_id")
    @Expose
    private Integer promoCodeId;
    @SerializedName("saldo_amount")
    @Expose
    private Integer saldoAmount;
    @SerializedName("cashback_top_cash_amount")
    @Expose
    private Integer cashbackTopCashAmount;
    @SerializedName("cashback_voucher_amount")
    @Expose
    private Integer cashbackVoucherAmount;
    @SerializedName("extra_amount")
    @Expose
    private Integer extraAmount;
    @SerializedName("cashback_voucher_description")
    @Expose
    private String cashbackVoucherDescription;
    @SerializedName("lp")
    @Expose
    private Object lp;
    @SerializedName("gateway_id")
    @Expose
    private String gatewayId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("message_success")
    @Expose
    private String messageSuccess;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(Integer promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public Integer getSaldoAmount() {
        return saldoAmount;
    }

    public void setSaldoAmount(Integer saldoAmount) {
        this.saldoAmount = saldoAmount;
    }

    public Integer getCashbackTopCashAmount() {
        return cashbackTopCashAmount;
    }

    public void setCashbackTopCashAmount(Integer cashbackTopCashAmount) {
        this.cashbackTopCashAmount = cashbackTopCashAmount;
    }

    public Integer getCashbackVoucherAmount() {
        return cashbackVoucherAmount;
    }

    public void setCashbackVoucherAmount(Integer cashbackVoucherAmount) {
        this.cashbackVoucherAmount = cashbackVoucherAmount;
    }

    public Integer getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(Integer extraAmount) {
        this.extraAmount = extraAmount;
    }

    public String getCashbackVoucherDescription() {
        return cashbackVoucherDescription;
    }

    public void setCashbackVoucherDescription(String cashbackVoucherDescription) {
        this.cashbackVoucherDescription = cashbackVoucherDescription;
    }

    public Object getLp() {
        return lp;
    }

    public void setLp(Object lp) {
        this.lp = lp;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

}
