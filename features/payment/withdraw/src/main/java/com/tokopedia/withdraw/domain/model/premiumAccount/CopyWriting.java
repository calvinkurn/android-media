
package com.tokopedia.withdraw.domain.model.premiumAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CopyWriting {
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("subtitle")
    @Expose
    String subtitle;
    @SerializedName("cta")
    @Expose
    String cta;
    @SerializedName("url")
    @Expose
    String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCta() {
        return cta;
    }

    public void setCta(String cta) {
        this.cta = cta;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
