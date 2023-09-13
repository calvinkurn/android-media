package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfMapper.mapToNewAtfData
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
    val flow: StateFlow<ListNewAtfData>
        get() = _flow
    private var _flow: MutableStateFlow<ListNewAtfData> = MutableStateFlow(ListNewAtfData())

    suspend fun getData() {
        coroutineScope {
            launch {
                getCachedData()
            }

            launch {
                getRemoteData()
            }
        }
    }

    suspend fun getCachedData() {
        val cachedData = ListNewAtfData(
            atfDao.getAtfDynamicPosition().map { it.mapToNewAtfData() },
            AtfSource.CACHE
        )
        _flow.emit(cachedData)
    }

    suspend fun getRemoteData() {
        val remoteData = ListNewAtfData(
            atfDataRepository.getRemoteData().dataList.map { it.mapToNewAtfData() },
            AtfSource.REMOTE
        )
        _flow.emit(remoteData)
    }
}
