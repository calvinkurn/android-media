package com.tokopedia.buyerorder.list.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.buyerorder.list.view.activity.OrderListActivity;
import com.tokopedia.buyerorder.list.view.fragment.OrderListFragment;

import dagger.Component;

/**
 * Created by baghira on 07/05/18.
 */

@OrderListModuleScope
@Component(dependencies = {BaseAppComponent.class}, modules = OrderListUseCaseModule.class)
public interface OrderListComponent {
    void inject(OrderListFragment orderListFragment);
    void inject(OrderListActivity orderListActivity);
}
