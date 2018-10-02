package com.tokopedia.affiliate.feature.createpost.data.pojo.uploadimage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadImageResponse {
    @SerializedName("data")
    @Expose
    private UploadImageData data;

    public UploadImageData getData() {
        return data;
    }

    public void setData(UploadImageData data) {
        this.data = data;
    }
}