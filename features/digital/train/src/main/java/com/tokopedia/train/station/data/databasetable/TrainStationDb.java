package com.tokopedia.train.station.data.databasetable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.train.common.database.TkpdTrainDatabase;

/**
 * @author by alvarisi on 3/7/18.
 */
@Table(database = TkpdTrainDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class TrainStationDb extends BaseModel {
    @PrimaryKey
    @Column(name = "station_id")
    private
    int stationId;
    @PrimaryKey
    @Column(name = "station_name")
    private
    String stationName;
    @Column(name = "station_display_name")
    private
    String stationDisplayName;
    @PrimaryKey
    @Column(name = "station_code")
    private
    String stationCode;
    @Column(name = "popularity_order")
    private
    int popularityOrder;
    @Column(name = "city_id")
    private
    int cityId;
    @Column(name = "city_name")
    private
    String cityName;
    @Column(name = "island_name")
    private
    String islandName;

    public TrainStationDb() {
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public int getPopularityOrder() {
        return popularityOrder;
    }

    public void setPopularityOrder(int popularityOrder) {
        this.popularityOrder = popularityOrder;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
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

    public String getStationDisplayName() {
        return stationDisplayName;
    }

    public void setStationDisplayName(String stationDisplayName) {
        this.stationDisplayName = stationDisplayName;
    }
}
