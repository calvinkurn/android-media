package com.tokopedia.logisticorder.view.shipping_confirmation.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticCommon.data.module.qualifier.OrderCourierScope;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.confirmshipment.ConfirmShippingActivity;

import dagger.Component;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

@OrderCourierScope
@Component(modules = OrderCourierModule.class, dependencies = BaseAppComponent.class)
public interface OrderCourierComponent {
    void inject(ConfirmShippingActivity activity);
}
