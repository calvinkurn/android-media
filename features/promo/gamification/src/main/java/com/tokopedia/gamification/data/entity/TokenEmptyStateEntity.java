package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 4/9/18.
 */

public class TokenEmptyStateEntity {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("buttonText")
    @Expose
    private String buttonText;
    @SerializedName("buttonApplink")
    @Expose
    private String buttonApplink;
    @SerializedName("buttonURL")
    @Expose
    private String buttonURL;

    public String getTitle() {
        return title;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getButtonApplink() {
        return buttonApplink;
    }

    public String getButtonURL() {
        return buttonURL;
    }
}
