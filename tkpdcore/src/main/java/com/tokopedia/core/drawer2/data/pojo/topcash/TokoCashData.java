package com.tokopedia.core.drawer2.data.pojo.topcash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 5/5/17.
 */

public class TokoCashData implements Parcelable {

    @SerializedName("action")
    @Expose
    private Action mAction;
    @SerializedName("balance")
    @Expose
    private String mBalance;
    @SerializedName("redirect_url")
    @Expose
    private String mRedirectUrl;
    @SerializedName("applinks")
    @Expose
    private String mAppLinks;
    @SerializedName("text")
    @Expose
    private String mText;
    @SerializedName("wallet_id")
    @Expose
    private Long mWalletId;
    @SerializedName("link")
    @Expose
    private int link;


    public static final Creator<TokoCashData> CREATOR = new Creator<TokoCashData>() {
        @Override
        public TokoCashData createFromParcel(Parcel in) {
            return new TokoCashData(in);
        }

        @Override
        public TokoCashData[] newArray(int size) {
            return new TokoCashData[size];
        }
    };

    public Action getAction() {
        return mAction;
    }

    public void setAction(Action action) {
        mAction = action;
    }

    public String getBalance() {
        return mBalance;
    }

    public void setBalance(String balance) {
        mBalance = balance;
    }

    public String getRedirectUrl() {
        return mRedirectUrl;
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

    public Long getWalletId() {
        return mWalletId;
    }

    public void setWalletId(Long wallet_id) {
        mWalletId = wallet_id;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public String getmAppLinks() {
        return mAppLinks;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mAction, flags);
        dest.writeString(this.mBalance);
        dest.writeString(this.mRedirectUrl);
        dest.writeString(this.mAppLinks);
        dest.writeString(this.mText);
        dest.writeValue(this.mWalletId);
        dest.writeInt(this.link);
    }

    public TokoCashData() {
    }

    protected TokoCashData(Parcel in) {
        this.mAction = in.readParcelable(Action.class.getClassLoader());
        this.mBalance = in.readString();
        this.mRedirectUrl = in.readString();
        this.mAppLinks = in.readString();
        this.mText = in.readString();
        this.mWalletId = (Long) in.readValue(Long.class.getClassLoader());
        this.link = in.readInt();
    }

}
