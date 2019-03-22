package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GqlAnchorListResponse implements Parcelable {

    @SerializedName("title")
    private String label;

    @SerializedName("text_color")
    private String color;

    @SerializedName("show_dialog")
    private boolean showDialog;

    @SerializedName("dialog")
    private GqlMclDialogResponse dialogInfo;

    @SerializedName("link")
    private String link;


    public GqlAnchorListResponse(Parcel in) {
        this.label = in.readString();
        this.color = in.readString();
        this.showDialog = in.readByte() != 0;
        this.dialogInfo = ((GqlMclDialogResponse) in.readValue((GqlMclDialogResponse.class.getClassLoader())));
        this.link = in.readString();
    }

    public static final Creator<GqlAnchorListResponse> CREATOR = new Creator<GqlAnchorListResponse>() {
        @Override
        public GqlAnchorListResponse createFromParcel(Parcel in) {
            return new GqlAnchorListResponse(in);
        }

        @Override
        public GqlAnchorListResponse[] newArray(int size) {
            return new GqlAnchorListResponse[size];
        }
    };

    public GqlAnchorListResponse() {


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(color);
        dest.writeByte((byte) (showDialog ? 1 : 0));
        dest.writeValue(dialogInfo);
        dest.writeString(link);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public GqlMclDialogResponse getDialogInfo() {
        return dialogInfo;
    }

    public void setDialogInfo(GqlMclDialogResponse dialogInfo) {
        this.dialogInfo = dialogInfo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
