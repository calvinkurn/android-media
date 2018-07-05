package com.tokopedia.paymentmanagementsystem.bankdestinationlist.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.view.fragment.BankDestinationFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@BankDestinationScope
@Component(modules = BankDestinationModule.class, dependencies = BaseAppComponent.class)
public interface BankDestinationComponent {
    void inject(BankDestinationFragment bankDestinationFragment);
}
