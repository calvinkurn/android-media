package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.position.DynamicPositionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

abstract class AtfRepository (
    private val atfDao: AtfDao
) {
    companion object {
        private const val FLOW_TIMEOUT_MILLIS = 5000L
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _flow: MutableStateFlow<AtfData?> = MutableStateFlow(null)
    val flow: StateFlow<AtfData?>
        get() = _flow.stateIn(
            coroutineScope,
            SharingStarted.WhileSubscribed(FLOW_TIMEOUT_MILLIS),
            null
        )

    abstract suspend fun getData(atfMetadata: AtfMetadata)

    open suspend fun emitData(atfData: AtfData) {
        _flow.emit(atfData)
    }
}
