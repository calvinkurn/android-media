package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.ADULT;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.CHILDREN;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.INFANT;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationViewHolder extends AbstractViewHolder<FlightCancellationViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_cancellation;

    public interface FlightCancellationListener {
        void onPassengerChecked(FlightCancellationPassengerViewModel passengerViewModel, int position);

        void onPassengerUnchecked(FlightCancellationPassengerViewModel passengerViewModel, int position);

        boolean shouldCheckAll();

        boolean isChecked(FlightCancellationPassengerViewModel passengerViewModel);
    }

    interface FlightPassengerAdapterListener {
        void checkIfAllPassengerIsChecked();
    }

    private Context context;
    private TextView txtDepartureDetail;
    private TextView txtJourneyDetail;
    private TextView txtAirlineName;
    private TextView txtDuration;
    private VerticalRecyclerView verticalRecyclerView;
    private PassengerAdapter passengerAdapter;
    private CheckBox checkBoxFlight;
    private boolean isJourneyChecked = false;
    private boolean uncheckAllData = true;

    private FlightCancellationListener listener;

    public FlightCancellationViewHolder(View itemView, FlightCancellationListener flightCancellationListener) {
        super(itemView);

        context = itemView.getContext();
        listener = flightCancellationListener;

        txtDepartureDetail = itemView.findViewById(R.id.tv_departure_time_label);
        txtJourneyDetail = itemView.findViewById(R.id.tv_journey_detail_label);
        txtAirlineName = itemView.findViewById(R.id.airline_name);
        txtDuration = itemView.findViewById(R.id.duration);
        checkBoxFlight = itemView.findViewById(R.id.checkbox);

        verticalRecyclerView = itemView.findViewById(R.id.recycler_view_passenger);
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        passengerAdapter = new PassengerAdapter();
        verticalRecyclerView.setAdapter(passengerAdapter);

    }

    @Override
    public void bind(FlightCancellationViewModel element) {

        String departureCityAirportCode = (element.getFlightCancellationJourney().getDepartureCityCode().isEmpty() ||
                element.getFlightCancellationJourney().getDepartureCityCode().length() == 0) ?
                element.getFlightCancellationJourney().getDepartureAiportId() :
                element.getFlightCancellationJourney().getDepartureCityCode();
        String arrivalCityAirportCode = (element.getFlightCancellationJourney().getArrivalCityCode().isEmpty() ||
                element.getFlightCancellationJourney().getArrivalCityCode().length() == 0) ?
                element.getFlightCancellationJourney().getArrivalAirportId() :
                element.getFlightCancellationJourney().getArrivalCityCode();
        String departureDate = FlightDateUtil.formatDate(
                FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                FlightDateUtil.FORMAT_DATE,
                element.getFlightCancellationJourney().getDepartureTime());
        String departureTime = FlightDateUtil.formatDate(
                FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                FlightDateUtil.FORMAT_TIME_DETAIL,
                element.getFlightCancellationJourney().getDepartureTime());
        String arrivalTime = FlightDateUtil.formatDate(
                FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                FlightDateUtil.FORMAT_TIME_DETAIL,
                element.getFlightCancellationJourney().getArrivalTime());


        txtDepartureDetail.setText(
                String.format(context.getString(R.string.flight_cancellation_journey_title),
                        getAdapterPosition() + 1,
                        departureDate)
        );
        txtJourneyDetail.setText(
                String.format("%s (%s) - %s (%s)",
                        element.getFlightCancellationJourney().getDepartureCity(),
                        departureCityAirportCode,
                        element.getFlightCancellationJourney().getArrivalCity(),
                        arrivalCityAirportCode)
        );
        txtAirlineName.setText(element.getFlightCancellationJourney().getAirlineName());
        txtDuration.setText(
                String.format(getString(R.string.flight_booking_trip_info_airport_format),
                        departureTime,
                        arrivalTime)
        );

        passengerAdapter.addData(element.getPassengerViewModelList());

        checkBoxFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uncheckAllData = true;
            }
        });

        checkBoxFlight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isJourneyChecked = isChecked;

                if (isJourneyChecked) {
                    passengerAdapter.checkAllData();
                } else if (uncheckAllData) {
                    passengerAdapter.uncheckAllData();
                }
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckJourney(true);
            }
        });

    }

    private void toggleCheckJourney(boolean uncheckAllData) {
        this.uncheckAllData = uncheckAllData;
        checkBoxFlight.setChecked(!isJourneyChecked);
    }

    private class PassengerAdapter extends RecyclerView.Adapter<PassengerViewHolder>
            implements FlightPassengerAdapterListener {

        List<FlightCancellationPassengerViewModel> passengerViewModelList;
        List<PassengerViewHolder> passengerViewHolderList = new ArrayList<>();

        public PassengerAdapter() {
            this.passengerViewModelList = new ArrayList<>();
        }

        @Override
        public PassengerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_flight_cancellation_passenger, viewGroup, false);
            return new PassengerViewHolder(view, this);
        }

        @Override
        public void onBindViewHolder(PassengerViewHolder passengerViewHolder, int position) {
            passengerViewHolder.bindData(passengerViewModelList.get(position), getAdapterPosition());
            passengerViewHolderList.add(passengerViewHolder);

            if (listener.shouldCheckAll()) {
                toggleCheckJourney(true);
            }
        }

        @Override
        public int getItemCount() {
            return passengerViewModelList.size();
        }

        public void addData(List<FlightCancellationPassengerViewModel> passengerViewModelList) {
            this.passengerViewModelList.clear();
            this.passengerViewModelList.addAll(passengerViewModelList);
            notifyDataSetChanged();
        }

        public void checkAllData() {
            for (int index = 0; index < passengerViewHolderList.size(); index++) {
                passengerViewHolderList.get(index).onCheck(true);
            }
        }

        public void uncheckAllData() {
            for (int index = 0; index < passengerViewHolderList.size(); index++) {
                passengerViewHolderList.get(index).onCheck(false);
            }
        }

        @Override
        public void checkIfAllPassengerIsChecked() {
            boolean allChecked = true;
            for (int index = 0; index < passengerViewHolderList.size(); index++) {
                if (!passengerViewHolderList.get(index).isPassengerChecked()) {
                    allChecked = false;
                }
            }

            isJourneyChecked = !allChecked;
            if (allChecked) {
                toggleCheckJourney(true);
            } else {
                toggleCheckJourney(false);
            }
        }
    }

    private class PassengerViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPassengerName;
        private TextView txtPassengerType;
        private CheckBox checkBoxPassenger;
        private boolean isPassengerChecked = false;
        private FlightCancellationPassengerViewModel passengerViewModel;
        private int adapterPosition = -1;
        private FlightPassengerAdapterListener passengerListener;

        public PassengerViewHolder(View itemView, FlightPassengerAdapterListener passengerListener) {
            super(itemView);

            this.passengerListener = passengerListener;

            txtPassengerName = itemView.findViewById(R.id.tv_passenger_name);
            txtPassengerType = itemView.findViewById(R.id.tv_passenger_type);
            checkBoxPassenger = itemView.findViewById(R.id.checkbox);

            checkBoxPassenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheck(!listener.isChecked(passengerViewModel));
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheck(!isPassengerChecked);
                }
            });
        }

        public void bindData(FlightCancellationPassengerViewModel passengerViewModel, int adapterPosition) {
            this.passengerViewModel = passengerViewModel;
            this.adapterPosition = adapterPosition;

            txtPassengerName.setText(String.format("%s %s %s", passengerViewModel.getTitleString(),
                    passengerViewModel.getFirstName(), passengerViewModel.getLastName()));

            switch (passengerViewModel.getType()) {
                case ADULT:
                    txtPassengerType.setText(R.string.flightbooking_price_adult_label);
                    break;
                case CHILDREN:
                    txtPassengerType.setText(R.string.flightbooking_price_child_label);
                    break;
                case INFANT:
                    txtPassengerType.setText(R.string.flightbooking_price_infant_label);
                    break;
                default:
                    txtPassengerType.setText(R.string.flightbooking_price_adult_label);
            }

            updateCheckedButton(listener.isChecked(passengerViewModel));
            passengerListener.checkIfAllPassengerIsChecked();
        }

        private void onCheck(boolean checkStatus) {
            isPassengerChecked = checkStatus;

            if (isPassengerChecked) {
                listener.onPassengerChecked(passengerViewModel, adapterPosition);
            } else {
                listener.onPassengerUnchecked(passengerViewModel, adapterPosition);
            }

            updateCheckedButton(isPassengerChecked);
            passengerListener.checkIfAllPassengerIsChecked();
        }

        public void updateCheckedButton(boolean checkedStatus) {
            checkBoxPassenger.setChecked(checkedStatus);
        }

        public boolean isPassengerChecked() {
            return isPassengerChecked;
        }

        public FlightCancellationPassengerViewModel getPassengerViewModel() {
            return passengerViewModel;
        }
    }
}
