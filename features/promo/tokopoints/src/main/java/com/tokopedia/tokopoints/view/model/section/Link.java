
package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Link {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("applink")
    @Expose
    private String applink;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

}
