
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @SerializedName("unreads")
    @Expose
    private Integer unreads;

    public Integer getUnreads() {
        return unreads;
    }

    public void setUnreads(Integer unreads) {
        this.unreads = unreads;
    }

}
