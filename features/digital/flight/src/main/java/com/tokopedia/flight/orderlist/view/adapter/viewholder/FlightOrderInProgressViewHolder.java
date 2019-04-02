package com.tokopedia.flight.orderlist.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapter;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderInProcessViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class FlightOrderInProgressViewHolder extends FlightOrderBaseViewHolder<FlightOrderInProcessViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_flight_order_in_progress;
    private final FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener;

    private AppCompatTextView tvTitle;
    private AppCompatTextView tvOrderDate;
    private AppCompatTextView tvOrderId;
    private AppCompatTextView tvDepartureCity;
    private AppCompatTextView tvArrivalCity;
    private AppCompatTextView tvRebooking;
    private FlightOrderInProcessViewModel item;

    public FlightOrderInProgressViewHolder(View itemView, FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener) {
        super(itemView);
        this.adapterInteractionListener = adapterInteractionListener;
        findViews(itemView);
    }

    private void findViews(View view) {
        tvTitle = (AppCompatTextView) view.findViewById(R.id.tv_title);
        tvOrderDate = (AppCompatTextView) view.findViewById(R.id.tv_order_date);
        tvOrderId = (AppCompatTextView) view.findViewById(R.id.tv_order_id);
        tvDepartureCity = (AppCompatTextView) view.findViewById(R.id.tv_departure_city);
        tvArrivalCity = (AppCompatTextView) view.findViewById(R.id.tv_arrival_city);
        tvRebooking = (AppCompatTextView) view.findViewById(R.id.tv_order_detail);
        tvRebooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailOptionClicked();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetailOptionClicked();
            }
        });
    }


    @Override
    public void bind(FlightOrderInProcessViewModel element) {
        this.item = element;
        tvTitle.setText(element.getTitle());
        tvOrderDate.setText(FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.FORMAT_DATE, element.getCreateTime()));
        tvOrderId.setText(String.format("%s %s", itemView.getContext().getString(R.string.flight_order_order_id_prefix), element.getId()));
        if (element.getOrderJourney().size() > 0) {
            renderArrow(element.getOrderJourney());
            FlightOrderJourney orderJourney = element.getOrderJourney().get(0);
            tvDepartureCity.setText(getAirportTextForView(
                    orderJourney.getDepartureCity()));
            tvArrivalCity.setText(getAirportTextForView(
                    orderJourney.getArrivalCity()));
            renderDepartureSchedule(element.getOrderJourney());
        }
    }

    @Override
    protected void onHelpOptionClicked() {
        adapterInteractionListener.onHelpOptionClicked(item.getId(), item.getStatus());
    }

    @Override
    protected void onDetailOptionClicked() {
        if (item.getOrderJourney().size() == 1) {
            FlightOrderDetailPassData passData = new FlightOrderDetailPassData();
            passData.setOrderId(item.getId());
            FlightOrderJourney orderJourney = item.getOrderJourney().get(0);
            passData.setDepartureAiportId(orderJourney.getDepartureAiportId());
            passData.setDepartureCity(orderJourney.getDepartureCity());
            passData.setDepartureTime(orderJourney.getDepartureTime());
            passData.setArrivalAirportId(orderJourney.getArrivalAirportId());
            passData.setArrivalCity(orderJourney.getArrivalCity());
            passData.setArrivalTime(orderJourney.getArrivalTime());
            passData.setStatus(item.getStatus());
            adapterInteractionListener.onDetailOrderClicked(passData);
        } else {
            adapterInteractionListener.onDetailOrderClicked(item.getId());
        }
    }
}