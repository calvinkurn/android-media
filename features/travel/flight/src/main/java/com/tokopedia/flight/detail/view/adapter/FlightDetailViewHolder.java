package com.tokopedia.flight.detail.view.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel;
import com.tokopedia.flight.search.presentation.util.FlightSearchCache;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifyprinciples.Typography;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailViewHolder extends AbstractViewHolder<FlightDetailRouteModel> {
    @LayoutRes
    public static final int LAYOUT = com.tokopedia.flight.R.layout.item_flight_detail;

    private FlightSearchCache flightSearchCache;

    private ImageView imageAirline;
    private TextView airlineName;
    private TextView stopOverTextView;
    private TextView airlineCode;
    private Typography airlineOperatingBy;
    private TextView refundableInfo;
    private TextView departureTime;
    private TextView departureDate;
    private ImageView departureCircleImage;
    private TextView departureAirportName;
    private TextView departureAirportDesc;
    private TextView departureTerminal;
    private TextView flightTime;
    private TextView arrivalTime;
    private TextView arrivalDate;
    private ImageView arrivalCircleImage;
    private TextView arrivalAirportName;
    private TextView arrivalAirportDesc;
    private TextView arrivalTerminal;
    private Ticker transitInfo;
    private View containerPNR;
    private TextView pnrCode;
    private ImageView copyPnr;
    private FlightDetailAdapterTypeFactory.OnFlightDetailListener onFlightDetailListener;
    private boolean isShowRefundableTag;

    public FlightDetailViewHolder(View itemView, FlightDetailAdapterTypeFactory.OnFlightDetailListener onFlightDetailListener,
                                  boolean isShowRefundableTag) {
        super(itemView);
        imageAirline = itemView.findViewById(R.id.airline_icon);
        airlineName = itemView.findViewById(R.id.airline_name);
        airlineCode = itemView.findViewById(R.id.airline_code);
        airlineOperatingBy = itemView.findViewById(R.id.airline_operating_by);
        refundableInfo = itemView.findViewById(R.id.airline_refundable_info);
        departureTime = itemView.findViewById(R.id.departure_time);
        departureDate = itemView.findViewById(R.id.departure_date);
        departureCircleImage = itemView.findViewById(R.id.departure_time_circle);
        departureAirportName = itemView.findViewById(R.id.departure_airport_name);
        departureAirportDesc = itemView.findViewById(R.id.departure_desc_airport_name);
        departureTerminal = itemView.findViewById(R.id.departure_terminal);
        flightTime = itemView.findViewById(R.id.flight_time);
        arrivalTime = itemView.findViewById(R.id.arrival_time);
        arrivalDate = itemView.findViewById(R.id.arrival_date);
        arrivalCircleImage = itemView.findViewById(R.id.arrival_time_circle);
        arrivalAirportName = itemView.findViewById(R.id.arrival_airport_name);
        arrivalAirportDesc = itemView.findViewById(R.id.arrival_desc_airport_name);
        arrivalTerminal = itemView.findViewById(R.id.arrival_terminal);
        transitInfo = itemView.findViewById(R.id.transit_info);
        containerPNR = itemView.findViewById(R.id.container_pnr);
        pnrCode = itemView.findViewById(R.id.pnr_code);
        copyPnr = itemView.findViewById(R.id.copy_pnr);
        stopOverTextView = itemView.findViewById(R.id.tv_flight_stop_over);
        this.onFlightDetailListener = onFlightDetailListener;
        this.isShowRefundableTag = isShowRefundableTag;

        flightSearchCache = new FlightSearchCache(itemView.getContext());
    }

    @Override
    public void bind(FlightDetailRouteModel route) {
        airlineName.setText(route.getAirlineName());
        airlineCode.setText(String.format("%s - %s", route.getAirlineCode(), route.getFlightNumber()));
        setRefundableInfo(route);
        departureTime.setText(FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.FORMAT_TIME_DETAIL, route.getDepartureTimestamp()));
        departureDate.setText(FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.FORMAT_DATE_LOCAL_DETAIL, route.getDepartureTimestamp()));
        setColorCircle();
        setDepartureInfo(route);

        flightTime.setText(route.getDuration());
        arrivalTime.setText(FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.FORMAT_TIME_DETAIL, route.getArrivalTimestamp()));
        arrivalDate.setText(FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.FORMAT_DATE_LOCAL_DETAIL, route.getArrivalTimestamp()));
        setArrivalInfo(route);
        setPNR(route.getPnr());
        ImageHandler.loadImageWithoutPlaceholder(imageAirline, route.getAirlineLogo(),
                ContextCompat.getDrawable(itemView.getContext(), com.tokopedia.flight.R.drawable.flight_ic_airline_default)
        );
        if (onFlightDetailListener != null) {
            bindLastPosition(onFlightDetailListener.getItemCount() == getAdapterPosition());
            bindTransitInfo(onFlightDetailListener.getItemCount());
        }

        if (route.getStopOver() > 0) {
            if (route.getStopOverDetail().size() > 0) {
                stopOverTextView.setVisibility(View.VISIBLE);
                if (route.getStopOverDetail().size() < route.getStopOver()) {
                    stopOverTextView.setText(String.format(getString(R.string.flight_detail_total_stop_over_label), route.getStopOver()));
                } else {
                    stopOverTextView.setText(getString(R.string.flight_detail_transit_stop_over_label));
                    stopOverTextView.append(" ");
                    stopOverTextView.append(TextUtils.join(", ", route.getStopOverDetail()));
                }
            } else {
                stopOverTextView.setVisibility(View.GONE);
            }
        } else {
            stopOverTextView.setVisibility(View.GONE);
        }

        if (route.getDepartureTerminal() != null && route.getDepartureTerminal().length() > 0) {
            departureTerminal.setText(getString(com.tokopedia.flight.R.string.flight_terminal_info, route.getDepartureTerminal()));
            departureTerminal.setVisibility(View.VISIBLE);
        } else {
            departureTerminal.setVisibility(View.GONE);
        }

        if (route.getArrivalTerminal() != null && route.getArrivalTerminal().length() > 0) {
            arrivalTerminal.setText(getString(com.tokopedia.flight.R.string.flight_terminal_info, route.getArrivalTerminal()));
            arrivalTerminal.setVisibility(View.VISIBLE);
        } else {
            arrivalTerminal.setVisibility(View.GONE);
        }

        if (route.getOperatingAirline() != null && route.getOperatingAirline().length() > 0) {
            airlineOperatingBy.setText(getString(R.string.flight_detail_operating_by, route.getOperatingAirline()));
            airlineOperatingBy.setVisibility(View.VISIBLE);
        } else {
            airlineOperatingBy.setVisibility(View.GONE);
        }
    }

    private void setDepartureInfo(FlightDetailRouteModel route) {
        if (!TextUtils.isEmpty(route.getDepartureAirportCity())) {
            departureAirportName.setText(String.format("%s (%s)", route.getDepartureAirportCity(), route.getDepartureAirportCode()));
            departureAirportDesc.setText(route.getDepartureAirportName());
        } else {
            departureAirportName.setText(route.getDepartureAirportCode());
            departureAirportDesc.setText("");
        }
    }

    private void setArrivalInfo(FlightDetailRouteModel route) {
        String transitTag = "";
        if (!TextUtils.isEmpty(flightSearchCache.getInternationalTransitTag())) {
            transitTag = "\n" + flightSearchCache.getInternationalTransitTag();
        }

        String arrivalAirport = "";

        if (!TextUtils.isEmpty(route.getArrivalAirportCity())) {
            arrivalAirportDesc.setText(route.getArrivalAirportName());
            arrivalAirportName.setText(String.format("%s (%s)", route.getArrivalAirportCity(), route.getArrivalAirportCode()));
            arrivalAirport = route.getArrivalAirportCity();
        } else {
            arrivalAirportName.setText(route.getArrivalAirportCode());
            arrivalAirportDesc.setText("");
            arrivalAirport = route.getArrivalAirportCode();
        }

        if (route.getLayover().length() > 0) {
            transitInfo.setTextDescription(itemView.getContext().getString(R.string.flight_label_transit_with_duration,
                    arrivalAirport, route.getLayover(), transitTag));
        } else {
            transitInfo.setTextDescription(itemView.getContext().getString(R.string.flight_label_transit_without_duration,
                    arrivalAirport, transitTag));
        }
    }

    private void setPNR(String pnr) {
        if (!TextUtils.isEmpty(pnr)) {
            containerPNR.setVisibility(View.VISIBLE);
            pnrCode.setText(pnr);
            copyPnr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(getString(com.tokopedia.flight.R.string.flight_label_order_id), pnrCode.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                        @Override
                        public void onPrimaryClipChanged() {
                            Toast.makeText(itemView.getContext(), com.tokopedia.flight.R.string.flight_label_copy_clipboard, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            containerPNR.setVisibility(View.GONE);
        }
    }

    private void setRefundableInfo(FlightDetailRouteModel route) {
        if (route.isRefundable()) {
            refundableInfo.setText(com.tokopedia.flight.R.string.flight_label_refundable_info);
        } else {
            refundableInfo.setText(com.tokopedia.flight.R.string.flight_label_non_refundable_info);
        }
        if (isShowRefundableTag) refundableInfo.setVisibility(View.VISIBLE);
        else refundableInfo.setVisibility(View.GONE);
    }

    //set color circle to green if position holder is on first index
    private void setColorCircle() {
        if (getAdapterPosition() == 0) {
            departureCircleImage.setEnabled(true);
        }
    }

    //set color circle to red if position holder is on last index
    public void bindLastPosition(boolean lastItemPosition) {
        if (lastItemPosition) {
            arrivalCircleImage.setEnabled(false);
        }
    }

    //set visible transit info if flight have transit and position holder is on first index
    public void bindTransitInfo(int sizeInfo) {
        if (sizeInfo > 0 && getAdapterPosition() < sizeInfo - 1) {
            transitInfo.setVisibility(View.VISIBLE);
        } else {
            transitInfo.setVisibility(View.GONE);
        }
    }
}
