package com.tokopedia.buyerorder.detail.di;

/**
 * Created by baghira on 10/05/18.
 */

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.buyerorder.detail.view.activity.SeeInvoiceActivity;
import com.tokopedia.buyerorder.detail.view.fragment.OmsDetailFragment;
import com.tokopedia.buyerorder.detail.view.fragment.OrderListDetailFragment;

import dagger.Component;


@OrderDetailScope @Component(dependencies = {BaseAppComponent.class}, modules = {OrderDetailModule.class})
public interface OrderDetailsComponent {
    void inject(OrderListDetailFragment orderListDetailFragment);

    void inject(OmsDetailFragment omsDetailFragment);

    void inject(SeeInvoiceActivity seeInvoiceActivity);
}