package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListPassengerViewModel;
import com.tokopedia.flight.common.util.FlightAmenityType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by furqan on 04/05/18.
 */

public class FlightCancellationDetailPassengerViewHolder extends AbstractViewHolder<FlightCancellationListPassengerViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_review_passenger;
    private CancellationPassengerDetailAdapter cancellationPassengerDetailAdapter;
    private TextView passengerNumber;
    private TextView passengerName;
    private TextView passengerCategory;
    private RecyclerView recyclerViewPassengerDetail;

    public FlightCancellationDetailPassengerViewHolder(View itemView) {
        super(itemView);

        passengerNumber = (TextView) itemView.findViewById(R.id.passenger_number);
        passengerName = (TextView) itemView.findViewById(R.id.passenger_name);
        passengerCategory = (TextView) itemView.findViewById(R.id.passenger_category);
        recyclerViewPassengerDetail = (RecyclerView) itemView.findViewById(R.id.recycler_view_passenger_detail);

        recyclerViewPassengerDetail.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        cancellationPassengerDetailAdapter = new CancellationPassengerDetailAdapter();
        recyclerViewPassengerDetail.setAdapter(cancellationPassengerDetailAdapter);
    }

    @Override
    public void bind(FlightCancellationListPassengerViewModel element) {
        passengerNumber.setText(String.format("%d.", getAdapterPosition() + 1));
        passengerName.setText(element.getFirstName() + " " + element.getLastName());
        passengerCategory.setText(getPassengerType(element.getType()));

        if (element.getAmenities().size() > 0) {
            recyclerViewPassengerDetail.setVisibility(View.VISIBLE);
            cancellationPassengerDetailAdapter.addData(transformToSimpleModelPassenger(element));
        } else {
            recyclerViewPassengerDetail.setVisibility(View.GONE);
        }
    }

    private String getPassengerType(int flightDetailPassenger) {
        switch (flightDetailPassenger) {
            case FlightBookingPassenger.ADULT:
                return getString(R.string.flight_label_adult_review);
            case FlightBookingPassenger.CHILDREN:
                return getString(R.string.flight_label_child_review);
            case FlightBookingPassenger.INFANT:
                return getString(R.string.flight_label_infant_review);
            default:
                return "";
        }
    }

    private List<SimpleViewModel> transformToSimpleModelPassenger(FlightCancellationListPassengerViewModel passengerViewModel) {
        List<SimpleViewModel> simpleViewModels = new ArrayList<>();
        for (FlightBookingAmenityViewModel flightBookingAmenityViewModel : passengerViewModel.getAmenities()) {
            if (flightBookingAmenityViewModel.getDepartureId().equals(passengerViewModel.getDepartureAiportId()) &&
                    flightBookingAmenityViewModel.getArrivalId().equals(passengerViewModel.getArrivalAirportId())) {
                SimpleViewModel simpleViewModel = new SimpleViewModel();
                simpleViewModel.setDescription(generateLabelPassenger(String.valueOf(flightBookingAmenityViewModel.getAmenityType()),
                        flightBookingAmenityViewModel.getDepartureId(),
                        flightBookingAmenityViewModel.getArrivalId()));
                simpleViewModel.setLabel(flightBookingAmenityViewModel.getTitle());
                simpleViewModels.add(simpleViewModel);
            }
        }
        return simpleViewModels;
    }

    private String generateLabelPassenger(String type, String departureId, String arrivalId) {
        switch (type) {
            case FlightAmenityType.LUGGAGE:
                return String.format(getString(R.string.flight_luggage_detail_order), departureId, arrivalId);
            case FlightAmenityType.MEAL:
                return String.format(getString(R.string.flight_meal_detail_order), departureId, arrivalId);
            default:
                return "";
        }
    }

    private class CancellationPassengerDetailAdapter extends RecyclerView.Adapter<PassengerDetailViewHolder> {
        List<SimpleViewModel> infoList;

        public CancellationPassengerDetailAdapter() {
            infoList = new ArrayList<>();
        }

        @Override
        public PassengerDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight_detail_passenger_info, parent, false);
            return new PassengerDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PassengerDetailViewHolder holder, int position) {
            holder.bindData(infoList.get(position));
        }

        @Override
        public int getItemCount() {
            return infoList.size();
        }

        public void addData(List<SimpleViewModel> infos) {
            infoList.clear();
            infoList.addAll(infos);
            notifyDataSetChanged();
        }
    }

    private class PassengerDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView titleInfo;
        private TextView descInfo;

        public PassengerDetailViewHolder(View itemView) {
            super(itemView);
            titleInfo = (TextView) itemView.findViewById(R.id.title_info);
            descInfo = (TextView) itemView.findViewById(R.id.desc_info);
        }

        public void bindData(SimpleViewModel info) {
            titleInfo.setText(info.getDescription());
            descInfo.setText(info.getLabel().trim());
        }
    }
}
