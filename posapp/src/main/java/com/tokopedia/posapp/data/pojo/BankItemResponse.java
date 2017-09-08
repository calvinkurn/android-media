package com.tokopedia.posapp.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 9/8/17.
 */

public class BankItemResponse {
    @SerializedName("bank_id")
    @Expose
    private int bankId;

    @SerializedName("bank_name")
    @Expose
    private String bankName;

    @SerializedName("installment_list")
    @Expose
    private List<InstallmentResponse> installmentList;

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<InstallmentResponse> getInstallmentList() {
        return installmentList;
    }

    public void setInstallmentList(List<InstallmentResponse> installmentList) {
        this.installmentList = installmentList;
    }
}
