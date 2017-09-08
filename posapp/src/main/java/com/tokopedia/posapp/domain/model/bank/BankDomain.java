package com.tokopedia.posapp.domain.model.bank;

import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankDomain extends BaseModel {
    private int bankId;
    private String bankName;
    private List<InstallmentDomain> installmentList;

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

    public List<InstallmentDomain> getInstallmentList() {
        return installmentList;
    }

    public void setInstallmentList(List<InstallmentDomain> installmentList) {
        this.installmentList = installmentList;
    }
}
