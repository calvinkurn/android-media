package com.tokopedia.gamification.floating.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 4/9/18.
 */

public class TokenEmptyState implements Parcelable {

    private String title;
    private String buttonText;
    private String buttonApplink;
    private String buttonURL;

    public TokenEmptyState() {
    }

    protected TokenEmptyState(Parcel in) {
        title = in.readString();
        buttonText = in.readString();
        buttonApplink = in.readString();
        buttonURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(buttonText);
        dest.writeString(buttonApplink);
        dest.writeString(buttonURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokenEmptyState> CREATOR = new Creator<TokenEmptyState>() {
        @Override
        public TokenEmptyState createFromParcel(Parcel in) {
            return new TokenEmptyState(in);
        }

        @Override
        public TokenEmptyState[] newArray(int size) {
            return new TokenEmptyState[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonApplink() {
        return buttonApplink;
    }

    public void setButtonApplink(String buttonApplink) {
        this.buttonApplink = buttonApplink;
    }

    public String getButtonURL() {
        return buttonURL;
    }

    public void setButtonURL(String buttonURL) {
        this.buttonURL = buttonURL;
    }
}
