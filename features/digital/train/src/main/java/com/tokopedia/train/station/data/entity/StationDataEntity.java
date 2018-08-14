package com.tokopedia.train.station.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 14/05/18.
 */
public class StationDataEntity {

    @SerializedName("kaiStations")
    @Expose
    private List<TrainStationIslandEntity> stations;

    public List<TrainStationIslandEntity> getStations() {
        return stations;
    }
}
