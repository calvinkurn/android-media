package com.tokopedia.train.seat.presentation.fragment.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.train.seat.presentation.fragment.adapter.viewholder.TrainSeatTopLabelViewHolder;
import com.tokopedia.train.seat.presentation.fragment.adapter.viewholder.TrainSeatViewHolder;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatTopLabelViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;

import java.util.List;

public class TrainSeatAdapterTypeFactory extends BaseAdapterTypeFactory {
    public TrainSeatAdapterTypeFactory(ActionListener listener) {
        this.listener = listener;
    }

    public int type(TrainSeatTopLabelViewModel trainSeatTopLabelViewModel) {
        return TrainSeatTopLabelViewHolder.LAYOUT;
    }

    public interface ActionListener {
        List<TrainSeatPassengerViewModel> getPassengers();

        List<TrainSeatViewModel> getSelectedSeatInCurrentWagon();

        void seatClicked(TrainSeatViewModel viewModel, int position, int height);

        String getWagonCode();
    }

    private ActionListener listener;

    public int type(TrainSeatViewModel viewModel) {
        return TrainSeatViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == TrainSeatViewHolder.LAYOUT) {
            return new TrainSeatViewHolder(parent, listener);
        }if (type == TrainSeatTopLabelViewHolder.LAYOUT) {
            return new TrainSeatTopLabelViewHolder(parent);
        } else
            return super.createViewHolder(parent, type);
    }
}
