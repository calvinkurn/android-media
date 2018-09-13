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
    private String bottomSheetTitleLeft;
    private String bottomSheetMessageLeft;
    private String bottomSheetButtonTextLeft;
    private String bottomSheetButtonRedirectionUrlLeft;
    private String bottomSheetTitleRight;
    private String bottomSheetMessageRight;
    private String bottomSheetButtonTextRight;
    private String bottomSheetButtonRedirectionUrlRight;
    private boolean isRightImportant;

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

    public String getBottomSheetTitleLeft() {
        return bottomSheetTitleLeft;
    }

    public void setBottomSheetTitleLeft(String bottomSheetTitleLeft) {
        this.bottomSheetTitleLeft = bottomSheetTitleLeft;
    }

    public String getBottomSheetMessageLeft() {
        return bottomSheetMessageLeft;
    }

    public void setBottomSheetMessageLeft(String bottomSheetMessageLeft) {
        this.bottomSheetMessageLeft = bottomSheetMessageLeft;
    }

    public String getBottomSheetButtonTextLeft() {
        return bottomSheetButtonTextLeft;
    }

    public void setBottomSheetButtonTextLeft(String bottomSheetButtonTextLeft) {
        this.bottomSheetButtonTextLeft = bottomSheetButtonTextLeft;
    }

    public String getBottomSheetButtonRedirectionUrlLeft() {
        return bottomSheetButtonRedirectionUrlLeft;
    }

    public void setBottomSheetButtonRedirectionUrlLeft(String bottomSheetButtonRedirectionUrlLeft) {
        this.bottomSheetButtonRedirectionUrlLeft = bottomSheetButtonRedirectionUrlLeft;
    }

    public String getBottomSheetTitleRight() {
        return bottomSheetTitleRight;
    }

    public void setBottomSheetTitleRight(String bottomSheetTitleRight) {
        this.bottomSheetTitleRight = bottomSheetTitleRight;
    }

    public String getBottomSheetMessageRight() {
        return bottomSheetMessageRight;
    }

    public void setBottomSheetMessageRight(String bottomSheetMessageRight) {
        this.bottomSheetMessageRight = bottomSheetMessageRight;
    }

    public String getBottomSheetButtonTextRight() {
        return bottomSheetButtonTextRight;
    }

    public void setBottomSheetButtonTextRight(String bottomSheetButtonTextRight) {
        this.bottomSheetButtonTextRight = bottomSheetButtonTextRight;
    }

    public String getBottomSheetButtonRedirectionUrlRight() {
        return bottomSheetButtonRedirectionUrlRight;
    }

    public void setBottomSheetButtonRedirectionUrlRight(String bottomSheetButtonRedirectionUrlRight) {
        this.bottomSheetButtonRedirectionUrlRight = bottomSheetButtonRedirectionUrlRight;
    }

    public boolean isRightImportant() {
        return isRightImportant;
    }

    public void setRightImportant(boolean rightImportant) {
        isRightImportant = rightImportant;
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
        dest.writeString(this.bottomSheetTitleLeft);
        dest.writeString(this.bottomSheetMessageLeft);
        dest.writeString(this.bottomSheetButtonTextLeft);
        dest.writeString(this.bottomSheetButtonRedirectionUrlLeft);
        dest.writeString(this.bottomSheetTitleRight);
        dest.writeString(this.bottomSheetMessageRight);
        dest.writeString(this.bottomSheetButtonTextRight);
        dest.writeString(this.bottomSheetButtonRedirectionUrlRight);
        dest.writeInt(isRightImportant ? 1 : 0);
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
        this.bottomSheetTitleLeft = in.readString();
        this.bottomSheetMessageLeft = in.readString();
        this.bottomSheetButtonTextLeft = in.readString();
        this.bottomSheetButtonRedirectionUrlLeft = in.readString();
        this.bottomSheetTitleRight = in.readString();
        this.bottomSheetMessageRight = in.readString();
        this.bottomSheetButtonTextRight = in.readString();
        this.bottomSheetButtonRedirectionUrlRight = in.readString();
        isRightImportant = in.readInt() == 1;
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
