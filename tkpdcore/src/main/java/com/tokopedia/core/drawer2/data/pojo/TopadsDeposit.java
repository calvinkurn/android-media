
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopadsDeposit {

    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("topads_amount")
    @Expose
    private Integer topadsAmount;
    @SerializedName("is_topads_user")
    @Expose
    private Boolean isTopadsUser;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Integer getTopadsAmount() {
        return topadsAmount;
    }

    public void setTopadsAmount(Integer topadsAmount) {
        this.topadsAmount = topadsAmount;
    }

    public Boolean getIsTopadsUser() {
        return isTopadsUser;
    }

    public void setIsTopadsUser(Boolean isTopadsUser) {
        this.isTopadsUser = isTopadsUser;
    }

}
