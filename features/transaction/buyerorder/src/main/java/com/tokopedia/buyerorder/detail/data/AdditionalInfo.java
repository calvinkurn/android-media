package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by baghira on 11/05/18.
 */

public class AdditionalInfo {
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("value")
    @Expose
    private String value;

    public String label() {
        return label;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return "[AdditionalInfo:{"
                + "label="+label +" "
                + "value="+value +" "
                + "}]";
    }
}