package com.tokopedia.home.account.presentation.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.account.presentation.adapter.buyer.BuyerAccountTypeFactory;

/**
 * @author okasurya on 7/17/18.
 */
public class TokopediaPayViewModel implements Visitable<BuyerAccountTypeFactory> {
    private String tokoCash;
    private String balance;

    @Override
    public int type(BuyerAccountTypeFactory typeFactory) {
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
