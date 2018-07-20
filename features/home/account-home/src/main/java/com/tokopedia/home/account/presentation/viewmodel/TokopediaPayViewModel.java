package com.tokopedia.home.account.presentation.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;

/**
 * @author okasurya on 7/17/18.
 */
public class TokopediaPayViewModel implements Visitable<AccountTypeFactory> {
    private String tokoCash;
    private String balance;

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getTokoCash() {
        return tokoCash;
    }

    public void setTokoCash(String tokoCash) {
        this.tokoCash = tokoCash;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
