package com.tokopedia.flight.orderlist.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.constant.FlightCancellationStatus;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapter;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;

import java.util.List;

/**
 * @author by alvarisi on 12/12/17.
 */

public class FlightOrderSuccessViewHolder extends FlightOrderBaseViewHolder<FlightOrderSuccessViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_flight_order_success;
    private final FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener;

    private AppCompatTextView tvTitle;
    private AppCompatTextView tvOrderDate;
    private AppCompatTextView tvOrderId;
    private AppCompatTextView tvDepartureCity;
    private AppCompatTextView tvMainButton;
    private AppCompatTextView tvArrivalCity;
    private FlightOrderSuccessViewModel item;
    private LinearLayout containerTicketCancellationStatus;
    private AppCompatTextView tvTicketCancellationStatus;

    public FlightOrderSuccessViewHolder(FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener, View itemView) {
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
        tvMainButton = (AppCompatTextView) view.findViewById(R.id.tv_main_button);
        containerTicketCancellationStatus = view.findViewById(R.id.container_cancellation_warning);
        tvTicketCancellationStatus = view.findViewById(R.id.tv_cancellation_ticket_status);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetailOptionClicked();
            }
        });
    }


    @Override
    public void bind(final FlightOrderSuccessViewModel element) {
        this.item = element;
        tvTitle.setText(element.getTitle());
        tvOrderDate.setText(FlightDateUtil.formatToUi(element.getCreateTime()));
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

        tvMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapterInteractionListener != null) {
                    adapterInteractionListener.onDownloadETicket(item.getId());
                }
            }
        });

        renderCancellationStatus(element);
    }


    @Override
    protected void onHelpOptionClicked() {
        adapterInteractionListener.onHelpOptionClicked(item.getId(), item.getStatus());
    }

    private void onCancelOptionClicked() {

        adapterInteractionListener.onCancelOptionClicked(item);
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

    @Override
    protected void showPopup(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_flight_order_success, popup.getMenu());
        popup.setOnMenuItemClickListener(new OnMenuPopupClicked() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_cancel) {
                    onCancelOptionClicked();
                    return true;
                }
                return super.onMenuItemClick(item);
            }
        });

        popup.show();
    }

    private void renderCancellationStatus(FlightOrderSuccessViewModel element) {
        if (element.getCancellations().size() > 0) {
            countCancellationStatus(element.getCancellations());
        } else {
            hideCancellationStatus();
        }
    }

    private void showCancellationStatus() {
        tvTicketCancellationStatus.setText(getString(R.string.flight_cancellation_ticket_status));
        containerTicketCancellationStatus.setVisibility(View.VISIBLE);
    }

    private void showCancellationStatusInProgress(int numberOfProcess) {
        tvTicketCancellationStatus.setText(String.format(getString(
                R.string.flight_cancellation_ticket_status_in_progress), numberOfProcess));
        containerTicketCancellationStatus.setVisibility(View.VISIBLE);
    }

    private void hideCancellationStatus() {
        containerTicketCancellationStatus.setVisibility(View.GONE);
    }

    private void countCancellationStatus(List<CancellationEntity> cancellationEntityList) {
        int numberOfProgress = 0;
        for (CancellationEntity item : cancellationEntityList) {
            if (item.getStatus() == FlightCancellationStatus.PENDING || item.getStatus() == FlightCancellationStatus.REQUESTED) {
                numberOfProgress++;
            }
        }

        if (numberOfProgress > 0) {
            showCancellationStatusInProgress(numberOfProgress);
        } else {
            showCancellationStatus();
        }
    }

}
