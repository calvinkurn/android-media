package com.tokopedia.flight.orderlist.view.adapter.viewholder;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.flight.orderlist.R;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapter;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.ticker.TickerCallback;
import com.tokopedia.utils.date.DateUtil;

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
    private Ticker warningTicker;

    public FlightOrderSuccessViewHolder(FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener, View itemView) {
        super(itemView);
        this.adapterInteractionListener = adapterInteractionListener;
        findViews(itemView);
    }

    private void findViews(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        tvOrderDate = view.findViewById(R.id.tv_order_date);
        tvOrderId = view.findViewById(R.id.tv_order_id);
        tvDepartureCity = view.findViewById(R.id.tv_departure_city);
        tvArrivalCity = view.findViewById(R.id.tv_arrival_city);
        tvMainButton = view.findViewById(R.id.tv_main_button);
        warningTicker = view.findViewById(R.id.cancellation_warning);

        view.setOnClickListener(v -> onDetailOptionClicked());
    }


    @Override
    public void bind(final FlightOrderSuccessViewModel element) {
        this.item = element;
        tvTitle.setText(element.getTitle());
        tvOrderDate.setText(DateUtil.formatToUi(element.getCreateTime()));
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

        tvMainButton.setOnClickListener(view -> {
            if (adapterInteractionListener != null) {
                adapterInteractionListener.onDownloadETicket(item.getId());
            }
        });

        renderCancellationStatus(element);
    }


    @Override
    protected void onHelpOptionClicked() {
        adapterInteractionListener.onHelpOptionClicked(item.getContactUsUrl());
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
        if (element.getCancellationInfo().length() > 0) {
            showCancellationStatus(element.getCancellationInfo());
        } else {
            hideCancellationStatus();
        }
    }

    private void showCancellationStatus(String cancellationInfo) {
        warningTicker.setHtmlDescription(cancellationInfo);
        warningTicker.setDescriptionClickEvent(new TickerCallback() {
            @Override
            public void onDescriptionViewClick(CharSequence charSequence) {
                RouteManager.route(itemView.getContext(), charSequence.toString());
            }

            @Override
            public void onDismiss() {

            }
        });
        warningTicker.setVisibility(View.VISIBLE);
    }

    private void hideCancellationStatus() {
        warningTicker.setVisibility(View.GONE);
    }

}
