package com.tokopedia.train.seat.presentation.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.train.seat.presentation.fragment.adapter.TrainSeatAdapterTypeFactory;

public class TrainSeatTopLabelViewModel implements Visitable<TrainSeatAdapterTypeFactory> {
    private String label;

    public TrainSeatTopLabelViewModel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int type(TrainSeatAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
