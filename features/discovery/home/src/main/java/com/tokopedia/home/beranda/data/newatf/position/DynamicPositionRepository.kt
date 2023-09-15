package com.tokopedia.home.beranda.data.newatf.position

import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfMapper.mapCachedAtfData
import com.tokopedia.home.beranda.data.newatf.AtfMapper.mapRemoteAtfData
import com.tokopedia.home.beranda.data.newatf.AtfDataList
import com.tokopedia.home.beranda.domain.interactor.repository.HomeAtfRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DynamicPositionRepository @Inject constructor(
    private val atfDao: AtfDao,
    private val atfDataRepository: HomeAtfRepository,
) {
    val flow: StateFlow<AtfDataList?>
        get() = _flow
    private var _flow: MutableStateFlow<AtfDataList?> = MutableStateFlow(null)

    suspend fun getData() {
        coroutineScope {
            launch { getCachedData() }
            launch { getRemoteData() }
        }
    }

    private suspend fun getCachedData() {
        val cachedData = AtfDataList(
            listAtfData = atfDao.getAtfDynamicPosition().map { it.mapCachedAtfData() },
            isCache = true,
        )
        _flow.emit(cachedData)
    }

    suspend fun getRemoteData() {
        val remoteData = AtfDataList(
            listAtfData = atfDataRepository.getRemoteData().dataList.map { it.mapRemoteAtfData() },
            isCache = false,
        )
        _flow.emit(remoteData)
    }
}
