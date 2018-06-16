package com.tokopedia.reksadana.view.data.initdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DropdownValues {
    @Expose
    @SerializedName("key")
    private String key;
    @Expose
    @SerializedName("value")
    private String value;

    public DropdownValues(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String key() {
        return key;
    }

    public String value() {
        return value;
    }
}
