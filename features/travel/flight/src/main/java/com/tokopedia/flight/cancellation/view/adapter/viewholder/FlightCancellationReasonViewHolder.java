package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonModel;

/**
 * @author by furqan on 30/10/18.
 */

public class FlightCancellationReasonViewHolder extends AbstractViewHolder<FlightCancellationReasonModel> {

    @LayoutRes
    public static int LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_amenity;

    private ReasonListener listener;

    private TextView title;
    private ImageView imageChecked;

    public FlightCancellationReasonViewHolder(View itemView, ReasonListener listener) {
        super(itemView);

        title = (TextView) itemView.findViewById(com.tokopedia.flight.R.id.tv_title);
        imageChecked = (ImageView) itemView.findViewById(com.tokopedia.flight.R.id.image_checked);
        this.listener = listener;
    }


    @Override
    public void bind(FlightCancellationReasonModel element) {
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
        boolean isItemChecked(FlightCancellationReasonModel selectedItem);
    }
}
