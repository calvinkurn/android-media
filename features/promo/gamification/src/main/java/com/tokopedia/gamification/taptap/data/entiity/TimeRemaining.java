
package com.tokopedia.gamification.taptap.data.entiity;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class TimeRemaining {

    @SerializedName("backgroundColor")
    private String mBackgroundColor;
    @SerializedName("fontColor")
    private String mFontColor;
    @SerializedName("borderColor")
    private String mBorderColor;
    @SerializedName("isShow")
    private boolean mIsShow;
    @SerializedName("seconds")
    private long mSeconds;
    @SerializedName("unixFetch")
    private long mUnixFetch;

    public String getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public String getFontColor() {
        return mFontColor;
    }

    public void setFontColor(String fontColor) {
        mFontColor = fontColor;
    }

    public String getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(String mBorderColor) {
        this.mBorderColor = mBorderColor;
    }

    public boolean getIsShow() {
        return mIsShow;
    }

    public void setIsShow(boolean isShow) {
        mIsShow = isShow;
    }

    public long getSeconds() {
        return mSeconds;
    }

    public void setSeconds(long seconds) {
        mSeconds = seconds;
    }

    public long getUnixFetch() {
        return mUnixFetch;
    }

    public void setUnixFetch(long unixFetch) {
        mUnixFetch = unixFetch;
    }

}
