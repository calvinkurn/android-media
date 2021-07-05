package com.tokopedia.flight.orderlist.view.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.orderlist.R;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderAmenityViewModel;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightOrderAmenityViewHolder extends AbstractViewHolder<FlightOrderAmenityViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_order_amenity;

    public interface ListenerCheckedLuggage {
        boolean isItemChecked(FlightOrderAmenityViewModel selectedItem);

        void resetItemCheck();
    }

    private ListenerCheckedLuggage listenerCheckedLuggage;

    private TextView title;
    private ImageView imageChecked;

    public FlightOrderAmenityViewHolder(View itemView, ListenerCheckedLuggage listenerCheckedLuggage) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.tv_title);
        imageChecked = (ImageView) itemView.findViewById(R.id.image_checked);
        this.listenerCheckedLuggage = listenerCheckedLuggage;
    }

    @Override
    public void bind(FlightOrderAmenityViewModel flightBookingLuggageViewModel) {
        boolean isItemChecked = false;
        if (listenerCheckedLuggage != null) {
            isItemChecked = listenerCheckedLuggage.isItemChecked(flightBookingLuggageViewModel);
        }

        title.setText(String.format("%s - %s", flightBookingLuggageViewModel.getTitle(), flightBookingLuggageViewModel.getPrice()));
        if (isItemChecked) {
            imageChecked.setVisibility(View.VISIBLE);
            title.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400));
        } else {
            imageChecked.setVisibility(View.INVISIBLE);
            title.setTextColor(ContextCompat.getColor(itemView.getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
        }
    }
}
