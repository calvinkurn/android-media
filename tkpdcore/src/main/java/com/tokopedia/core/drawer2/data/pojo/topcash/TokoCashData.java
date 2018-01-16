package com.tokopedia.core.drawer2.data.pojo.topcash;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    @SerializedName("ab_tags")
    @Expose
    private List<String> abTags;


    protected TokoCashData(Parcel in) {
        mAction = in.readParcelable(Action.class.getClassLoader());
        mBalance = in.readString();
        mRedirectUrl = in.readString();
        mAppLinks = in.readString();
        mText = in.readString();
        if (in.readByte() == 0) {
            mWalletId = null;
        } else {
            mWalletId = in.readLong();
        }
        link = in.readInt();
        abTags = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mAction, flags);
        dest.writeString(mBalance);
        dest.writeString(mRedirectUrl);
        dest.writeString(mAppLinks);
        dest.writeString(mText);
        if (mWalletId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mWalletId);
        }
        dest.writeInt(link);
        dest.writeStringList(abTags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public List<String> getAbTags() {
        return abTags;
    }

    public void setAbTags(List<String> abTags) {
        this.abTags = abTags;
    }

    public TokoCashData() {
    }

}