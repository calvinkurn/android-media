package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrossSellingWording {
    @SerializedName("header_title")
    @Expose
    private String headerTitle;
    @SerializedName("body_title")
    @Expose
    private String bodyTitle;
    @SerializedName("body_content_before")
    @Expose
    private String bodyContentBefore;
    @SerializedName("body_content_after")
    @Expose
    private String bodyContentAfter;
    @SerializedName("cta_button_text")
    @Expose
    private String checkoutButtonText;

    public String getHeaderTitle() {
        return headerTitle;
    }

    public String getBodyTitle() {
        return bodyTitle;
    }

    public String getBodyContentBefore() {
        return bodyContentBefore;
    }

    public String getBodyContentAfter() {
        return bodyContentAfter;
    }

    public String getCheckoutButtonText() {
        return checkoutButtonText;
    }
}
