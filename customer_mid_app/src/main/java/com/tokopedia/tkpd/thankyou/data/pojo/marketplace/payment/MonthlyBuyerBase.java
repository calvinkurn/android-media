package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonthlyBuyerBase {
    @SerializedName("is_buyer_first_monthly_trx")
    @Expose
    MonthlyNewBuyer monthlyNewBuyer;

    public MonthlyNewBuyer getMonthlyNewBuyer() {
        return monthlyNewBuyer;
    }

    public void setMonthlyNewBuyer(MonthlyNewBuyer monthlyNewBuyer) {
        this.monthlyNewBuyer = monthlyNewBuyer;
    }
}
