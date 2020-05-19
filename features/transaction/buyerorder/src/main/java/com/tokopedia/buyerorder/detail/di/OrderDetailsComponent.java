package com.tokopedia.buyerorder.detail.di;

/**
 * Created by baghira on 10/05/18.
 */

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.transaction.orders.orderdetails.view.activity.SeeInvoiceActivity;
import com.tokopedia.transaction.orders.orderlist.di.OrderListModuleScope;

import dagger.Component;


@OrderListModuleScope
@Component(dependencies = {BaseAppComponent.class}, modules = OrderListDetailModule.class)
public interface OrderDetailsComponent {
    void inject(OrderListDetailFragment orderListDetailFragment);

    void inject(OmsDetailFragment omsDetailFragment);

    void inject(SeeInvoiceActivity seeInvoiceActivity);

    void inject(MarketPlaceDetailFragment marketPlaceDetailFragment);
}