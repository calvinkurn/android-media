package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author by StevenFredian on 15/05/18.
 */

public class AdsViewModel implements Visitable<GroupChatTypeFactory>, Parcelable {
    public static final String TYPE = "ads";

    @SerializedName("ads_url")
    @Expose
    private String adsUrl;
    @SerializedName("ads_link")
    @Expose
    private String adsLink;
    @SerializedName("ads_id")
    @Expose
    private String adsId;

    public AdsViewModel(String adsUrl, String adsLink, String adsId) {
        this.adsUrl = adsUrl;
        this.adsLink = adsLink;
        this.adsId = adsId;
    }

    protected AdsViewModel(Parcel in) {
        adsUrl = in.readString();
        adsLink = in.readString();
        adsId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adsUrl);
        dest.writeString(adsLink);
        dest.writeString(adsId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AdsViewModel> CREATOR = new Creator<AdsViewModel>() {
        @Override
        public AdsViewModel createFromParcel(Parcel in) {
            return new AdsViewModel(in);
        }

        @Override
        public AdsViewModel[] newArray(int size) {
            return new AdsViewModel[size];
        }
    };

    public String getAdsUrl() {
        return adsUrl;
    }

    public void setAdsUrl(String adsUrl) {
        this.adsUrl = adsUrl;
    }

    public String getAdsLink() {
        return adsLink;
    }

    public void setAdsLink(String adsLink) {
        this.adsLink = adsLink;
    }

    public String getAdsId() {
        return adsId;
    }

    public void setAdsId(String adsId) {
        this.adsId = adsId;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return 0;
    }
}
