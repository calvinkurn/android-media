
package com.tokopedia.core.drawer2.data.pojo.topcash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author kulomady on 11/08/16
 */
public class Action implements Parcelable {

    @SerializedName("redirect_url")
    private String mRedirectUrl;
    @SerializedName("text")
    private String mText;
    @SerializedName("applinks")
    private String mAppLinks;
    @SerializedName("visibility")
    private String mVisibility;

    public String getRedirectUrl() {
        if (mRedirectUrl == null) return "";
        else return mRedirectUrl;
    }

    public void setRedirectUrl(String redirect_url) {
        mRedirectUrl = redirect_url;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }


    public String getmRedirectUrl() {
        return mRedirectUrl;
    }

    public void setmRedirectUrl(String mRedirectUrl) {
        this.mRedirectUrl = mRedirectUrl;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmAppLinks() {
        return mAppLinks;
    }

    public void setmAppLinks(String mAppLinks) {
        this.mAppLinks = mAppLinks;
    }

    public String getmVisibility() {
        return mVisibility;
    }

    public void setmVisibility(String mVisibility) {
        this.mVisibility = mVisibility;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mRedirectUrl);
        dest.writeString(this.mText);
        dest.writeString(this.mAppLinks);
        dest.writeString(this.mVisibility);
    }

    public Action() {
    }

    protected Action(Parcel in) {
        this.mRedirectUrl = in.readString();
        this.mText = in.readString();
        this.mAppLinks = in.readString();
        this.mVisibility = in.readString();
    }

    public static final Creator<Action> CREATOR = new Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel source) {
            return new Action(source);
        }

        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };
}
