package com.tokopedia.posapp.bank.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 9/13/17.
 */

public class CCBinResponse {
    @SerializedName("bank_id")
    @Expose
    private int bankId;

    @SerializedName("bank_name")
    @Expose
    private String bankName;

    @SerializedName("bank_logo")
    @Expose
    private String bankLogo;

    @SerializedName("validate_bin")
    @Expose
    private List<String> validateBin;

    @SerializedName("installment_bin")
    @Expose
    private List<String> installmentBin;

    @SerializedName("allow_installment")
    @Expose
    private Boolean allowInstallment;

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

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public List<String> getValidateBin() {
        return validateBin;
    }

    public void setValidateBin(List<String> validateBin) {
        this.validateBin = validateBin;
    }

    public List<String> getInstallmentBin() {
        return installmentBin;
    }

    public void setInstallmentBin(List<String> installmentBin) {
        this.installmentBin = installmentBin;
    }

    public Boolean getAllowInstallment() {
        return allowInstallment;
    }

    public void setAllowInstallment(Boolean allowInstallment) {
        this.allowInstallment = allowInstallment;
    }
}
