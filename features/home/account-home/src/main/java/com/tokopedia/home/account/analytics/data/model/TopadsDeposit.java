
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopadsDeposit {

    @SerializedName("topads_amount")
    @Expose
    private Integer topadsAmount = 0;
    @SerializedName("is_topads_user")
    @Expose
    private Boolean isTopadsUser = false;

    public Integer getTopadsAmount() {
        return topadsAmount;
    }

    public Boolean getIsTopadsUser() {
        return isTopadsUser;
    }

}
