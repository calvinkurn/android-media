
package com.tokopedia.abstraction.base.view.appupdate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataUpdateApp {

    @SerializedName("latest_version_force_update")
    @Expose
    private int latestVersionForceUpdate;
    @SerializedName("latest_version_optional_update")
    @Expose
    private int latestVersionOptionalUpdate;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("is_force_enabled")
    @Expose
    private boolean isForceEnabled;
    @SerializedName("is_optional_enabled")
    @Expose
    private boolean isOptionalEnabled;

    @SerializedName("inapp_update_enabled")
    @Expose
    private boolean inappUpdateEnabled;

    public int getLatestVersionForceUpdate() {
        return latestVersionForceUpdate;
    }

    public void setLatestVersionForceUpdate(int latestVersionForceUpdate) {
        this.latestVersionForceUpdate = latestVersionForceUpdate;
    }

    public int getLatestVersionOptionalUpdate() {
        return latestVersionOptionalUpdate;
    }

    public void setLatestVersionOptionalUpdate(int latestVersionOptionalUpdate) {
        this.latestVersionOptionalUpdate = latestVersionOptionalUpdate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsForceEnabled() {
        return isForceEnabled;
    }

    public void setIsForceEnabled(boolean isForceEnabled) {
        this.isForceEnabled = isForceEnabled;
    }

    public boolean isIsOptionalEnabled() {
        return isOptionalEnabled;
    }

    public void setIsOptionalEnabled(boolean isOptionalEnabled) {
        this.isOptionalEnabled = isOptionalEnabled;
    }

    public boolean isInappUpdateEnabled() {
        return inappUpdateEnabled;
    }
}
