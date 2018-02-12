package com.tokopedia.core.drawer2.data.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 9/18/17.
 */

public class DrawerWalletAction implements Parcelable {
    public static final int TYPE_ACTION_ACTIVATION = 1;
    public static final int TYPE_ACTION_BALANCE = 2;

    private String labelTitle;
    private String balance;
    private String redirectUrlBalance;
    private String appLinkBalance;
    private int typeAction;
    private boolean visibleActionButton;
    private String labelActionButton;
    private String appLinkActionButton;
    private String redirectUrlActionButton;

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

    public String getRedirectUrlActionButton() {
        return redirectUrlActionButton;
    }

    public void setRedirectUrlActionButton(String redirectUrlActionButton) {
        this.redirectUrlActionButton = redirectUrlActionButton;
    }


    public DrawerWalletAction() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.labelTitle);
        dest.writeString(this.balance);
        dest.writeString(this.redirectUrlBalance);
        dest.writeString(this.appLinkBalance);
        dest.writeInt(this.typeAction);
        dest.writeByte(this.visibleActionButton ? (byte) 1 : (byte) 0);
        dest.writeString(this.labelActionButton);
        dest.writeString(this.appLinkActionButton);
        dest.writeString(this.redirectUrlActionButton);
    }

    protected DrawerWalletAction(Parcel in) {
        this.labelTitle = in.readString();
        this.balance = in.readString();
        this.redirectUrlBalance = in.readString();
        this.appLinkBalance = in.readString();
        this.typeAction = in.readInt();
        this.visibleActionButton = in.readByte() != 0;
        this.labelActionButton = in.readString();
        this.appLinkActionButton = in.readString();
        this.redirectUrlActionButton = in.readString();
    }

    public static final Creator<DrawerWalletAction> CREATOR = new Creator<DrawerWalletAction>() {
        @Override
        public DrawerWalletAction createFromParcel(Parcel source) {
            return new DrawerWalletAction(source);
        }

        @Override
        public DrawerWalletAction[] newArray(int size) {
            return new DrawerWalletAction[size];
        }
    };
}
