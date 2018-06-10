package com.tokopedia.tkpd.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwanityagi on 04/04/18.
 */

public class InAppMessageModel implements Parcelable {

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("type")
    public String type;

    @SerializedName("close_btn_show")
    public String closeButtonShow;

    @SerializedName("header_background")
    public String headerBackgroundColor;

    @SerializedName("header_text_alignment")
    public String headerTextAlignment;

    @SerializedName("color_title")
    public String colorTitle;

    @SerializedName("color_desc")
    public String colorDesc;

    @SerializedName("action_btn_txt_1")
    public String actionBtnText1;

    @SerializedName("action_btn_txt_2")
    public String actionBtnText2;

    @SerializedName("action_deeplink_1")
    public String actionDeeplink1;

    @SerializedName("action_deeplink_2")
    public String actionDeeplink2;

    @SerializedName("header_visibile")
    public String headerVisibile;

    @SerializedName("close_btn_shape")
    public String closeButtonShape;

    @SerializedName("body")
    public List<InAppMessageItemModel> messageList = new ArrayList<InAppMessageItemModel>();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.type);
        dest.writeString(this.closeButtonShow);
        dest.writeString(this.headerBackgroundColor);
        dest.writeString(this.headerTextAlignment);
        dest.writeString(this.colorTitle);
        dest.writeString(this.colorDesc);
        dest.writeString(this.actionBtnText1);
        dest.writeString(this.actionBtnText2);
        dest.writeString(this.actionDeeplink1);
        dest.writeString(this.actionDeeplink2);
        dest.writeString(this.headerVisibile);
        dest.writeString(this.closeButtonShape);
        dest.writeTypedList(this.messageList);
    }

    public InAppMessageModel() {
    }

    protected InAppMessageModel(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.type = in.readString();
        this.closeButtonShow = in.readString();
        this.headerBackgroundColor = in.readString();
        this.headerTextAlignment = in.readString();
        this.colorTitle = in.readString();
        this.colorDesc = in.readString();
        this.actionBtnText1 = in.readString();
        this.actionBtnText2 = in.readString();
        this.actionDeeplink1 = in.readString();
        this.actionDeeplink2 = in.readString();
        this.headerVisibile = in.readString();
        this.closeButtonShape = in.readString();
        this.messageList = in.createTypedArrayList(InAppMessageItemModel.CREATOR);
    }

    public static final Creator<InAppMessageModel> CREATOR = new Creator<InAppMessageModel>() {
        @Override
        public InAppMessageModel createFromParcel(Parcel source) {
            return new InAppMessageModel(source);
        }

        @Override
        public InAppMessageModel[] newArray(int size) {
            return new InAppMessageModel[size];
        }
    };
}
