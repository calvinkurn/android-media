package com.tokopedia.withdraw.domain.model;


import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

import java.util.List;

public class InfoDepositDomainModel {


    private int useableDeposit;
    private String useableDepositIdr;
    private List<BankAccountViewModel> bankAccount = null;

    public int getUseableDeposit() {
        return useableDeposit;
    }

    public void setUseableDeposit(int useableDeposit) {
        this.useableDeposit = useableDeposit;
    }

    public String getUseableDepositIdr() {
        return useableDepositIdr;
    }

    public void setUseableDepositIdr(String useableDepositIdr) {
        this.useableDepositIdr = useableDepositIdr;
    }

    public List<BankAccountViewModel> getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(List<BankAccountViewModel> bankAccount) {
        this.bankAccount = bankAccount;
    }

}