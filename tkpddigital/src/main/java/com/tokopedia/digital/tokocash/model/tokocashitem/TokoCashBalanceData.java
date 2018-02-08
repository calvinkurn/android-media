
package com.tokopedia.digital.tokocash.model.tokocashitem;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 11/15/17.
 */

public class TokoCashBalanceData implements Parcelable {

    @SerializedName("text")
    private String titleText;
    @SerializedName("action")
    private Action action;
    @SerializedName("balance")
    private String balance;
    @SerializedName("raw_balance")
    private String raw_balance;
    @SerializedName("total_balance")
    private String totalBalance;
    @SerializedName("raw_total_balance")
    private long rawTotalBalance;
    @SerializedName("hold_balance")
    private String holdBalance;
    @SerializedName("raw_hold_balance")
    private long rawHoldBalance;
    @SerializedName("applinks")
    private String applinks;
    @SerializedName("redirect_url")
    private String redirectUrl;
    @SerializedName("link")
    private int link;
    @SerializedName("raw_threshold")
    private long rawThreshold;
    @SerializedName("threshold")
    private String threshold;

    public TokoCashBalanceData() {
    }


    protected TokoCashBalanceData(Parcel in) {
        titleText = in.readString();
        action = in.readParcelable(Action.class.getClassLoader());
        balance = in.readString();
        raw_balance = in.readString();
        totalBalance = in.readString();
        rawTotalBalance = in.readLong();
        holdBalance = in.readString();
        rawHoldBalance = in.readLong();
        applinks = in.readString();
        redirectUrl = in.readString();
        link = in.readInt();
        rawThreshold = in.readLong();
        threshold = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titleText);
        dest.writeParcelable(action, flags);
        dest.writeString(balance);
        dest.writeString(raw_balance);
        dest.writeString(totalBalance);
        dest.writeLong(rawTotalBalance);
        dest.writeString(holdBalance);
        dest.writeLong(rawHoldBalance);
        dest.writeString(applinks);
        dest.writeString(redirectUrl);
        dest.writeInt(link);
        dest.writeLong(rawThreshold);
        dest.writeString(threshold);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokoCashBalanceData> CREATOR = new Creator<TokoCashBalanceData>() {
        @Override
        public TokoCashBalanceData createFromParcel(Parcel in) {
            return new TokoCashBalanceData(in);
        }

        @Override
        public TokoCashBalanceData[] newArray(int size) {
            return new TokoCashBalanceData[size];
        }
    };

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRaw_balance() {
        return raw_balance;
    }

    public void setRaw_balance(String raw_balance) {
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

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
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


}