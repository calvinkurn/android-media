package com.tokopedia.flight.passenger.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingAmenityViewHolder extends AbstractViewHolder<FlightBookingAmenityModel> {
    @LayoutRes
    public static int LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_amenity;

    public interface ListenerCheckedLuggage {
        boolean isItemChecked(FlightBookingAmenityModel selectedItem);
        void resetItemCheck();
    }

    private ListenerCheckedLuggage listenerCheckedLuggage;

    private TextView title;
    private ImageView imageChecked;

    public FlightBookingAmenityViewHolder(View itemView, ListenerCheckedLuggage listenerCheckedLuggage) {
        super(itemView);
        title = (TextView) itemView.findViewById(com.tokopedia.flight.R.id.tv_title);
        imageChecked = (ImageView) itemView.findViewById(com.tokopedia.flight.R.id.image_checked);
        this.listenerCheckedLuggage = listenerCheckedLuggage;
    }

    @Override
    public void bind(FlightBookingAmenityModel flightBookingLuggageViewModel) {
        boolean isItemChecked = false;
        if (listenerCheckedLuggage != null) {
            isItemChecked = listenerCheckedLuggage.isItemChecked(flightBookingLuggageViewModel);
        }

        title.setText(String.format("%s - %s", flightBookingLuggageViewModel.getTitle(), flightBookingLuggageViewModel.getPrice()));
        if (isItemChecked) {
            imageChecked.setVisibility(View.VISIBLE);
            title.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G500));
        } else {
            imageChecked.setVisibility(View.INVISIBLE);
            title.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        }
    }
}
