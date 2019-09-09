package com.tokopedia.gamification.taptap.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

}
