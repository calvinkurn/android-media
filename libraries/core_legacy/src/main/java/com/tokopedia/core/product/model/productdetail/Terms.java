
package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
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

    @SerializedName("18")
    @Expose
    private InstallmentRule rule18Months;

    @SerializedName("24")
    @Expose
    private InstallmentRule rule24Months;

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

    public InstallmentRule getRule18Months() {
        return rule18Months;
    }

    public void setRule18Months(InstallmentRule rule18Months) {
        this.rule18Months = rule18Months;
    }

    public InstallmentRule getRule24Months() {
        return rule24Months;
    }

    public void setRule24Months(InstallmentRule rule24Months) {
        this.rule24Months = rule24Months;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.rule6Months, flags);
        dest.writeParcelable(this.rule3Months, flags);
        dest.writeParcelable(this.rule12Months, flags);
        dest.writeParcelable(this.rule18Months, flags);
        dest.writeParcelable(this.rule24Months, flags);
    }

    public Terms() {
    }

    protected Terms(Parcel in) {
        this.rule6Months = in.readParcelable(InstallmentRule.class.getClassLoader());
        this.rule3Months = in.readParcelable(InstallmentRule.class.getClassLoader());
        this.rule12Months = in.readParcelable(InstallmentRule.class.getClassLoader());
        this.rule18Months = in.readParcelable(InstallmentRule.class.getClassLoader());
        this.rule24Months = in.readParcelable(InstallmentRule.class.getClassLoader());
    }

    public static final Creator<Terms> CREATOR = new Creator<Terms>() {
        @Override
        public Terms createFromParcel(Parcel source) {
            return new Terms(source);
        }

        @Override
        public Terms[] newArray(int size) {
            return new Terms[size];
        }
    };
}
