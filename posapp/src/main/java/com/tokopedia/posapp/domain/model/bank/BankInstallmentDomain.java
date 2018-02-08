package com.tokopedia.posapp.domain.model.bank;

import java.util.List;

/**
 * Created by okasurya on 9/5/17.
 */

public class BankInstallmentDomain {
    private List<BankDomain> bankDomainList;

    public List<BankDomain> getBankDomainList() {
        return bankDomainList;
    }

    public void setBankDomainList(List<BankDomain> bankDomainList) {
        this.bankDomainList = bankDomainList;
    }
}
