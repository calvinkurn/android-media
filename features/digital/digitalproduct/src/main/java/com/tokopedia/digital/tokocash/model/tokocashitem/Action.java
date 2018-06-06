
package com.tokopedia.digital.tokocash.model.tokocashitem;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 11/15/17.
 */

public class Action implements Parcelable {

    @SerializedName("redirect_url")
    private String redirectUrl;
    @SerializedName("text")
    private String labelAction;
    @SerializedName("applinks")
    private String applinks;
    @SerializedName("visibility")
    private String visibility;

    public Action() {
    }

    protected Action(Parcel in) {
        redirectUrl = in.readString();
        labelAction = in.readString();
        applinks = in.readString();
        visibility = in.readString();
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
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getLabelAction() {
        return labelAction;
    }

    public void setLabelAction(String labelAction) {
        this.labelAction = labelAction;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(redirectUrl);
        parcel.writeString(labelAction);
        parcel.writeString(applinks);
        parcel.writeString(visibility);
    }
}
