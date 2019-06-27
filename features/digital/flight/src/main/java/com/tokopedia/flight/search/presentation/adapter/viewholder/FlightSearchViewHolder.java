package com.tokopedia.flight.search.presentation.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.view.FlightMultiAirlineView;
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory;
import com.tokopedia.flight.search.presentation.model.Duration;
import com.tokopedia.flight.search.presentation.model.FlightAirlineViewModel;
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel;
import com.tokopedia.flight.search.util.DurationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by furqan on 02/10/18.
 */

public class FlightSearchViewHolder extends AbstractViewHolder<FlightJourneyViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_search;

    private LinearLayout containerLayout;
    private TextView tvDeparture;
    private TextView tvArrival;
    private TextView tvAirline;
    private FlightMultiAirlineView flightMultiAirlineView;
    private TextView tvPrice;
    private TextView tvDuration;
    private TextView airlineRefundableInfo;
    private TextView savingPrice;
    private TextView arrivalAddDay;
    private TextView discountTag;
    private TextView bestPairingTag;
    private View containerDetail;

    private FlightSearchAdapterTypeFactory.OnFlightSearchListener onFlightSearchListener;

    public FlightSearchViewHolder(View itemView, FlightSearchAdapterTypeFactory.OnFlightSearchListener onFlightSearchListener) {
        super(itemView);
        containerLayout = itemView.findViewById(R.id.container_layout);
        tvDeparture = itemView.findViewById(R.id.departure_time);
        tvArrival = itemView.findViewById(R.id.arrival_time);
        tvAirline = itemView.findViewById(R.id.tv_airline);
        flightMultiAirlineView = itemView.findViewById(R.id.view_multi_airline);
        airlineRefundableInfo = itemView.findViewById(R.id.airline_refundable_info);
        tvPrice = itemView.findViewById(R.id.total_price);
        tvDuration = itemView.findViewById(R.id.flight_time);
        savingPrice = itemView.findViewById(R.id.saving_price);
        arrivalAddDay = itemView.findViewById(R.id.arrival_add_day);
        discountTag = itemView.findViewById(R.id.tv_discount_tag);
        bestPairingTag = itemView.findViewById(R.id.tv_best_pairing_tag);
        containerDetail = itemView.findViewById(R.id.container_detail);
        this.onFlightSearchListener = onFlightSearchListener;
    }

    @Override
    public void bind(final FlightJourneyViewModel flightJourneyViewModel) {
        tvDeparture.setText(String.format("%s %s", flightJourneyViewModel.getDepartureTime(), flightJourneyViewModel.getDepartureAirport()));
        tvArrival.setText(String.format("%s %s", flightJourneyViewModel.getArrivalTime(), flightJourneyViewModel.getArrivalAirport()));
        tvPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(flightJourneyViewModel.getFare().getAdultNumeric()));
        setDuration(flightJourneyViewModel);
        setAirline(flightJourneyViewModel);
        View.OnClickListener detailClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFlightSearchListener.onDetailClicked(flightJourneyViewModel, getAdapterPosition());
            }
        };
        tvPrice.setOnClickListener(detailClickListener);
        containerDetail.setOnClickListener(detailClickListener);

        setRefundableInfo(flightJourneyViewModel);
        setSavingPrice(flightJourneyViewModel);
        setDiscountPriceTag(flightJourneyViewModel);
        setArrivalAddDay(flightJourneyViewModel);
        setBestPairingPrice(flightJourneyViewModel);
        itemView.setOnClickListener(view -> onFlightSearchListener.onItemClicked(flightJourneyViewModel, getAdapterPosition()));
        setMarginOnFirstItem();
    }

    private void setMarginOnFirstItem() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        if (getAdapterPosition() == 0) {
            params.setMargins(
                    itemView.getResources().getDimensionPixelSize(R.dimen.margin_16),
                    itemView.getResources().getDimensionPixelSize(R.dimen.margin_16),
                    itemView.getResources().getDimensionPixelSize(R.dimen.margin_16),
                    itemView.getResources().getDimensionPixelSize(R.dimen.margin_8)
            );
            containerLayout.setLayoutParams(params);
        } else {
            params.setMargins(
                    itemView.getResources().getDimensionPixelSize(R.dimen.margin_16),
                    itemView.getResources().getDimensionPixelSize(R.dimen.margin_8),
                    itemView.getResources().getDimensionPixelSize(R.dimen.margin_16),
                    itemView.getResources().getDimensionPixelSize(R.dimen.margin_8)
            );
            containerLayout.setLayoutParams(params);
        }
    }

    private void setArrivalAddDay(FlightJourneyViewModel flightJourneyViewModel) {
        if (flightJourneyViewModel.getAddDayArrival() > 0) {
            arrivalAddDay.setVisibility(View.VISIBLE);
            arrivalAddDay.setText(itemView.getContext().getString(
                    R.string.flight_label_duration_add_day, flightJourneyViewModel.getAddDayArrival()));
        } else {
            arrivalAddDay.setVisibility(View.GONE);
        }
    }

    void setDuration(FlightJourneyViewModel flightJourneyViewModel) {
        Duration duration = DurationUtil.convertFormMinute(flightJourneyViewModel.getDurationMinute());
        String durationString = DurationUtil.getReadableString(itemView.getContext(), duration);
        if (flightJourneyViewModel.getTotalTransit() > 0) {
            tvDuration.setText(itemView.getContext().getString(R.string.flight_label_duration_transit,
                    durationString, String.valueOf(flightJourneyViewModel.getTotalTransit())));
        } else {
            tvDuration.setText(itemView.getContext().getString(R.string.flight_label_duration_direct,
                    durationString));
        }
    }

    private void setSavingPrice(FlightJourneyViewModel flightJourneyViewModel) {

        if (flightJourneyViewModel.getBeforeTotal() != null &&
                flightJourneyViewModel.getBeforeTotal().length() > 0) {
            savingPrice.setVisibility(View.VISIBLE);
            savingPrice.setText(flightJourneyViewModel.getBeforeTotal());
        } else {
            savingPrice.setVisibility(View.GONE);
        }
    }

    private void setDiscountPriceTag(FlightJourneyViewModel flightJourneyViewModel) {
        if (flightJourneyViewModel.isShowSpecialPriceTag()) {
            discountTag.setVisibility(View.VISIBLE);
            discountTag.setText(flightJourneyViewModel.getSpecialTagText());
        } else {
            discountTag.setVisibility(View.GONE);
        }
    }

    private void setAirline(FlightJourneyViewModel flightJourneyViewModel) {
        if (flightJourneyViewModel.getAirlineDataList() != null &&
                flightJourneyViewModel.getAirlineDataList().size() > 1) {
            List<FlightAirlineViewModel> flightAirlineDBs = flightJourneyViewModel.getAirlineDataList();
            if (flightAirlineDBs != null && flightAirlineDBs.size() > 0) {
                List<String> airlineLogoList = new ArrayList<>();
                for (int i = 0, sizei = flightAirlineDBs.size(); i < sizei; i++) {
                    FlightAirlineViewModel flightAirlineDB = flightAirlineDBs.get(i);
                    airlineLogoList.add(flightAirlineDB.getLogo());
                }
                flightMultiAirlineView.setAirlineLogos(airlineLogoList);
            } else {
                flightMultiAirlineView.setAirlineLogos(null);
            }
            tvAirline.setText(R.string.flight_label_multi_maskapai);
        } else if (flightJourneyViewModel.getAirlineDataList() != null &&
                flightJourneyViewModel.getAirlineDataList().size() == 1) {
            flightMultiAirlineView.setAirlineLogo(flightJourneyViewModel.getAirlineDataList().get(0).getLogo());
            tvAirline.setText(flightJourneyViewModel.getAirlineDataList().get(0).getShortName());
        }
    }

    private void setRefundableInfo(FlightJourneyViewModel flightJourneyViewModel) {
        airlineRefundableInfo.setVisibility(View.VISIBLE);
        airlineRefundableInfo.setText(flightJourneyViewModel.isRefundable().getValueRes());
    }

    private void setBestPairingPrice(FlightJourneyViewModel flightJourneyViewModel) {
        if (flightJourneyViewModel.isBestPairing()) {
            bestPairingTag.setVisibility(View.VISIBLE);
            discountTag.setVisibility(View.GONE);
            tvPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(
                    flightJourneyViewModel.getFare().getAdultNumericCombo()));
        } else if (flightJourneyViewModel.getFare().getAdultNumericCombo() != 0) {
            bestPairingTag.setVisibility(View.GONE);
            tvPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(
                    flightJourneyViewModel.getFare().getAdultNumericCombo()));
        } else {
            bestPairingTag.setVisibility(View.GONE);
        }
    }
}
