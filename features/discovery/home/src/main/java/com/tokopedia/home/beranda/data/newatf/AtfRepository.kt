package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class AtfRepository (
    private val atfDao: AtfDao
) {
    val flow: StateFlow<AtfData?>
        get() = _flow
    open var _flow: MutableStateFlow<AtfData?> = MutableStateFlow(null)

    abstract suspend fun getData(atfMetadata: AtfMetadata)

    open suspend fun emitAndSaveData(atfData: AtfData) {
        val atfCacheEntity = AtfMapper.mapRemoteToCache(atfData)
        _flow.emit(atfData)
        atfDao.updateAtfData(atfCacheEntity)
    }
}
