package com.tokopedia.flight.cancellation.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.cancellation.view.adapter.viewholder.FlightCancellationAttachmentButtonViewHolder;
import com.tokopedia.flight.cancellation.view.adapter.viewholder.FlightCancellationAttachmentViewHolder;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentButtonViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;

/**
 * @author  by alvarisi on 3/26/18.
 */

public class FlightCancellationAttachementAdapterTypeFactory extends BaseAdapterTypeFactory implements FlightCancellationAttachmentTypeFactory {

    public interface OnAdapterInteractionListener{
        void onUploadAttachmentButtonClicked(int positionIndex);

        void deleteAttachement(FlightCancellationAttachmentViewModel element);

        void viewImage(String filePath);
    }

    private OnAdapterInteractionListener interactionListener;
    private boolean showChangeButton;

    public FlightCancellationAttachementAdapterTypeFactory(OnAdapterInteractionListener interactionListener, boolean showChangeButton) {
        this.interactionListener = interactionListener;
        this.showChangeButton = showChangeButton;
    }

    @Override
    public int type(FlightCancellationAttachmentViewModel flightCancellationAttachmentViewModel) {
        return FlightCancellationAttachmentViewHolder.LAYOUT;
    }

    @Override
    public int type(FlightCancellationAttachmentButtonViewModel flightCancellationAttachmentButtonViewModel) {
        return FlightCancellationAttachmentButtonViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightCancellationAttachmentButtonViewHolder.LAYOUT){
            return new FlightCancellationAttachmentButtonViewHolder(parent, interactionListener);
        }else if (type == FlightCancellationAttachmentViewHolder.LAYOUT){
            return new FlightCancellationAttachmentViewHolder(parent, interactionListener, showChangeButton);
        }else {
            return super.createViewHolder(parent, type);
        }
    }
}
