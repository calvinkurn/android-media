package com.tokopedia.train.station.data.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Created by nabillasabbaha on 31/01/19.
 * dont forget to change the db room version if something change in this table
 */
@Entity(primaryKeys = {"stationId", "stationName", "stationCode"})
public class TrainStationTable {
    @NonNull
    private int stationId;
    @NonNull
    private String stationName;
    @NonNull
    private String stationCode;
    private String stationDisplayName;
    private int popularityOrder;
    private int cityId;
    private String cityName;
    private String islandName;

    @NonNull
    public int getStationId() {
        return stationId;
    }

    @NonNull
    public String getStationName() {
        return stationName;
    }

    @NonNull
    public String getStationCode() {
        return stationCode;
    }

    public String getStationDisplayName() {
        return stationDisplayName;
    }

    public int getPopularityOrder() {
        return popularityOrder;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getIslandName() {
        return islandName;
    }

    public void setStationId(@NonNull int stationId) {
        this.stationId = stationId;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public void setStationDisplayName(String stationDisplayName) {
        this.stationDisplayName = stationDisplayName;
    }

    public void setPopularityOrder(int popularityOrder) {
        this.popularityOrder = popularityOrder;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setIslandName(String islandName) {
        this.islandName = islandName;
    }
}
