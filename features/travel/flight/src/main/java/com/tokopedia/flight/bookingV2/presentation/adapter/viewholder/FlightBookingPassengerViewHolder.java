package com.tokopedia.flight.bookingV2.presentation.adapter.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.flight.bookingV2.presentation.adapter.FlightBookingPassengerActionListener;
import com.tokopedia.flight.bookingV2.presentation.adapter.FlightSimpleAdapter;
import com.tokopedia.flight.bookingV2.presentation.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.bookingV2.presentation.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.bookingV2.presentation.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.bookingV2.presentation.viewmodel.SimpleViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 12/7/17.
 */

public class FlightBookingPassengerViewHolder extends AbstractViewHolder<FlightBookingPassengerViewModel> {
    @LayoutRes
    public static final int LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_passenger;

    private LabelView headerLabel;
    private LinearLayout passengerDetailLayout;
    private RecyclerView rvPassengerDetail;

    private FlightBookingPassengerActionListener listener;

    public FlightBookingPassengerViewHolder(View itemView, FlightBookingPassengerActionListener listener) {
        super(itemView);
        this.listener = listener;
        findViews(itemView);
    }

    private void findViews(View view) {
        headerLabel = (LabelView) view.findViewById(com.tokopedia.flight.R.id.header_label);
        passengerDetailLayout = (LinearLayout) view.findViewById(com.tokopedia.flight.R.id.passenger_detail_layout);
        rvPassengerDetail = (RecyclerView) view.findViewById(com.tokopedia.flight.R.id.rv_list_details);
    }

    @Override
    public void bind(final FlightBookingPassengerViewModel viewModel) {
        headerLabel.setTitle(String.valueOf(viewModel.getHeaderTitle()));
        headerLabel.setContentColorValue(itemView.getResources().getColor(com.tokopedia.design.R.color.bg_button_green_border_outline));
        headerLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onChangePassengerData(viewModel);
                }
            }
        });
        if (viewModel.getPassengerFirstName() != null) {
            bindPassenger(viewModel);
        } else {
            passengerDetailLayout.setVisibility(View.GONE);
            headerLabel.setContent(itemView.getContext().getString(com.tokopedia.flight.R.string.flight_booking_passenger_fill_data_label));
        }
    }

    private void bindPassenger(FlightBookingPassengerViewModel viewModel) {
        passengerDetailLayout.setVisibility(View.VISIBLE);
        headerLabel.setContent(itemView.getContext().getString(com.tokopedia.flight.R.string.flight_booking_passenger_change_label));
        String passengerName = viewModel.getPassengerFirstName() + " " + viewModel.getPassengerLastName();
        if (viewModel.getPassengerTitle() != null && viewModel.getPassengerTitle().length() > 0) {
            passengerName = String.format("%s %s", viewModel.getPassengerTitle(), passengerName);
        }
        headerLabel.setTitle(String.valueOf(passengerName));
        initiatePassengerDetailView(viewModel);
    }

    private void initiatePassengerDetailView(FlightBookingPassengerViewModel viewModel) {
        List<SimpleViewModel> simpleViewModels = new ArrayList<>();
        if (viewModel.getPassengerBirthdate() != null && viewModel.getPassengerBirthdate().length() > 0) {
            simpleViewModels.add(new SimpleViewModel(itemView.getContext().getString(com.tokopedia.flight.R.string.flight_booking_list_passenger_birthdate_label) + "  | ", String.valueOf(FlightDateUtil.formatDate(
                    FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, viewModel.getPassengerBirthdate()
            ))));
        }

        if (viewModel.getPassportNumber() != null && viewModel.getPassportNumber().length() > 0) {
            simpleViewModels.add(new SimpleViewModel(itemView.getContext().getString(
                    com.tokopedia.flight.R.string.flight_passenger_passport_number_hint) + "  | ", viewModel.getPassportNumber()));
        }

        if (viewModel.getFlightBookingLuggageMetaViewModels() != null) {
            for (FlightBookingAmenityMetaViewModel flightBookingLuggageRouteViewModel : viewModel.getFlightBookingLuggageMetaViewModels()) {
                ArrayList<String> selectedLuggages = new ArrayList<>();
                for (FlightBookingAmenityViewModel flightBookingLuggageViewModel : flightBookingLuggageRouteViewModel.getAmenities()) {
                    selectedLuggages.add(flightBookingLuggageViewModel.getTitle());
                }
                simpleViewModels.add(new SimpleViewModel(
                        itemView.getContext().getString(com.tokopedia.flight.R.string.flight_booking_list_passenger_luggage_label) + " " + flightBookingLuggageRouteViewModel.getDescription() + "  | ",
                        TextUtils.join(" + ", selectedLuggages)
                ));
            }
        }

        if (viewModel.getFlightBookingMealMetaViewModels() != null && viewModel.getFlightBookingMealMetaViewModels().size() > 0) {
            for (FlightBookingAmenityMetaViewModel flightBookingMealRouteViewModel : viewModel.getFlightBookingMealMetaViewModels()) {
                simpleViewModels.add(new SimpleViewModel(
                        itemView.getContext().getString(com.tokopedia.flight.R.string.flight_booking_list_passenger_meals_label) + " " + flightBookingMealRouteViewModel.getDescription() + "  | ",
                        TextUtils.join(" + ", flightBookingMealRouteViewModel.getAmenities())
                ));
            }
        }

        FlightSimpleAdapter adapter = new FlightSimpleAdapter();
        adapter.setTitleHalfView(false);
        adapter.setContentAllignmentLeft(true);
        adapter.setDescriptionTextColor(itemView.getResources().getColor(com.tokopedia.design.R.color.font_black_secondary_54));
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(itemView.getContext(), RecyclerView.VERTICAL, false);
        rvPassengerDetail.setLayoutManager(layoutManager);
        rvPassengerDetail.setHasFixedSize(true);
        rvPassengerDetail.setNestedScrollingEnabled(false);
        rvPassengerDetail.setAdapter(adapter);
        adapter.setViewModels(simpleViewModels);
        adapter.notifyDataSetChanged();
    }

}
