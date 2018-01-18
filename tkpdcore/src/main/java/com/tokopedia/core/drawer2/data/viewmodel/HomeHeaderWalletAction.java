package com.tokopedia.core.drawer2.data.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author anggaprasetiyo on 9/13/17.
 */

public class HomeHeaderWalletAction implements Parcelable {
    public static final int TYPE_ACTION_ACTIVATION = 1;
    public static final int TYPE_ACTION_TOP_UP = 2;

    private String labelTitle;
    private String balance;
    private String redirectUrlBalance;
    private String appLinkBalance;
    private int typeAction;
    private boolean visibleActionButton;
    private String labelActionButton;
    private String appLinkActionButton;
    private String redirectUrlActionButton;
    private List<String> abTags;

    protected HomeHeaderWalletAction(Parcel in) {
        labelTitle = in.readString();
        balance = in.readString();
        redirectUrlBalance = in.readString();
        appLinkBalance = in.readString();
        typeAction = in.readInt();
        visibleActionButton = in.readByte() != 0;
        labelActionButton = in.readString();
        appLinkActionButton = in.readString();
        redirectUrlActionButton = in.readString();
        abTags = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(labelTitle);
        dest.writeString(balance);
        dest.writeString(redirectUrlBalance);
        dest.writeString(appLinkBalance);
        dest.writeInt(typeAction);
        dest.writeByte((byte) (visibleActionButton ? 1 : 0));
        dest.writeString(labelActionButton);
        dest.writeString(appLinkActionButton);
        dest.writeString(redirectUrlActionButton);
        dest.writeStringList(abTags);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public List<String> getAbTags() {
        return abTags;
    }

    public void setAbTags(List<String> abTags) {
        this.abTags = abTags;
    }

    public HomeHeaderWalletAction() {
    }
}
