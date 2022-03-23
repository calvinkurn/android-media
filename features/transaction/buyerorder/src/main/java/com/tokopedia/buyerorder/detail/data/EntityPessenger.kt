package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.SerializedName;

public class EntityPessenger {
    @SerializedName("title")
    private String title;

    @SerializedName("value")
    private String value;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
