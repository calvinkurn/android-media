package com.tokopedia.flight.passenger.view.adapter.viewholder;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

/**
 * @author by furqan on 23/02/18.
 */

public class FlightPassengerListViewHolder extends AbstractViewHolder<FlightBookingPassengerViewModel> {

    @LayoutRes
    public static int LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_saved_passenger;

    public interface ListenerCheckedSavedPassenger {
        void deletePassenger(String passengerId);

        void editPassenger(FlightBookingPassengerViewModel passengerViewModel);
    }

    private ListenerCheckedSavedPassenger listenerCheckedSavedPassenger;

    private Context context;
    private TextView txtPassengerName;
    private ImageView imgEdit, imgDelete;

    public FlightPassengerListViewHolder(View itemView, final ListenerCheckedSavedPassenger listenerCheckedSavedPassenger) {
        super(itemView);
        txtPassengerName = itemView.findViewById(com.tokopedia.flight.R.id.tv_passenger_name);
        imgDelete = itemView.findViewById(com.tokopedia.flight.R.id.image_passenger_delete);
        imgEdit = itemView.findViewById(com.tokopedia.flight.R.id.image_passenger_edit);
        this.listenerCheckedSavedPassenger = listenerCheckedSavedPassenger;
        this.context = itemView.getContext();
    }

    @Override
    public void bind(final FlightBookingPassengerViewModel flightBookingPassengerViewModel) {

        txtPassengerName.setText(String.format(
                "%s. %s %s",
                flightBookingPassengerViewModel.getPassengerTitle(),
                flightBookingPassengerViewModel.getPassengerFirstName(),
                flightBookingPassengerViewModel.getPassengerLastName()
        ));

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerCheckedSavedPassenger.deletePassenger(flightBookingPassengerViewModel.getPassengerId());
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerCheckedSavedPassenger.editPassenger(flightBookingPassengerViewModel);
            }
        });
    }
}
