package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GqlMclDialogResponse implements Parcelable {

    @SerializedName("dialog_title")
    private String dialogTitle;

    @SerializedName("dialog_body")
    private String dialogBody;

    @SerializedName("dialog_button_text")
    private String dialogButtonText;

    @SerializedName("dialog_button_link")
    private String dialogButtonLink;


    protected GqlMclDialogResponse(Parcel in) {
        this.dialogTitle = in.readString();
        this.dialogBody = in.readString();
        this.dialogButtonText = in.readString();
        this.dialogButtonLink = in.readString();
    }

    public static final Creator<GqlMclDialogResponse> CREATOR = new Creator<GqlMclDialogResponse>() {
        @Override
        public GqlMclDialogResponse createFromParcel(Parcel in) {
            return new GqlMclDialogResponse(in);
        }

        @Override
        public GqlMclDialogResponse[] newArray(int size) {
            return new GqlMclDialogResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dialogTitle);
        dest.writeString(dialogBody);
        dest.writeString(dialogButtonText);
        dest.writeString(dialogButtonLink);
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public String getDialogBody() {
        return dialogBody;
    }

    public void setDialogBody(String dialogBody) {
        this.dialogBody = dialogBody;
    }

    public String getDialogButtonText() {
        return dialogButtonText;
    }

    public void setDialogButtonText(String dialogButtonText) {
        this.dialogButtonText = dialogButtonText;
    }

    public String getDialogButtonLink() {
        return dialogButtonLink;
    }

    public void setDialogButtonLink(String dialogButtonLink) {
        this.dialogButtonLink = dialogButtonLink;
    }
}
