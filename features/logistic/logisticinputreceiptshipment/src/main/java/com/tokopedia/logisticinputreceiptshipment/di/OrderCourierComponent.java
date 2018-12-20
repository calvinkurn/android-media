package com.tokopedia.logisticinputreceiptshipment.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.logisticdata.data.module.qualifier.OrderCourierScope;
import com.tokopedia.logisticinputreceiptshipment.view.confirmshipment.ConfirmShippingActivity;

import dagger.Component;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

@OrderCourierScope
@Component(modules = OrderCourierModule.class, dependencies = BaseAppComponent.class)
public interface OrderCourierComponent {
    void inject(ConfirmShippingActivity activity);
}
