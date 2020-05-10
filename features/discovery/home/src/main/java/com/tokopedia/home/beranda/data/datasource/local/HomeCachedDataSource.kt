package com.tokopedia.home.beranda.data.datasource.local

import android.os.SystemClock
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeRoomData
import com.tokopedia.home.beranda.helper.BenchmarkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
class HomeCachedDataSource(
        private val homeDao: HomeDao) {
    private val timeout = TimeUnit.DAYS.toMillis(30)

    fun getCachedHomeData(): Flow<HomeData?> {
        BenchmarkHelper.beginSystraceSection("HomeCachedDataSource.getCachedHomeData")
        return homeDao.getHomeData().map {
            if(SystemClock.uptimeMillis() - (it?.modificationDate?.time ?: SystemClock.uptimeMillis()) > timeout){
                homeDao.deleteHomeData()
                null
            } else {
                BenchmarkHelper.endSystraceSection()
                it?.homeData
            }
        }
    }

    suspend fun saveToDatabase(homeData: HomeData) {
        BenchmarkHelper.beginSystraceSection("HomeCachedDataSource.saveToDatabase")
        homeDao.save(HomeRoomData(homeData = homeData))
        BenchmarkHelper.endSystraceSection()
    }
}