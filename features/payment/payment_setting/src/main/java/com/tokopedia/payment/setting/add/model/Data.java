
package com.tokopedia.payment.setting.add.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("cc_iframe")
    @Expose
    private CcIframe ccIframe;
    @SerializedName("cc_iframe_encode")
    @Expose
    private String ccIframeEncode;
    @SerializedName("api_info")
    @Expose
    private ApiInfo apiInfo;

    public CcIframe getCcIframe() {
        return ccIframe;
    }

    public void setCcIframe(CcIframe ccIframe) {
        this.ccIframe = ccIframe;
    }

    public String getCcIframeEncode() {
        return ccIframeEncode;
    }

    public void setCcIframeEncode(String ccIframeEncode) {
        this.ccIframeEncode = ccIframeEncode;
    }

    public ApiInfo getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
    }

}
