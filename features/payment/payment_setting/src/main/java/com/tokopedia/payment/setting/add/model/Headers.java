
package com.tokopedia.payment.setting.add.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Headers {

    @SerializedName("Content-Type")
    @Expose
    private String contentType;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
