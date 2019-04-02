package com.tokopedia.train.station.presentation.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.train.station.presentation.adapter.TrainStationTypeFactory;

public class TrainAllStationsViewModel implements Visitable<TrainStationTypeFactory> {
    public TrainAllStationsViewModel() {
    }

    @Override
    public int type(TrainStationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
