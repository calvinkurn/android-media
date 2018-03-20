
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

    public Action() {
    }

    protected Action(Parcel in) {
        mRedirectUrl = in.readString();
        mText = in.readString();
        mAppLinks = in.readString();
        mVisibility = in.readString();
    }

    public static final Creator<Action> CREATOR = new Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }

        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };

    public String getRedirectUrl() {
        if (mRedirectUrl == null) return "";
        else return mRedirectUrl;
    }

    public void setRedirectUrl(String redirect_url) {
        mRedirectUrl = redirect_url;
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mRedirectUrl);
        parcel.writeString(mText);
        parcel.writeString(mAppLinks);
        parcel.writeString(mVisibility);
    }
}
