package com.tokopedia.flight.booking.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingAmenityViewHolder extends AbstractViewHolder<FlightBookingAmenityViewModel> {
    @LayoutRes
    public static int LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_amenity;

    public interface ListenerCheckedLuggage {
        boolean isItemChecked(FlightBookingAmenityViewModel selectedItem);
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
    public void bind(FlightBookingAmenityViewModel flightBookingLuggageViewModel) {
        boolean isItemChecked = false;
        if (listenerCheckedLuggage != null) {
            isItemChecked = listenerCheckedLuggage.isItemChecked(flightBookingLuggageViewModel);
        }

        title.setText(String.format("%s - %s", flightBookingLuggageViewModel.getTitle(), flightBookingLuggageViewModel.getPrice()));
        if (isItemChecked) {
            imageChecked.setVisibility(View.VISIBLE);
            title.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.design.R.color.tkpd_main_green));
        } else {
            imageChecked.setVisibility(View.INVISIBLE);
            title.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.design.R.color.font_black_primary_70));
        }
    }
}
