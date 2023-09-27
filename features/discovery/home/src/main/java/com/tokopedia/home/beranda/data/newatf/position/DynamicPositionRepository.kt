package com.tokopedia.home.beranda.data.newatf.position

import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfDataList
import com.tokopedia.home.beranda.data.newatf.AtfMapper
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeAtfRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HomeScope
class DynamicPositionRepository @Inject constructor(
    private val atfDao: AtfDao,
    private val atfDataRepository: HomeAtfRepository,
    private val homeRoomDataSource: HomeRoomDataSource,
) {
    private val _flow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)
    val flow: StateFlow<AtfDataList?> = _flow

    suspend fun getData() {
        coroutineScope {
            launch { getCachedData() }
            launch { getRemoteData() }
        }
    }

    private suspend fun getCachedData() {
        val cachedData = AtfDataList(
            listAtfData = atfDao.getAtfDynamicPosition().map(AtfMapper::mapCacheToDomainAtfData),
            isCache = true,
        )
        _flow.emit(cachedData)
    }

    suspend fun getRemoteData() {
        val listAtf = atfDataRepository.getRemoteData().dataList
        val remoteData = AtfDataList(
            listAtfData = listAtf.mapIndexed(AtfMapper::mapRemoteToDomainAtfData),
            isCache = false,
        )
        homeRoomDataSource.saveCachedAtf(listAtf.mapIndexed(AtfMapper::mapRemoteToCache))
        _flow.emit(remoteData)
    }
}
