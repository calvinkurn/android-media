package com.tokopedia.flight.orderlist.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.orderlist.view.adapter.viewholder.FlightOrderFailedViewHolder;
import com.tokopedia.flight.orderlist.view.adapter.viewholder.FlightOrderInProgressViewHolder;
import com.tokopedia.flight.orderlist.view.adapter.viewholder.FlightOrderRefundViewHolder;
import com.tokopedia.flight.orderlist.view.adapter.viewholder.FlightOrderSuccessViewHolder;
import com.tokopedia.flight.orderlist.view.adapter.viewholder.FlightOrderWaitingForPaymentViewHolder;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderFailedViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderInProcessViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderRefundViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderWaitingForPaymentViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class FlightOrderAdapterTypeFactory extends BaseAdapterTypeFactory implements FlightOrderTypeFactory {
    private FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener;

    public FlightOrderAdapterTypeFactory(FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener) {
        this.adapterInteractionListener = adapterInteractionListener;
    }

    @Override
    public int type(FlightOrderInProcessViewModel inProcessViewModel) {
        return FlightOrderInProgressViewHolder.LAYOUT;
    }

    @Override
    public int type(FlightOrderWaitingForPaymentViewModel inProcessViewModel) {
        return FlightOrderWaitingForPaymentViewHolder.LAYOUT;
    }

    @Override
    public int type(FlightOrderRefundViewModel refundViewModel) {
        return FlightOrderRefundViewHolder.LAYOUT;
    }

    @Override
    public int type(FlightOrderSuccessViewModel successViewModel) {
        return FlightOrderSuccessViewHolder.LAYOUT;
    }

    @Override
    public int type(FlightOrderFailedViewModel failedViewModel) {
        return FlightOrderFailedViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        if (viewType == FlightOrderSuccessViewHolder.LAYOUT) {
            viewHolder = new FlightOrderSuccessViewHolder(adapterInteractionListener, view);
        } else if (viewType == FlightOrderFailedViewHolder.LAYOUT) {
            viewHolder = new FlightOrderFailedViewHolder(view, adapterInteractionListener);
        } else if (viewType == FlightOrderInProgressViewHolder.LAYOUT) {
            viewHolder = new FlightOrderInProgressViewHolder(view, adapterInteractionListener);
        } else if (viewType == FlightOrderWaitingForPaymentViewHolder.LAYOUT) {
            viewHolder = new FlightOrderWaitingForPaymentViewHolder(view, adapterInteractionListener);
        } else if (viewType == FlightOrderRefundViewHolder.LAYOUT) {
            viewHolder = new FlightOrderRefundViewHolder(view, adapterInteractionListener);
        } else {
            viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }
}
