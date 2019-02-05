package com.tokopedia.home.beranda.presentation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class HomeHeaderWalletAction implements Parcelable {

    private String labelTitle;
    private String balance;
    private String redirectUrlBalance;
    private String appLinkBalance;
    private int typeAction;
    private boolean visibleActionButton;
    private String labelActionButton;
    private String appLinkActionButton;
    private boolean linked;
    private List<String> abTags = new ArrayList<>();
    private String pointBalance;
    private int rawPointBalance;
    private String cashBalance;
    private int rawCashBalance;
    private String walletType;
    private boolean showAnnouncement;

    protected HomeHeaderWalletAction(Parcel in) {
        labelTitle = in.readString();
        balance = in.readString();
        redirectUrlBalance = in.readString();
        appLinkBalance = in.readString();
        typeAction = in.readInt();
        visibleActionButton = in.readByte() != 0;
        labelActionButton = in.readString();
        appLinkActionButton = in.readString();
        linked = in.readByte() != 0;
        abTags = in.createStringArrayList();
        pointBalance = in.readString();
        rawPointBalance = in.readInt();
        cashBalance = in.readString();
        rawCashBalance = in.readInt();
        walletType = in.readString();
        showAnnouncement = in.readByte() != 0;
    }

    public static final Creator<HomeHeaderWalletAction> CREATOR = new Creator<HomeHeaderWalletAction>() {
        @Override
        public HomeHeaderWalletAction createFromParcel(Parcel in) {
            return new HomeHeaderWalletAction(in);
        }

        @Override
        public HomeHeaderWalletAction[] newArray(int size) {
            return new HomeHeaderWalletAction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(labelTitle);
        parcel.writeString(balance);
        parcel.writeString(redirectUrlBalance);
        parcel.writeString(appLinkBalance);
        parcel.writeInt(typeAction);
        parcel.writeByte((byte) (visibleActionButton ? 1 : 0));
        parcel.writeString(labelActionButton);
        parcel.writeString(appLinkActionButton);
        parcel.writeByte((byte) (linked ? 1 : 0));
        parcel.writeStringList(abTags);
        parcel.writeString(pointBalance);
        parcel.writeInt(rawPointBalance);
        parcel.writeString(cashBalance);
        parcel.writeInt(rawCashBalance);
        parcel.writeString(walletType);
        parcel.writeByte((byte) (showAnnouncement ? 1 : 0));
    }

    public String getLabelTitle() {
        return labelTitle;
    }

    public void setLabelTitle(String labelTitle) {
        this.labelTitle = labelTitle;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRedirectUrlBalance() {
        return redirectUrlBalance;
    }

    public void setRedirectUrlBalance(String redirectUrlBalance) {
        this.redirectUrlBalance = redirectUrlBalance;
    }

    public String getAppLinkBalance() {
        return appLinkBalance;
    }

    public void setAppLinkBalance(String appLinkBalance) {
        this.appLinkBalance = appLinkBalance;
    }

    public int getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(int typeAction) {
        this.typeAction = typeAction;
    }

    public boolean isVisibleActionButton() {
        return visibleActionButton;
    }

    public void setVisibleActionButton(boolean visibleActionButton) {
        this.visibleActionButton = visibleActionButton;
    }

    public String getLabelActionButton() {
        return labelActionButton;
    }

    public void setLabelActionButton(String labelActionButton) {
        this.labelActionButton = labelActionButton;
    }

    public String getAppLinkActionButton() {
        return appLinkActionButton;
    }

    public void setAppLinkActionButton(String appLinkActionButton) {
        this.appLinkActionButton = appLinkActionButton;
    }

    public List<String> getAbTags() {
        return abTags;
    }

    public void setAbTags(List<String> abTags) {
        this.abTags = abTags;
    }

    public boolean isLinked() {
        return linked;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
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

    public boolean isShowAnnouncement() {
        return showAnnouncement;
    }

    public void setShowAnnouncement(boolean showAnnouncement) {
        this.showAnnouncement = showAnnouncement;
    }

    public HomeHeaderWalletAction() {
    }
}
