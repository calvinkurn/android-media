
package com.tokopedia.gamification.taptap.data.entiity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ActionButton implements Parcelable {

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

    protected ActionButton(Parcel in) {
        mApplink = in.readString();
        mBackgroundColor = in.readString();
        mIsDisable = in.readByte() != 0;
        mText = in.readString();
        mType = in.readString();
        mUrl = in.readString();
    }

    public static final Creator<ActionButton> CREATOR = new Creator<ActionButton>() {
        @Override
        public ActionButton createFromParcel(Parcel in) {
            return new ActionButton(in);
        }

        @Override
        public ActionButton[] newArray(int size) {
            return new ActionButton[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mApplink);
        dest.writeString(mBackgroundColor);
        dest.writeByte((byte) (mIsDisable ? 1 : 0));
        dest.writeString(mText);
        dest.writeString(mType);
        dest.writeString(mUrl);
    }
}
