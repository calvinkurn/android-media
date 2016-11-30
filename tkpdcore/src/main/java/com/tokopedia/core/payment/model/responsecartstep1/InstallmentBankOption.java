package com.tokopedia.core.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * InstallmentBankOption
 * Created by Angga.Prasetiyo on 11/07/2016.
 */
public class InstallmentBankOption implements Parcelable{
    @SerializedName("bank_id")
    @Expose
    private String bankId;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("installment_term")
    @Expose
    private List<InstallmentTerm> installmentTerm = new ArrayList<InstallmentTerm>();

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public List<InstallmentTerm> getInstallmentTerm() {
        return installmentTerm;
    }

    public void setInstallmentTerm(List<InstallmentTerm> installmentTerm) {
        this.installmentTerm = installmentTerm;
    }

    protected InstallmentBankOption(Parcel in) {
        bankId = in.readString();
        bankName = in.readString();
        percentage = in.readString();
        if (in.readByte() == 0x01) {
            installmentTerm = new ArrayList<InstallmentTerm>();
            in.readList(installmentTerm, InstallmentTerm.class.getClassLoader());
        } else {
            installmentTerm = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankId);
        dest.writeString(bankName);
        dest.writeString(percentage);
        if (installmentTerm == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(installmentTerm);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<InstallmentBankOption> CREATOR = new Parcelable.Creator<InstallmentBankOption>() {
        @Override
        public InstallmentBankOption createFromParcel(Parcel in) {
            return new InstallmentBankOption(in);
        }

        @Override
        public InstallmentBankOption[] newArray(int size) {
            return new InstallmentBankOption[size];
        }
    };
}
