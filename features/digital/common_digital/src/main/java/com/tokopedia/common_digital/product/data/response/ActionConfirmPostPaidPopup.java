package com.tokopedia.common_digital.product.data.response;

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
