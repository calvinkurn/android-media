
package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Terms implements Parcelable {

    @SerializedName("6")
    @Expose
    private InstallmentRule rule6Months;
    @SerializedName("3")
    @Expose
    private InstallmentRule rule3Months;
    @SerializedName("12")
    @Expose
    private InstallmentRule rule12Months;

    public InstallmentRule getRule6Months() {
        return rule6Months;
    }

    public void setRule6Months(InstallmentRule rule6Months) {
        this.rule6Months = rule6Months;
    }

    public InstallmentRule getRule3Months() {
        return rule3Months;
    }

    public void setRule3Months(InstallmentRule rule3Months) {
        this.rule3Months = rule3Months;
    }

    public InstallmentRule getRule12Months() {
        return rule12Months;
    }

    public void setRule12Months(InstallmentRule rule12Months) {
        this.rule12Months = rule12Months;
    }

    protected Terms(Parcel in) {
        rule6Months = (InstallmentRule) in.readValue(InstallmentRule.class.getClassLoader());
        rule3Months = (InstallmentRule) in.readValue(InstallmentRule.class.getClassLoader());
        rule12Months = (InstallmentRule) in.readValue(InstallmentRule.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(rule6Months);
        dest.writeValue(rule3Months);
        dest.writeValue(rule12Months);
    }

    @SuppressWarnings("unused")
    public static final Creator<Terms> CREATOR = new Creator<Terms>() {
        @Override
        public Terms createFromParcel(Parcel in) {
            return new Terms(in);
        }

        @Override
        public Terms[] newArray(int size) {
            return new Terms[size];
        }
    };
}
