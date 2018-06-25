package com.tokopedia.paymentmanagementsystem.paymentlist.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.paymentmanagementsystem.paymentlist.view.fragment.PaymentListFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@Component(modules = PaymentListModule.class, dependencies = BaseAppComponent.class)
public interface PaymentListComponent {
    void inject(PaymentListFragment paymentListFragment);
}
