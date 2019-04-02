package com.tokopedia.common.travel.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import rx.Single;

/**
 * Created by nabillasabbaha on 17/12/18.
 */
@Dao
public interface TravelPassengerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<TravelPassengerTable> travelPassengerTables);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TravelPassengerTable travelPassengerTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(TravelPassengerTable travelPassengerTable);

    @Query("SELECT * FROM TravelPassengerTable WHERE name = :name")
    TravelPassengerTable findTravelPassengerByName(String name);

    @Query("SELECT * FROM TravelPassengerTable WHERE idPassenger = :idPassenger")
    TravelPassengerTable findTravelPassengerByIdPassenger(String idPassenger);

    @Query("UPDATE TravelPassengerTable SET selected = :isSelected WHERE idPassenger = :idPassenger")
    int updateTravelPassenger(int isSelected, String idPassenger);

    @Query("SELECT * FROM TravelPassengerTable")
    List<TravelPassengerTable> findAllTravelPassenger();

    @Query("DELETE FROM TravelPassengerTable")
    int deleteAll();

    @Query("DELETE FROM TravelPassengerTable WHERE idPassenger = :idPassenger")
    int deletePassengerById(String idPassenger);
}
