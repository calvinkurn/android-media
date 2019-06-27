
package com.tokopedia.tokopoints.view.model.section;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cta {

    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("appLink")
    @Expose
    private String appLink;
    @SerializedName("type")
    @Expose
    private String type;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(text);
    }

}
