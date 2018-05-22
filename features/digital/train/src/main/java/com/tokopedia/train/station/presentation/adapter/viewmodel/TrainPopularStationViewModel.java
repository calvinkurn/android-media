package com.tokopedia.train.station.presentation.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.train.station.presentation.adapter.TrainStationTypeFactory;

import java.util.List;

/**
 * @author by Rizky on 21/02/18.
 */

public class TrainPopularStationViewModel implements Visitable<TrainStationTypeFactory> {
    private List<TrainStationViewModel> stations;

    @Override
    public int type(TrainStationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    public List<TrainStationViewModel> getStations() {
        return stations;
    }

    public void setStations(List<TrainStationViewModel> stations) {
        this.stations = stations;
    }
}
