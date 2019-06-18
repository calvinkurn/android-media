
package com.tokopedia.gamification.taptap.data.entiity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class BackButton implements Parcelable {

    @SerializedName("cancelText")
    private String mCancelText;
    @SerializedName("imageURL")
    private String mImageURL;
    @SerializedName("isShow")
    private boolean mIsShow;
    @SerializedName("text")
    private String mText;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("yesText")
    private String mYesText;

    protected BackButton(Parcel in) {
        mCancelText = in.readString();
        mImageURL = in.readString();
        mIsShow = in.readByte() != 0;
        mText = in.readString();
        mTitle = in.readString();
        mYesText = in.readString();
    }

    public static final Creator<BackButton> CREATOR = new Creator<BackButton>() {
        @Override
        public BackButton createFromParcel(Parcel in) {
            return new BackButton(in);
        }

        @Override
        public BackButton[] newArray(int size) {
            return new BackButton[size];
        }
    };

    public String getCancelText() {
        return mCancelText;
    }

    public void setCancelText(String cancelText) {
        mCancelText = cancelText;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }

    public boolean getIsShow() {
        return mIsShow;
    }

    public void setIsShow(boolean isShow) {
        mIsShow = isShow;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getYesText() {
        return mYesText;
    }

    public void setYesText(String yesText) {
        mYesText = yesText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCancelText);
        dest.writeString(mImageURL);
        dest.writeByte((byte) (mIsShow ? 1 : 0));
        dest.writeString(mText);
        dest.writeString(mTitle);
        dest.writeString(mYesText);
    }
}
