package com.tokopedia.train.station.presentation.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.train.station.presentation.adapter.TrainStationTypeFactory;

/**
 * Created by Alvarisi on 21/02/18.
 */

public class TrainStationCityViewModel implements Visitable<TrainStationTypeFactory> {
    private String cityName;
    private String islandName;

    @Override
    public int type(TrainStationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getIslandName() {
        return islandName;
    }

    public void setIslandName(String islandName) {
        this.islandName = islandName;
    }

}
