package com.tokopedia.posapp.bank.domain.model;

import java.util.List;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankDomain {
    private int bankId;
    private String bankName;
    private String bankLogo;
    private List<InstallmentDomain> installmentList;
    private List<String> bin;
    private List<String> binInstallment;
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

    public List<InstallmentDomain> getInstallmentList() {
        return installmentList;
    }

    public void setInstallmentList(List<InstallmentDomain> installmentList) {
        this.installmentList = installmentList;
    }

    public List<String> getBin() {
        return bin;
    }

    public void setBin(List<String> bin) {
        this.bin = bin;
    }

    public List<String> getBinInstallment() {
        return binInstallment;
    }

    public void setBinInstallment(List<String> binInstallment) {
        this.binInstallment = binInstallment;
    }

    public Boolean getAllowInstallment() {
        return allowInstallment;
    }

    public void setAllowInstallment(Boolean allowInstallment) {
        this.allowInstallment = allowInstallment;
    }
}
