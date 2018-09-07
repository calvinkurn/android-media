package com.tokopedia.reksadana.view.data.signimageurl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageDetails {
    @Expose
    @SerializedName("objectName")
    private String objectName;
    @Expose
    @SerializedName("uploadType")
    private int uploadType;
    @Expose
    @SerializedName("contentType")
    private String contentType;

    public ImageDetails(String objectName, int uploadType, String contentType) {
        this.objectName = objectName;
        this.uploadType = uploadType;
        this.contentType = contentType;
    }

    public String objectName() {
        return objectName;
    }

    public int uploadType() {
        return uploadType;
    }

    public String contentType() {
        return contentType;
    }
}
