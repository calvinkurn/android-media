package com.tokopedia.core.addtocart.model.responseatcform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AtcFormData {

    @SerializedName("auto_resi")
    @Expose
    private List<Object> autoResi = new ArrayList<Object>();
    @SerializedName("insurance_info_json")
    @Expose
    private String insuranceInfoJson;
    @SerializedName("rpx")
    @Expose
    private Rpx rpx;
    @SerializedName("form")
    @Expose
    private Form form;
    @SerializedName("shop")
    @Expose
    private Shop shop;

    /**
     * @return The autoResi
     */
    public List<Object> getAutoResi() {
        return autoResi;
    }

    /**
     * @param autoResi The auto_resi
     */
    public void setAutoResi(List<Object> autoResi) {
        this.autoResi = autoResi;
    }

    /**
     * @return The insuranceInfoJson
     */
    public String getInsuranceInfoJson() {
        return insuranceInfoJson;
    }

    /**
     * @param insuranceInfoJson The insurance_info_json
     */
    public void setInsuranceInfoJson(String insuranceInfoJson) {
        this.insuranceInfoJson = insuranceInfoJson;
    }

    /**
     * @return The rpx
     */
    public Rpx getRpx() {
        return rpx;
    }

    /**
     * @param rpx The rpx
     */
    public void setRpx(Rpx rpx) {
        this.rpx = rpx;
    }

    /**
     * @return The form
     */
    public Form getForm() {
        return form;
    }

    /**
     * @param form The form
     */
    public void setForm(Form form) {
        this.form = form;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
