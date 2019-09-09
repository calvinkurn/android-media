package com.tokopedia.train.seat.presentation.fragment.adapter.viewholder;

import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatTopLabelViewModel;

public class TrainSeatTopLabelViewHolder extends AbstractViewHolder<TrainSeatTopLabelViewModel> {
    public static final int LAYOUT = R.layout.item_train_seat_label;
    private AppCompatTextView labelTextView;

    public TrainSeatTopLabelViewHolder(View itemView) {
        super(itemView);
        labelTextView = itemView.findViewById(R.id.tv_label);
    }

    @Override
    public void bind(TrainSeatTopLabelViewModel element) {
        labelTextView.setText(element.getLabel());
    }
}
