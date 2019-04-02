package com.tokopedia.home.account.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.home.account.di.component.AccountHomeComponent;
import com.tokopedia.home.account.di.component.DaggerAccountHomeComponent;

/**
 * @author okasurya on 7/30/18.
 */
public class AccountHomeInjectionImpl implements AccountHomeInjection {
    private AccountHomeComponent accountHomeComponent;

    @Override
    public AccountHomeComponent getAccountHomeComponent(BaseAppComponent baseAppComponent) {
        if(accountHomeComponent == null) {
            accountHomeComponent = DaggerAccountHomeComponent.builder().baseAppComponent(baseAppComponent).build();
        }
        return accountHomeComponent;
    }

    @Override
    public void setAccountHomeComponent(AccountHomeComponent accountHomeComponent) {
        this.accountHomeComponent = accountHomeComponent;
    }
}
