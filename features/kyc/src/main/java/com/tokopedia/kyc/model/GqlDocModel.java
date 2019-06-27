package com.tokopedia.kyc.model;

import com.google.gson.annotations.SerializedName;


public class GqlDocModel {
    @SerializedName("document_id")
    private int key;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
