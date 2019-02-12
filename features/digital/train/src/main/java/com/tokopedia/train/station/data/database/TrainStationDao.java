package com.tokopedia.train.station.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by nabillasabbaha on 31/01/19.
 */
@Dao
public interface TrainStationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<TrainStationTable> trainStationTables);

    @Query("SELECT * FROM TrainStationTable")
    List<TrainStationTable> getAllStation();

    @Query("SELECT * FROM TrainStationTable WHERE stationCode = :code")
    TrainStationTable getStationByStationCode(String code);

    @Query("SELECT * FROM TrainStationTable WHERE popularityOrder > 0 ORDER BY popularityOrder ASC")
    List<TrainStationTable> getPopularStations();

    @Query("SELECT * FROM TrainStationTable WHERE stationCode LIKE :query OR stationName LIKE :query OR stationDisplayName LIKE :query OR cityName LIKE :query")
    List<TrainStationTable> getStationsByKeyword(String query);

    @Query("SELECT * FROM TrainStationTable WHERE cityName LIKE :query GROUP BY cityName ORDER BY cityName ASC")
    List<TrainStationTable> getStationsCitiesByKeyword(String query);

    @Query("DELETE FROM TrainStationTable")
    int deleteAll();
}
