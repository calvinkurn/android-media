package com.tokopedia.flight.cancellation.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentButtonModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 3/26/18.
 */

public class FlightCancellationAttachmentAdapter extends BaseAdapter<FlightCancellationAttachmentTypeFactory> {

    public FlightCancellationAttachmentAdapter(FlightCancellationAttachmentTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }

    public FlightCancellationAttachmentAdapter(FlightCancellationAttachmentTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }

    public void hideAttachmentButton() {
        if (visitables.size() > 0 && visitables.get(visitables.size() - 1) instanceof FlightCancellationAttachmentButtonModel) {
            visitables.remove(visitables.size() - 1);
            notifyDataSetChanged();
        }
    }


    public List<FlightCancellationAttachmentModel> getData() {
        List<FlightCancellationAttachmentModel> list = new ArrayList<>();
        for (Visitable visitable : this.visitables) {
            try {
                FlightCancellationAttachmentModel item = (FlightCancellationAttachmentModel) visitable;
                list.add(item);
            } catch (ClassCastException exception) {
                exception.printStackTrace();
            }
        }
        return list;
    }

    public void removeAttachment(FlightCancellationAttachmentModel element) {
        List<FlightCancellationAttachmentModel> viewModels = getData();
        int index = viewModels.indexOf(element);
        if (index != -1) {
            viewModels.remove(index);
            visitables.clear();
            visitables.addAll(viewModels);
            showAttachmentButton();
        }
    }

    public void showAttachmentButton() {
        visitables.add(new FlightCancellationAttachmentButtonModel());
        notifyDataSetChanged();
    }
}
