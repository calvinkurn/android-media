package com.tokopedia.gamification.taptap.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.tokopedia.gamification.data.entity.CrackResultEntity;

import java.util.List;

@Dao
public interface GamificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CrackResultEntity trainStationTables);

    @Query("SELECT * FROM CrackResultEntity")
    List<CrackResultEntity> getAllRewards();

    @Query("DELETE FROM CrackResultEntity")
    int deleteAll();

    @Query("SELECT * FROM CrackResultEntity WHERE campaignId <> :code")
    List<CrackResultEntity> getRewardsNotFor(long code);
//
//    @Query("SELECT * FROM TrainStationTable WHERE popularityOrder > 0 ORDER BY popularityOrder ASC")
//    List<TrainStationTable> getPopularStations();
//
//    @Query("SELECT * FROM TrainStationTable WHERE stationCode LIKE :query OR stationName LIKE :query OR stationDisplayName LIKE :query OR cityName LIKE :query")
//    List<TrainStationTable> getStationsByKeyword(String query);
//
//    @Query("SELECT * FROM TrainStationTable WHERE cityName LIKE :query GROUP BY cityName ORDER BY cityName ASC")
//    List<TrainStationTable> getStationsCitiesByKeyword(String query);
//
}
