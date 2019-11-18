package com.tokopedia.v2.home.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RoomWarnings
import com.tokopedia.v2.home.model.pojo.home.HomeData

@Dao
abstract class HomeDao : BaseDao<HomeData>(){

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM HomeData LIMIT 1")
    abstract suspend fun getHomeData(): HomeData

    suspend fun save(homeData: HomeData){
        insert(homeData)
    }
}