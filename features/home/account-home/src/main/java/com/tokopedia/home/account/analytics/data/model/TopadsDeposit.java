
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopadsDeposit {

    @SerializedName("topads_amount")
    @Expose
    private Integer topadsAmount;
    @SerializedName("is_topads_user")
    @Expose
    private Boolean isTopadsUser;

    public Integer getTopadsAmount() {
        return topadsAmount;
    }

    public Boolean getIsTopadsUser() {
        return isTopadsUser;
    }

}
