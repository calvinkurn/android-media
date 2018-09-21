
package com.tokopedia.tokocash.balance.view;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nabillasabbaha on 11/15/17.
 */

public class BalanceTokoCash implements Parcelable {

    private String titleText;
    private ActionBalance actionBalance;
    private String balance;
    private long rawBalance;
    private String totalBalance;
    private long rawTotalBalance;
    private String holdBalance;
    private long rawHoldBalance;
    private String applinks;
    private String redirectUrl;
    private boolean link;
    private long rawThreshold;
    private String threshold;
    private List<String> abTags;
    private String pointBalance;
    private int rawPointBalance;
    private String cashBalance;
    private int rawCashBalance;
    private String walletType;

    public BalanceTokoCash() {
    }


    protected BalanceTokoCash(Parcel in) {
        titleText = in.readString();
        actionBalance = in.readParcelable(ActionBalance.class.getClassLoader());
        balance = in.readString();
        rawBalance = in.readLong();
        totalBalance = in.readString();
        rawTotalBalance = in.readLong();
        holdBalance = in.readString();
        rawHoldBalance = in.readLong();
        applinks = in.readString();
        redirectUrl = in.readString();
        link = in.readByte() != 0;
        rawThreshold = in.readLong();
        threshold = in.readString();
        abTags = in.createStringArrayList();
        pointBalance = in.readString();
        rawPointBalance = in.readInt();
        cashBalance = in.readString();
        rawCashBalance = in.readInt();
        walletType = in.readString();
    }

    public static final Creator<BalanceTokoCash> CREATOR = new Creator<BalanceTokoCash>() {
        @Override
        public BalanceTokoCash createFromParcel(Parcel in) {
            return new BalanceTokoCash(in);
        }

        @Override
        public BalanceTokoCash[] newArray(int size) {
            return new BalanceTokoCash[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(titleText);
        parcel.writeParcelable(actionBalance, i);
        parcel.writeString(balance);
        parcel.writeLong(rawBalance);
        parcel.writeString(totalBalance);
        parcel.writeLong(rawTotalBalance);
        parcel.writeString(holdBalance);
        parcel.writeLong(rawHoldBalance);
        parcel.writeString(applinks);
        parcel.writeString(redirectUrl);
        parcel.writeByte((byte) (link ? 1 : 0));
        parcel.writeLong(rawThreshold);
        parcel.writeString(threshold);
        parcel.writeStringList(abTags);
        parcel.writeString(pointBalance);
        parcel.writeInt(rawPointBalance);
        parcel.writeString(cashBalance);
        parcel.writeInt(rawCashBalance);
        parcel.writeString(walletType);
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public ActionBalance getActionBalance() {
        return actionBalance;
    }

    public void setActionBalance(ActionBalance actionBalance) {
        this.actionBalance = actionBalance;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public long getRawBalance() {
        return rawBalance;
    }

    public void setRawBalance(long rawBalance) {
        this.rawBalance = rawBalance;
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

    public boolean getLink() {
        return link;
    }

    public void setLink(boolean link) {
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

    public List<String> getAbTags() {
        return abTags;
    }

    public void setAbTags(List<String> abTags) {
        this.abTags = abTags;
    }

    public String getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(String pointBalance) {
        this.pointBalance = pointBalance;
    }

    public int getRawPointBalance() {
        return rawPointBalance;
    }

    public void setRawPointBalance(int rawPointBalance) {
        this.rawPointBalance = rawPointBalance;
    }

    public String getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(String cashBalance) {
        this.cashBalance = cashBalance;
    }

    public int getRawCashBalance() {
        return rawCashBalance;
    }

    public void setRawCashBalance(int rawCashBalance) {
        this.rawCashBalance = rawCashBalance;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }
}