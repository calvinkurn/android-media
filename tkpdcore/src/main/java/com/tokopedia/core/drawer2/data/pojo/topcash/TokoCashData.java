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
    @SerializedName("raw_balance")
    @Expose
    private long raw_balance;
    @SerializedName("total_balance")
    @Expose
    private String totalBalance;
    @SerializedName("raw_total_balance")
    @Expose
    private long rawTotalBalance;
    @SerializedName("hold_balance")
    @Expose
    private String holdBalance;
    @SerializedName("raw_hold_balance")
    @Expose
    private long rawHoldBalance;
    @SerializedName("raw_threshold")
    @Expose
    private long rawThreshold;
    @SerializedName("threshold")
    @Expose
    private String threshold;

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
        raw_balance = in.readLong();
        totalBalance = in.readString();
        rawTotalBalance = in.readLong();
        holdBalance = in.readString();
        rawHoldBalance = in.readLong();
        rawThreshold = in.readLong();
        threshold = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mAction, i);
        parcel.writeString(mBalance);
        parcel.writeString(mRedirectUrl);
        parcel.writeString(mAppLinks);
        parcel.writeString(mText);
        if (mWalletId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(mWalletId);
        }
        parcel.writeInt(link);
        parcel.writeStringList(abTags);
        parcel.writeLong(raw_balance);
        parcel.writeString(totalBalance);
        parcel.writeLong(rawTotalBalance);
        parcel.writeString(holdBalance);
        parcel.writeLong(rawHoldBalance);
        parcel.writeLong(rawThreshold);
        parcel.writeString(threshold);
    }

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

    public void setmAppLinks(String mAppLinks) {
        this.mAppLinks = mAppLinks;
    }

    public List<String> getAbTags() {
        return abTags;
    }

    public void setAbTags(List<String> abTags) {
        this.abTags = abTags;
    }

    public long getRaw_balance() {
        return raw_balance;
    }

    public void setRaw_balance(long raw_balance) {
        this.raw_balance = raw_balance;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public long getRawTotalBalance() {
        return rawTotalBalance;
    }

    public void setRawTotalBalance(long rawTotalBalance) {
        this.rawTotalBalance = rawTotalBalance;
    }

    public String getHoldBalance() {
        return holdBalance;
    }

    public void setHoldBalance(String holdBalance) {
        this.holdBalance = holdBalance;
    }

    public long getRawHoldBalance() {
        return rawHoldBalance;
    }

    public void setRawHoldBalance(long rawHoldBalance) {
        this.rawHoldBalance = rawHoldBalance;
    }

    public long getRawThreshold() {
        return rawThreshold;
    }

    public void setRawThreshold(long rawThreshold) {
        this.rawThreshold = rawThreshold;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public TokoCashData() {

    }

}