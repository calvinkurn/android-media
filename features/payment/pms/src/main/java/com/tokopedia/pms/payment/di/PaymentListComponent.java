package com.tokopedia.pms.payment.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.pms.payment.view.fragment.PaymentListFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

@PaymentListScope
@Component(modules = PaymentListModule.class, dependencies = BaseAppComponent.class)
public interface PaymentListComponent {
    void inject(PaymentListFragment paymentListFragment);
}
