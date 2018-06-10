
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentAdminProfile {

    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("is_purchased_marketplace")
    @Expose
    private Boolean isPurchasedMarketplace;
    @SerializedName("is_purchased_digital")
    @Expose
    private Boolean isPurchasedDigital;
    @SerializedName("is_purchased_ticket")
    @Expose
    private Boolean isPurchasedTicket;
    @SerializedName("last_purchase_date")
    @Expose
    private String lastPurchaseDate;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Boolean getIsPurchasedMarketplace() {
        return isPurchasedMarketplace;
    }

    public void setIsPurchasedMarketplace(Boolean isPurchasedMarketplace) {
        this.isPurchasedMarketplace = isPurchasedMarketplace;
    }

    public Boolean getIsPurchasedDigital() {
        return isPurchasedDigital;
    }

    public void setIsPurchasedDigital(Boolean isPurchasedDigital) {
        this.isPurchasedDigital = isPurchasedDigital;
    }

    public Boolean getIsPurchasedTicket() {
        return isPurchasedTicket;
    }

    public void setIsPurchasedTicket(Boolean isPurchasedTicket) {
        this.isPurchasedTicket = isPurchasedTicket;
    }

    public String getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setLastPurchaseDate(String lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }

}
