
package com.tokopedia.gamification.taptap.data.entiity;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class RewardButton {

    @SerializedName("applink")
    private String mApplink;
    @SerializedName("backgroundColor")
    private String mBackgroundColor;
    @SerializedName("isDisable")
    private boolean mIsDisable;
    @SerializedName("text")
    private String mText;
    @SerializedName("type")
    private String mType;
    @SerializedName("url")
    private String mUrl;

    public String getApplink() {
        return mApplink;
    }

    public void setApplink(String applink) {
        mApplink = applink;
    }

    public String getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public boolean getIsDisable() {
        return mIsDisable;
    }

    public void setIsDisable(boolean isDisable) {
        mIsDisable = isDisable;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
