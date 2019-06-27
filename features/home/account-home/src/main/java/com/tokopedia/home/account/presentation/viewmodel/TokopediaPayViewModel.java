package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author okasurya on 7/17/18.
 */
public class TokopediaPayViewModel implements ParcelableViewModel<AccountTypeFactory> {
    private String labelLeft;
    private String amountLeft;
    private String labelRight;
    private String amountRight;
    private String applinkLeft;
    private String applinkRight;
    private String iconUrlRight;
    private String iconUrlLeft;
    private boolean isRightImportant;
    private boolean isRightSaldo;
    private TokopediaPayBSModel bsDataLeft;
    private TokopediaPayBSModel bsDataRight;
    private boolean isLinked;
    private String walletType;
    private String vccUserStatus;

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getLabelLeft() {
        return labelLeft;
    }

    public void setLabelLeft(String labelLeft) {
        this.labelLeft = labelLeft;
    }

    public String getAmountLeft() {
        return amountLeft;
    }

    public void setAmountLeft(String amountLeft) {
        this.amountLeft = amountLeft;
    }

    public String getLabelRight() {
        return labelRight;
    }

    public void setLabelRight(String labelRight) {
        this.labelRight = labelRight;
    }

    public String getAmountRight() {
        return amountRight;
    }

    public void setAmountRight(String amountRight) {
        this.amountRight = amountRight;
    }

    public String getApplinkLeft() {
        return applinkLeft;
    }

    public void setApplinkLeft(String applinkLeft) {
        this.applinkLeft = applinkLeft;
    }

    public String getApplinkRight() {
        return applinkRight;
    }

    public void setApplinkRight(String applinkRight) {
        this.applinkRight = applinkRight;
    }

    public String getIconUrlRight() {
        return iconUrlRight;
    }

    public void setIconUrlRight(String iconUrlRight) {
        this.iconUrlRight = iconUrlRight;
    }

    public String getIconUrlLeft() {
        return iconUrlLeft;
    }

    public void setIconUrlLeft(String iconUrlLeft) {
        this.iconUrlLeft = iconUrlLeft;
    }

    public boolean isRightImportant() {
        return isRightImportant;
    }

    public void setRightImportant(boolean rightImportant) {
        isRightImportant = rightImportant;
    }

    public TokopediaPayBSModel getBsDataLeft() {
        return bsDataLeft;
    }

    public void setBsDataLeft(TokopediaPayBSModel bsDataLeft) {
        this.bsDataLeft = bsDataLeft;
    }

    public TokopediaPayBSModel getBsDataRight() {
        return bsDataRight;
    }

    public void setBsDataRight(TokopediaPayBSModel bsDataRight) {
        this.bsDataRight = bsDataRight;
    }

    public boolean isLinked() {
        return isLinked;
    }

    public void setLinked(boolean linked) {
        isLinked = linked;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getVccUserStatus() {
        return vccUserStatus;
    }

    public void setVccUserStatus(String vccUserStatus) {
        this.vccUserStatus = vccUserStatus;
    }

    public boolean isRightSaldo() {
        return isRightSaldo;
    }

    public void setRightSaldo(boolean rightSaldo) {
        isRightSaldo = rightSaldo;
    }

    public TokopediaPayViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.labelLeft);
        dest.writeString(this.amountLeft);
        dest.writeString(this.labelRight);
        dest.writeString(this.amountRight);
        dest.writeString(this.applinkLeft);
        dest.writeString(this.applinkRight);
        dest.writeString(this.iconUrlRight);
        dest.writeString(this.iconUrlLeft);
        dest.writeByte(this.isRightImportant ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isRightSaldo ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.bsDataLeft, flags);
        dest.writeParcelable(this.bsDataRight, flags);
        dest.writeByte(this.isLinked ? (byte) 1 : (byte) 0);
        dest.writeString(this.walletType);
        dest.writeString(this.vccUserStatus);
    }

    protected TokopediaPayViewModel(Parcel in) {
        this.labelLeft = in.readString();
        this.amountLeft = in.readString();
        this.labelRight = in.readString();
        this.amountRight = in.readString();
        this.applinkLeft = in.readString();
        this.applinkRight = in.readString();
        this.iconUrlRight = in.readString();
        this.iconUrlLeft = in.readString();
        this.isRightImportant = in.readByte() != 0;
        this.isRightSaldo = in.readByte() != 0;
        this.bsDataLeft = in.readParcelable(TokopediaPayBSModel.class.getClassLoader());
        this.bsDataRight = in.readParcelable(TokopediaPayBSModel.class.getClassLoader());
        this.isLinked = in.readByte() != 0;
        this.walletType = in.readString();
        this.vccUserStatus = in.readString();
    }

    public static final Creator<TokopediaPayViewModel> CREATOR = new Creator<TokopediaPayViewModel>() {
        @Override
        public TokopediaPayViewModel createFromParcel(Parcel source) {
            return new TokopediaPayViewModel(source);
        }

        @Override
        public TokopediaPayViewModel[] newArray(int size) {
            return new TokopediaPayViewModel[size];
        }
    };
}