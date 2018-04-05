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
        dest.writeTypedList(this.messageList);
    }

    public InAppMessageModel() {
    }

    protected InAppMessageModel(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.type = in.readString();
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
