
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentAdminProfile {

    @SerializedName("is_purchased_marketplace")
    @Expose
    private Boolean isPurchasedMarketplace = false;
    @SerializedName("is_purchased_digital")
    @Expose
    private Boolean isPurchasedDigital = false;
    @SerializedName("is_purchased_ticket")
    @Expose
    private Boolean isPurchasedTicket = false;
    @SerializedName("last_purchase_date")
    @Expose
    private String lastPurchaseDate = "";

    public Boolean getIsPurchasedMarketplace() {
        return isPurchasedMarketplace;
    }

    public Boolean getIsPurchasedDigital() {
        return isPurchasedDigital;
    }

    public Boolean getIsPurchasedTicket() {
        return isPurchasedTicket;
    }

    public String getLastPurchaseDate() {
        return lastPurchaseDate;
    }
}
