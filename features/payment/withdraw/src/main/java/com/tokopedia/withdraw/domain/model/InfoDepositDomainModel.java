package com.tokopedia.withdraw.domain.model;


import com.tokopedia.withdraw.view.model.BankAccount;

import java.util.List;

public class InfoDepositDomainModel {


    private int useableDeposit;
    private String useableDepositIdr;
    private List<BankAccount> bankAccount = null;

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

    public List<BankAccount> getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(List<BankAccount> bankAccount) {
        this.bankAccount = bankAccount;
    }

}