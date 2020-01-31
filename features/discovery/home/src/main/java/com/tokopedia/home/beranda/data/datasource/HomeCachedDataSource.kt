package com.tokopedia.home.beranda.data.datasource

import android.os.SystemClock
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeRoomData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Named
class HomeCachedDataSource(
        private val homeDao: HomeDao) {
    private val timeout = TimeUnit.DAYS.toMillis(30)

    suspend fun getCachedHomeData(): Flow<HomeData?> {
        return homeDao.getHomeData().map {
            if(SystemClock.uptimeMillis() - (it?.modificationDate?.time ?: SystemClock.uptimeMillis()) > timeout){
                homeDao.deleteHomeData()
                null
            } else {
                it?.homeData
            }
        }
    }

    suspend fun saveToDatabase(homeData: HomeData) {
        homeDao.save(HomeRoomData(homeData = homeData))
    }
}