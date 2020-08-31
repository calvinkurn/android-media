package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachementAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentButtonModel;

/**
 * @author by alvarisi on 3/26/18.
 */

public class FlightCancellationAttachmentButtonViewHolder extends AbstractViewHolder<FlightCancellationAttachmentButtonModel> {

    @LayoutRes
    public static int LAYOUT = com.tokopedia.flight.R.layout.item_flight_cancellation_attachment_button;

    private LinearLayout containerLayout;
    private FlightCancellationAttachementAdapterTypeFactory.OnAdapterInteractionListener interactionListener;

    public FlightCancellationAttachmentButtonViewHolder(View itemView, FlightCancellationAttachementAdapterTypeFactory.OnAdapterInteractionListener interactionListener) {
        super(itemView);
        containerLayout = itemView.findViewById(com.tokopedia.flight.R.id.upload_container);
        this.interactionListener = interactionListener;
        containerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FlightCancellationAttachmentButtonViewHolder.this.interactionListener != null) {
                    FlightCancellationAttachmentButtonViewHolder.this.interactionListener.onUploadAttachmentButtonClicked(getAdapterPosition());
                }
            }
        });
    }

    @Override
    public void bind(FlightCancellationAttachmentButtonModel element) {

    }
}
