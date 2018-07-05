package com.tokopedia.paymentmanagementsystem.changebankaccount.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.paymentmanagementsystem.changebankaccount.view.ChangeBankAccountFragment;
import com.tokopedia.paymentmanagementsystem.changeclickbca.view.ChangeClickBcaFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@ChangeBankAccountScope
@Component(modules = ChangeBankAccountModule.class, dependencies = BaseAppComponent.class)
public interface ChangeBankAccountComponent {
    void inject(ChangeBankAccountFragment changeBankAccountFragment);
}
