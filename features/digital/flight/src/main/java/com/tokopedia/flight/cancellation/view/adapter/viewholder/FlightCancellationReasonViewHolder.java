package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonViewModel;

/**
 * @author by furqan on 30/10/18.
 */

public class FlightCancellationReasonViewHolder extends AbstractViewHolder<FlightCancellationReasonViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_booking_amenity;

    private ReasonListener listener;

    private TextView title;
    private ImageView imageChecked;

    public FlightCancellationReasonViewHolder(View itemView, ReasonListener listener) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.tv_title);
        imageChecked = (ImageView) itemView.findViewById(R.id.image_checked);
        this.listener = listener;
    }


    @Override
    public void bind(FlightCancellationReasonViewModel element) {
        boolean isItemChecked = false;
        if (listener != null) {
            isItemChecked = listener.isItemChecked(element);
        }

        title.setText(element.getDetail());
        if (isItemChecked) {
            imageChecked.setVisibility(View.VISIBLE);
        } else {
            imageChecked.setVisibility(View.GONE);
        }
    }

    public interface ReasonListener {
        boolean isItemChecked(FlightCancellationReasonViewModel selectedItem);
    }
}
