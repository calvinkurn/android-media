package com.tokopedia.home.beranda.data.datasource.local

import android.os.SystemClock
import com.tokopedia.home.beranda.data.datasource.local.dao.HomeDao
import com.tokopedia.home.beranda.data.datasource.local.entity.AtfCacheEntity
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.HomeRoomData
import com.tokopedia.home.beranda.helper.benchmark.TRACE_GET_CACHED_DATA_SOURCE
import com.tokopedia.home.beranda.helper.benchmark.TRACE_SAVE_TO_DATABASE
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
class HomeCachedDataSource(
        private val homeDao: HomeDao) {
    private val timeout = TimeUnit.DAYS.toMillis(30)

    suspend fun saveCachedAtf(items: List<AtfCacheEntity>) {
        homeDao.deleteAtfTable()
        homeDao.save(items)
    }

    fun getCachedAtfData(): List<AtfCacheEntity> {
        return homeDao.getHomeDataObject()
    }

    fun getCachedHomeData(): Flow<HomeData?> {
        BenchmarkHelper.beginSystraceSection(TRACE_GET_CACHED_DATA_SOURCE)
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

    suspend fun saveToDatabase(homeData: HomeData?) {
        BenchmarkHelper.beginSystraceSection(TRACE_SAVE_TO_DATABASE)
        homeDao.save(HomeRoomData(homeData = homeData))
        BenchmarkHelper.endSystraceSection()
    }

    fun deleteHomeData() {
        homeDao.deleteHomeData()
    }
}