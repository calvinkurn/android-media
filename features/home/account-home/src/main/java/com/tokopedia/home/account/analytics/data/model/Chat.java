
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @SerializedName("unreads")
    @Expose
    private Integer unreads = 0;

    public Integer getUnreads() {
        return unreads;
    }

    public void setUnreads(Integer unreads) {
        this.unreads = unreads;
    }

}
