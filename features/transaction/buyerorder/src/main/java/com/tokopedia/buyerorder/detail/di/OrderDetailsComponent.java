package com.tokopedia.buyerorder.detail.di;

/**
 * Created by baghira on 10/05/18.
 */

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.buyerorder.detail.view.activity.SeeInvoiceActivity;
import com.tokopedia.buyerorder.detail.view.fragment.BuyerRequestCancelFragment;
import com.tokopedia.buyerorder.detail.view.fragment.MarketPlaceDetailFragment;
import com.tokopedia.buyerorder.detail.view.fragment.OmsDetailFragment;
import com.tokopedia.buyerorder.detail.view.fragment.OrderListDetailFragment;
import com.tokopedia.buyerorder.list.di.OrderListModuleScope;

import dagger.Component;


@OrderListModuleScope
@Component(dependencies = {BaseAppComponent.class}, modules = {OrderListDetailModule.class,
        GetCancellationReasonModule.class, GetCancellationReasonViewModelModule.class})
public interface OrderDetailsComponent {
    void inject(OrderListDetailFragment orderListDetailFragment);

    void inject(OmsDetailFragment omsDetailFragment);

    void inject(SeeInvoiceActivity seeInvoiceActivity);

    void inject(MarketPlaceDetailFragment marketPlaceDetailFragment);

    void inject(BuyerRequestCancelFragment buyerRequestCancelFragment);
}