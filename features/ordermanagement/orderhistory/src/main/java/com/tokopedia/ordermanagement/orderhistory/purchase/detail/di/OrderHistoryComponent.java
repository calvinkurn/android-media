package com.tokopedia.ordermanagement.orderhistory.purchase.detail.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.activity.OrderHistoryActivity;

import dagger.Component;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

@OrderHistoryScope
@Component(modules = OrderHistoryModule.class, dependencies = BaseAppComponent.class)
public interface OrderHistoryComponent {
    void inject(OrderHistoryActivity activity);
}
