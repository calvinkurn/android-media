package com.tokopedia.tkpd.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 20/06/2016.
 */
public class SysBankAccount {
    private static final String TAG = SysBankAccount.class.getSimpleName();

    @SerializedName("sysbank_account_number")
    @Expose
    private String sysbankAccountNumber;
    @SerializedName("sysbank_account_name")
    @Expose
    private String sysbankAccountName;
    @SerializedName("sysbank_name")
    @Expose
    private String sysbankName;
    @SerializedName("sysbank_note")
    @Expose
    private String sysbankNote;
    @SerializedName("sysbank_id")
    @Expose
    private String sysbankId;

    public String getSysbankAccountNumber() {
        return sysbankAccountNumber;
    }

    public void setSysbankAccountNumber(String sysbankAccountNumber) {
        this.sysbankAccountNumber = sysbankAccountNumber;
    }

    public String getSysbankAccountName() {
        return sysbankAccountName;
    }

    public void setSysbankAccountName(String sysbankAccountName) {
        this.sysbankAccountName = sysbankAccountName;
    }

    public String getSysbankName() {
        return sysbankName;
    }

    public void setSysbankName(String sysbankName) {
        this.sysbankName = sysbankName;
    }

    public String getSysbankNote() {
        return sysbankNote;
    }

    public void setSysbankNote(String sysbankNote) {
        this.sysbankNote = sysbankNote;
    }

    public String getSysbankId() {
        return sysbankId;
    }

    public void setSysbankId(String sysbankId) {
        this.sysbankId = sysbankId;
    }

    public static SysBankAccount instanceInfo(String info) {
        SysBankAccount data = new SysBankAccount();
        data.setSysbankName(info);
        data.setSysbankId("0");
        return data;
    }

    @Override
    public String toString() {
        return sysbankName;
    }
}
