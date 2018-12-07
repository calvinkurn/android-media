package com.tokopedia.digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionConfirmPostPaidPopup {
    @SerializedName("title")
    @Expose
    private String title;

    public String getTitle() {
        return title;
    }
}
