package com.tokopedia.flight.orderlist.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderFailedViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderInProcessViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderRefundViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderWaitingForPaymentViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public interface FlightOrderTypeFactory extends AdapterTypeFactory {
    int type(FlightOrderInProcessViewModel inProcessViewModel);

    int type(FlightOrderWaitingForPaymentViewModel inProcessViewModel);

    int type(FlightOrderRefundViewModel refundViewModel);

    int type(FlightOrderSuccessViewModel successViewModel);

    int type(FlightOrderFailedViewModel failedViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
