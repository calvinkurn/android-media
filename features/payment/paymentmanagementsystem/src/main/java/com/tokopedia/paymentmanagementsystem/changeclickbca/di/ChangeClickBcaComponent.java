package com.tokopedia.paymentmanagementsystem.changeclickbca.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.paymentmanagementsystem.changeclickbca.view.ChangeClickBcaFragment;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.fragment.PaymentListFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@ChangeClickBcaScope
@Component(modules = ChangeClickBcaModule.class, dependencies = BaseAppComponent.class)
public interface ChangeClickBcaComponent {
    void inject(ChangeClickBcaFragment changeClickBcaFragment);
}
