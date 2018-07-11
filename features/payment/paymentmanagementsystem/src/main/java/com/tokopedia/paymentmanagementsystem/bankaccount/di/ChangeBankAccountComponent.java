package com.tokopedia.paymentmanagementsystem.bankaccount.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.paymentmanagementsystem.bankaccount.view.ChangeBankAccountFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@ChangeBankAccountScope
@Component(modules = ChangeBankAccountModule.class, dependencies = BaseAppComponent.class)
public interface ChangeBankAccountComponent {
    void inject(ChangeBankAccountFragment changeBankAccountFragment);
}
