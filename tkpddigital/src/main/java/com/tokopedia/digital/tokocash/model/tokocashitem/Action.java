
package com.tokopedia.digital.tokocash.model.tokocashitem;

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
    @SerializedName("type")
    private String mType;

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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    protected Action(Parcel in) {
        mRedirectUrl = in.readString();
        mText = in.readString();
        mType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRedirectUrl);
        dest.writeString(mText);
        dest.writeString(mType);
    }

    @SuppressWarnings("unused")
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

}
