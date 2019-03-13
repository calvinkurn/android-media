package com.tokopedia.train.search.data.database;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import java.util.List;

/**
 * Created by nabillasabbaha on 01/02/19.
 */
@Dao
public interface TrainScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<TrainScheduleTable> trainScheduleTableList);

    @Query("UPDATE TrainScheduleTable SET availableSeat = :availableSeat WHERE idSchedule = :scheduleId")
    int updateAvailableSeat(String scheduleId, int availableSeat);

    @Query("SELECT * FROM TrainScheduleTable")
    List<TrainScheduleTable> findSchedules();

    @Query("SELECT MIN(adultFare) as minAdultFare FROM TrainScheduleTable")
    int findCheapestSchedules();

    @Query("SELECT MIN(duration) as minDuration FROM TrainScheduleTable")
    int findFastestSchedules();

    @Query("UPDATE TrainScheduleTable SET cheapestFlag = 1 WHERE adultFare = :minAdultFare")
    int updateCheapestFlag(int minAdultFare);

    @Query("UPDATE TrainScheduleTable SET fastestFlag = 1 WHERE duration = :minDuration")
    int updateFastestFlag(int minDuration);

    @RawQuery
    List<TrainScheduleTable> findSchedules(SimpleSQLiteQuery query);

    @Query("SELECT * FROM TrainScheduleTable WHERE idSchedule = :scheduleId")
    TrainScheduleTable getScheduleById(String scheduleId);

    @Query("DELETE FROM TrainScheduleTable")
    int deleteAll();
}
