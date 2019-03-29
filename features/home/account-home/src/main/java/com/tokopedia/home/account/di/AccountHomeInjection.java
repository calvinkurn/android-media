package com.tokopedia.home.account.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.home.account.di.component.AccountHomeComponent;

/**
 * @author okasurya on 7/30/18.
 */
public interface AccountHomeInjection {
    AccountHomeComponent getAccountHomeComponent(BaseAppComponent baseAppComponent);

    void setAccountHomeComponent(AccountHomeComponent accountHomeComponent);
}
