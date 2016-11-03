
package com.tokopedia.tkpd.cart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditCard {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("charge_idr")
    @Expose
    private String chargeIdr;
    @SerializedName("charge_25")
    @Expose
    private String charge25;
    @SerializedName("charge")
    @Expose
    private String charge;
    @SerializedName("total_idr")
    @Expose
    private String totalIdr;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getChargeIdr() {
        return chargeIdr;
    }

    public void setChargeIdr(String chargeIdr) {
        this.chargeIdr = chargeIdr;
    }

    public String getCharge25() {
        return charge25;
    }

    public void setCharge25(String charge25) {
        this.charge25 = charge25;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getTotalIdr() {
        return totalIdr;
    }

    public void setTotalIdr(String totalIdr) {
        this.totalIdr = totalIdr;
    }
}
