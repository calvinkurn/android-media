
package com.tokopedia.tkpd.drawer.model.LoyaltyItem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoyaltyPoint {

    @SerializedName("has_lp")
    @Expose
    private int hasLp;
    @SerializedName("amount")
    @Expose
    private String amount;

    /**
     * 
     * @return
     *     The hasLp
     */
    public int getHasLp() {
        return hasLp;
    }

    /**
     * 
     * @param hasLp
     *     The has_lp
     */
    public void setHasLp(int hasLp) {
        this.hasLp = hasLp;
    }

    /**
     * 
     * @return
     *     The amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * 
     * @param amount
     *     The amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

}
