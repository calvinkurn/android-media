package com.tokopedia.reksadana.view.data.signimageurl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.reksadana.view.data.common.Result;

public class GetSignUrl {
    @Expose
    @SerializedName("result")
    private Result result;
    @Expose
    @SerializedName("signedURL")
    private String signedURL;
    @Expose
    @SerializedName("fileName")
    private String fileName;
    @Expose
    @SerializedName("publicURL")
    private String publicURL;
    @Expose
    @SerializedName("originalFileName")
    private String originalFileName;
    @Expose
    @SerializedName("contentType")
    private String contentType;
    @Expose
    @SerializedName("xAmzAcl")
    private String xAmzAcl;

    public GetSignUrl(Result result, String signedURL, String fileName, String publicURL, String originalFileName, String contentType, String xAmzAcl) {
        this.result = result;
        this.signedURL = signedURL;
        this.fileName = fileName;
        this.publicURL = publicURL;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.xAmzAcl = xAmzAcl;
    }

    public Result result() {
        return result;
    }

    public String signedURL() {
        return signedURL;
    }

    public String fileName() {
        return fileName;
    }

    public String publicURL() {
        return publicURL;
    }

    public String originalFileName() {
        return originalFileName;
    }

    public String contentType() {
        return contentType;
    }

    public String xAmzAcl() {
        return xAmzAcl;
    }
}
