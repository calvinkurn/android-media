
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentAdminProfile {

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
