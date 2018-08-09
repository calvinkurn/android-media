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
    }

    protected TokopediaPayViewModel(Parcel in) {
        this.labelLeft = in.readString();
        this.amountLeft = in.readString();
        this.labelRight = in.readString();
        this.amountRight = in.readString();
        this.applinkLeft = in.readString();
        this.applinkRight = in.readString();
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
